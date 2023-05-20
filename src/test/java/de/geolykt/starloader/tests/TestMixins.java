package de.geolykt.starloader.tests;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

/**
 * Checks for the integrity of the api mixins classes.
 */
public class TestMixins {

    /**
     * Checks whether there are an assignments on {@link org.spongepowered.asm.mixin.Shadow} fields
     * where it does not belong. (i. e. the initial starting value of these fields
     * should be not altered)
     *
     * @throws Exception Any exception thrown means that the unit test failed.
     */
    @Test
    public void testNoShadowAssignments() throws Exception {
        URL url = ClassLoader.getSystemResource("de/geolykt/starloader/apimixins");
        List<InputStream> childStreams = new ArrayList<>();
        JarFile toClose = null;
        if (url.getFile().indexOf('!') != -1) {
            JarFile file = new JarFile(URLDecoder.decode(url.getFile().substring(url.getFile().indexOf(":") + 1, url.getFile().indexOf("!")), StandardCharsets.UTF_8.name()));
            Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith("package-info.class") || !entry.getName().endsWith(".class")) {
                    continue;
                }
                childStreams.add(file.getInputStream(entry));
            }
            toClose = file;
        } else {
            @SuppressWarnings("null")
            File root = new File(url.toURI());
            File[] rootChild = root.listFiles((dir, name) -> !name.equals("package-info.class") && name.endsWith(".class"));
            if (rootChild != null) {
                for (File f : rootChild) {
                    childStreams.add(Files.newInputStream(f.toPath()));
                }
            }
        }
        if (childStreams.size() == 0) {
            throw new Exception("Unable to obtain mixin classes");
        }
        for (InputStream mixinStream : childStreams) {
            ClassNode clazzNode = new ClassNode(Opcodes.ASM9);
            ClassReader reader = new ClassReader(mixinStream);
            mixinStream.close();
            reader.accept(clazzNode, 0);
            Set<Map.Entry<String, String>> annotatedFields = new HashSet<>();
            for (FieldNode field : clazzNode.fields) {
                boolean hasAnnot = false;
                if (field.visibleAnnotations != null) {
                    for (AnnotationNode annot : field.visibleAnnotations) {
                        if (annot.desc.equals("Lorg/spongepowered/asm/mixin/Shadow;")) {
                            annotatedFields.add(new AbstractMap.SimpleImmutableEntry<>(field.name, field.desc));
                            hasAnnot = true;
                            break; // short-circuit
                        }
                    }
                }
                if (hasAnnot && field.value != null) {
                    // I do not think that field.value can be anything but null, but it is safe to be safe.
                    // This is a unit test after all
                    throw new AssertionError(clazzNode.name + "." + field.name + " " + field.desc
                            + " is annotated as @Shadow but has a starting value (" + field.value + ").");
                }
            }
            for (MethodNode method : clazzNode.methods) {
                if (method.desc.equals("()V") && (method.name.equals("<init>") || method.name.equals("<clinit>"))) {
                    InsnList insnList = method.instructions;
                    AbstractInsnNode instruction = insnList.getFirst();
                    while (instruction != null) {
                        if (instruction instanceof FieldInsnNode && (instruction.getOpcode() == Opcodes.PUTFIELD || instruction.getOpcode() == Opcodes.PUTSTATIC)) {
                            FieldInsnNode fieldInsn = (FieldInsnNode) instruction;
                            if (fieldInsn.owner.equals(clazzNode.name)
                                    && annotatedFields.contains(new AbstractMap.SimpleImmutableEntry<>(fieldInsn.name, fieldInsn.desc))) {
                                throw new AssertionError(clazzNode.name + "." + fieldInsn.name + " " + fieldInsn.desc
                                        + " is annotated as @Shadow but has a starting value.");
                            }
                        }
                        instruction = instruction.getNext();
                    }
                }
            }
        }
        if (toClose != null) {
            toClose.close();
        }
    }
}

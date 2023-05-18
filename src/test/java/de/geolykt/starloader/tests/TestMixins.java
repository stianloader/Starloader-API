package de.geolykt.starloader.tests;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        @SuppressWarnings("null")
        File[] children = new File(url.toURI()).listFiles((dir, name) -> !name.equals("package-info.class") && name.endsWith(".class"));
        if (children == null || children.length == 0) {
            throw new Exception("Unable to obtain mixin classes");
        }
        for (File mixinClass : children) {
            ClassNode clazzNode = new ClassNode(Opcodes.ASM9);
            FileInputStream fis = new FileInputStream(mixinClass);
            ClassReader reader = new ClassReader(fis);
            fis.close();
            reader.accept(clazzNode, 0);
            Set<Map.Entry<String, String>> annotatedFields = new HashSet<>();
            for (FieldNode field : clazzNode.fields) {
                boolean hasAnnot = false;
                if (field.visibleAnnotations != null) {
                    for (AnnotationNode annot : field.visibleAnnotations) {
                        if (annot.desc.equals("Lorg/spongepowered/asm/mixin/Shadow;")) {
                            annotatedFields.add(Map.entry(field.name, field.desc));
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
                                    && annotatedFields.contains(Map.entry(fieldInsn.name, fieldInsn.desc))) {
                                throw new AssertionError(clazzNode.name + "." + fieldInsn.name + " " + fieldInsn.desc
                                        + " is annotated as @Shadow but has a starting value.");
                            }
                        }
                        instruction = instruction.getNext();
                    }
                }
            }
        }
    }
}

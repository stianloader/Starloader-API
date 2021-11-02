package de.geolykt.starloader.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Checks the integrity of registries.
 * The most cursed thing ever
 */
public class TestRegistry {

    @SuppressWarnings("serial")
    private static final HashSet<String> ENUMS = new HashSet<>() {
        {
            add("snoddasmannen/galimulator/AudioManager$AudioSample.class");
            add("snoddasmannen/galimulator/EmpireSpecial.class");
            add("snoddasmannen/galimulator/EmpireState.class");
            add("snoddasmannen/galimulator/MapMode$MapModes.class");
            add("snoddasmannen/galimulator/weapons/WeaponsFactory.class");
            add("snoddasmannen/galimulator/FlagItem$BuiltinSymbols.class");
        }
    };

    private <@NotNull T extends Enum<T>> void requireRegistryCompleteness(ClassNode node, InputStream enumStream) throws Exception {
        ClassNode enumNode = new ClassNode(Opcodes.ASM9);
        ClassReader reader = new ClassReader(enumStream);
        reader.accept(enumNode, 0);

        enumMemberIterator: for (FieldNode field : enumNode.fields) {
            if ((field.access & Opcodes.ACC_ENUM) != 0) {
                for (MethodNode method : node.methods) {
                    if (method.instructions == null) {
                        continue;
                    }
                    for (AbstractInsnNode insn = method.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                        if (insn instanceof FieldInsnNode) {
                            FieldInsnNode fieldInsnNode = (FieldInsnNode) insn;
                            if (fieldInsnNode.name.equals(field.name) && fieldInsnNode.owner.equals(enumNode.name)) {
                                continue enumMemberIterator;
                            }
                        }
                    }
                }
                throw new AssertionError("Enum member " + enumNode.name + "." + field.name + " is not mapped in the enum registry.");
            }
        }
    }

    /**
     * Checks whether the enum registries have all enums registered that it should have.
     */
    @Test
    public void testRegistryCompleteness() throws Exception {
        URL url = ClassLoader.getSystemResource("de/geolykt/starloader/impl/registry/Registries.class");
        File registriesClass = new File(url.toURI());

        ClassNode clazzNode = new ClassNode(Opcodes.ASM9);
        FileInputStream fis = new FileInputStream(registriesClass);
        ClassReader reader = new ClassReader(fis);
        fis.close();
        reader.accept(clazzNode, 0);

        JarFile galimulatorJar = new JarFile(new File("build/obftools/intermediary.jar"));
        for (Enumeration<JarEntry> entries = galimulatorJar.entries(); entries.hasMoreElements();) {
            JarEntry entry = entries.nextElement();
            if (ENUMS.contains(entry.getName())) {
                InputStream stream = galimulatorJar.getInputStream(entry);
                requireRegistryCompleteness(clazzNode, stream);
                stream.close();
            }
        }
    }
}

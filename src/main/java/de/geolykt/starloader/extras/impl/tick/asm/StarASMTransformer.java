package de.geolykt.starloader.extras.impl.tick.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.geolykt.starloader.impl.StarplaneReobfuscateReference;
import de.geolykt.starloader.transformers.ASMTransformer;

public class StarASMTransformer extends ASMTransformer {

    private boolean transformed = false;

    @StarplaneReobfuscateReference
    @NotNull
    private static String tickMethod = "snoddasmannen/galimulator/Star.tick()V";

    @Override
    public boolean accept(@NotNull ClassNode node) {
        if (node.name.equals("snoddasmannen/galimulator/Star")) {
            transformed = true;
        }
        String tickMethodName = tickMethod.split("[\\.\\(]", 3)[1];
        boolean transformed = false;
        for (MethodNode method : node.methods) {
            if (method.name.equals(tickMethodName) && method.desc.equals("()V")) {
                transformed = true;
                method.instructions.clear();
                method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "de/geolykt/starloader/extras/impl/tick/StarTickingImplementation", "tick", "(Lsnoddasmannen/galimulator/Star;)V"));
                method.instructions.add(new InsnNode(Opcodes.RETURN));
            }
        }
        if (!transformed) {
            throw new IllegalStateException("Unable to find method " + tickMethod);
        }
        return false;
    }

    @Override
    public boolean isValidTraget(@NotNull String internalName) {
        return internalName.equals("snoddasmannen/galimulator/Star");
    }

    @Override
    public boolean isValid() {
        return !transformed;
    }
}

package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.geolykt.starloader.impl.StarplaneReobfuscateReference;
import de.geolykt.starloader.transformers.ASMTransformer;

/**
 * An {@link ASMTransformer} which has the only goal to transform the {@link SLInstrinsics} class.
 *
 * @since 2.0.0
 */
public class SLInstrinsicsTransformer extends ASMTransformer {

    @StarplaneReobfuscateReference
    private static String predicateConstructor = "snoddasmannen/galimulator/Space$ActorSpawningPredicate.<init>(Lsnoddasmannen/galimulator/actors/StateActorCreator;F)V";

    @Override
    public boolean accept(@NotNull ClassNode node) {
        if (!node.name.equals("de/geolykt/starloader/impl/asm/SLInstrinsics")) {
            return false;
        }
        for (MethodNode method : node.methods) {
            if (method.name.equals("createPredicate")) {
                method.instructions.clear();
                method.maxStack = 4;
                String[] parts = predicateConstructor.split("\\.");
                method.instructions.add(new TypeInsnNode(Opcodes.NEW, parts[0]));
                method.instructions.add(new InsnNode(Opcodes.DUP));
                method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // ALOAD creator
                method.instructions.add(new VarInsnNode(Opcodes.FLOAD, 1)); // FLOAD chance
                method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, parts[0], "<init>", parts[1].substring(parts[1].indexOf('('))));
                method.instructions.add(new InsnNode(Opcodes.ARETURN));
            }
        }
        return true;
    }

    @Override
    public boolean isValidTraget(@NotNull String internalName) {
        return internalName.equals("de/geolykt/starloader/impl/asm/SLInstrinsics");
    }

}

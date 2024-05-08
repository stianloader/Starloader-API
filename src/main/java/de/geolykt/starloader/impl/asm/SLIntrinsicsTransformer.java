package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.geolykt.starloader.starplane.annotations.MethodDesc;
import de.geolykt.starloader.starplane.annotations.ReferenceSource;
import de.geolykt.starloader.starplane.annotations.RemapMemberReference;
import de.geolykt.starloader.starplane.annotations.RemapMemberReference.ReferenceFormat;
import de.geolykt.starloader.transformers.ASMTransformer;

import snoddasmannen.galimulator.Space.ActorSpawningPredicate;
import snoddasmannen.galimulator.actors.StateActorCreator;

/**
 * An {@link ASMTransformer} which has the only goal to transform the {@link SLIntrinsics} class.
 *
 * @since 2.0.0
 */
public class SLIntrinsicsTransformer extends ASMTransformer {

    @RemapMemberReference(ownerType = ActorSpawningPredicate.class, name = "<init>", methodDesc = @MethodDesc(args = {StateActorCreator.class, float.class}, ret = void.class), format = ReferenceFormat.COMBINED_LEGACY)
    private static String predicateConstructor = ReferenceSource.getStringValue();

    @Override
    public boolean accept(@NotNull ClassNode node) {
        if (!node.name.equals("de/geolykt/starloader/impl/asm/SLIntrinsics")) {
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
    public int getPriority() {
        return -9_900;
    }

    @Override
    public boolean isValidTarget(@NotNull String internalName) {
        return internalName.equals("de/geolykt/starloader/impl/asm/SLIntrinsics");
    }
}

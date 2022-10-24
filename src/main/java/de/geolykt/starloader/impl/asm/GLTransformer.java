package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.geolykt.starloader.impl.gui.GLScissorState;
import de.geolykt.starloader.transformers.ASMTransformer;

/**
 * Mass ASM Transformer that re-routes all {@link GL11#glScissor(int, int, int, int)}
 * calls to the {@link GLScissorState} class.
 * This is needed as LWJGL cannot be trusted when it comes to obtaining the scissor box.
 *
 * @since 2.0.0
 */
public class GLTransformer extends ASMTransformer {

    @Override
    public boolean accept(@NotNull ClassNode node) {
        boolean modified = false;
        for (MethodNode method : node.methods) {
            if ((method.access & Opcodes.ACC_ABSTRACT) == 0) {
                AbstractInsnNode insn = method.instructions.getFirst();
                while (insn != null) {
                    if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
                        MethodInsnNode methodInsn = (MethodInsnNode) insn;
                        if (methodInsn.owner.equals("org/lwjgl/opengl/GL11") && methodInsn.name.equals("glScissor")) {
                            methodInsn.owner = "de/geolykt/starloader/impl/gui/GLScissorState";
                            modified = true;
                        }
                    }
                    insn = insn.getNext();
                }
            }
        }
        return modified;
    }

    @Override
    public boolean isValidTraget(@NotNull String internalName) {
        return !internalName.equals("de/geolykt/starloader/impl/gui/GLScissorState");
    }
}

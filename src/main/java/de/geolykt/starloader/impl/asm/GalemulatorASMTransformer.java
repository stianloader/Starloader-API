package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.geolykt.starloader.impl.gui.s2d.MenuHandler;
import de.geolykt.starloader.starplane.annotations.ReferenceSource;
import de.geolykt.starloader.starplane.annotations.RemapClassReference;
import de.geolykt.starloader.transformers.ASMTransformer;

import snoddasmannen.galimulator.Galemulator;

public class GalemulatorASMTransformer extends ASMTransformer {

    @NotNull
    @RemapClassReference(type = Galemulator.class)
    private static final String GALEMULATOR_CLASS = ReferenceSource.getStringValue();

    @NotNull
    @RemapClassReference(type = MenuHandler.class)
    private static final String MAIN_MENU_HANDLER_CLASS = ReferenceSource.getStringValue();

    private boolean transformed = false;

    @Override
    public boolean accept(@NotNull ClassNode node) {
        for (MethodNode method : node.methods) {
            if (!method.desc.equals("()V")) {
                if (method.name.equals("resize") && method.desc.equals("(II)V")) {
                    method.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, MAIN_MENU_HANDLER_CLASS, "resize", "(II)V"));
                    method.instructions.insert(new VarInsnNode(Opcodes.ILOAD, 2));
                    method.instructions.insert(new VarInsnNode(Opcodes.ILOAD, 1));
                }
                continue;
            }
            if (method.name.equals("dispose")) {
                method.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, MAIN_MENU_HANDLER_CLASS, "dispose", "()V"));
            } else if (method.name.equals("render")) {
                LabelNode label = new LabelNode();
                method.instructions.insert(label);
                method.instructions.insert(new InsnNode(Opcodes.RETURN));
                method.instructions.insert(new JumpInsnNode(Opcodes.IFEQ, label));
                method.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, MAIN_MENU_HANDLER_CLASS, "render", "()Z"));
            }
        }
        this.transformed = true;
        return true;
    }

    @Override
    public int getPriority() {
        return 90;
    }

    @Override
    public boolean isValid() {
        return !this.transformed;
    }

    @Override
    public boolean isValidTarget(@NotNull String internalName) {
        return internalName.equals(GALEMULATOR_CLASS);
    }
}

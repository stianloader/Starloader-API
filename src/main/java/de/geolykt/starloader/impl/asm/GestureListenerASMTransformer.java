package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.geolykt.starloader.impl.StarplaneReobfuscateReference;
import de.geolykt.starloader.transformers.ASMTransformer;

public class GestureListenerASMTransformer extends ASMTransformer {

    @StarplaneReobfuscateReference
    private static String containsPoint = "snoddasmannen/galimulator/ui/Widget.containsPoint(Lcom/badlogic/gdx/math/Vector2;)Z";

    @StarplaneReobfuscateReference
    private static String onMouseDown = "snoddasmannen/galimulator/ui/Widget.onMouseDown(FF)Z";

    @StarplaneReobfuscateReference
    private static String onMouseUp = "snoddasmannen/galimulator/ui/Widget.onMouseUp(DD)V";

    @StarplaneReobfuscateReference
    private static String target = "snoddasmannen/galimulator/GalimulatorGestureListener";

    private boolean valid = true;

    @Override
    public boolean accept(@NotNull ClassNode node) {
        if (!node.name.equals(target)) {
            return false;
        }
        String onMouseDownName = onMouseDown.split("[\\.\\(]", 3)[1];
        String widgetClass = onMouseDown.split("[\\.\\(]", 3)[0];
        String containsPointName = containsPoint.split("[\\.\\(]", 3)[1];
        String onMouseUpName = onMouseUp.split("[\\.\\(]", 3)[1];
        for (MethodNode method : node.methods) {
            // Conveniently the method names and descriptors cannot be obfuscated for inherited methods
            if (!method.name.equals("touchDown") || !method.desc.equals("(FFII)Z")) {
                continue;
            }
            AbstractInsnNode insn = method.instructions.getFirst();
            while (insn != null) {
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode minsn = (MethodInsnNode) insn;
                    if (minsn.owner.equals(widgetClass) && minsn.name.equals(onMouseDownName) && minsn.desc.equals("(FF)Z")) {
                        break;
                    }
                }
                insn = insn.getNext();
            }
            if (insn == null) {
                throw new IllegalStateException("Unable to find method!");
            }
            MethodInsnNode invokeOnMouseDown = (MethodInsnNode) insn;
            while (insn != null) {
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode minsn = (MethodInsnNode) insn;
                    if (minsn.owner.equals(widgetClass) && minsn.name.equals(containsPointName) && minsn.desc.equals("(Lcom/badlogic/gdx/math/Vector2;)Z")) {
                        break;
                    }
                }
                insn = insn.getPrevious();
            }
            if (insn == null) {
                throw new IllegalStateException("Unable to find method!");
            }
            MethodInsnNode invokeContainsPoint = (MethodInsnNode) insn;
            while (insn != null) {
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode minsn = (MethodInsnNode) insn;
                    if (minsn.owner.equals("java/util/concurrent/Semaphore") && minsn.name.equals("acquire") && minsn.desc.equals("(I)V")) {
                        break;
                    }
                }
                insn = insn.getNext();
            }
            if (insn == null) {
                throw new IllegalStateException("Unable to find method!");
            }
            MethodInsnNode invokeAcquire = (MethodInsnNode) insn;
            insn = invokeContainsPoint;
            while (insn.getOpcode() != Opcodes.IFEQ) {
                insn = insn.getNext();
            }
            JumpInsnNode jumpNotContaining = (JumpInsnNode) insn;
            insn = invokeAcquire.getNext();
            while (insn.getOpcode() == -1) {
                insn = insn.getNext();
            }
            VarInsnNode getWidget = (VarInsnNode) insn;

            LabelNode endInject = new LabelNode();
            InsnList inject = new InsnList();
            inject.add(new VarInsnNode(Opcodes.ALOAD, getWidget.var));
            inject.add(new TypeInsnNode(Opcodes.INSTANCEOF, "de/geolykt/starloader/impl/gui/AsyncWidgetInput"));
            inject.add(new JumpInsnNode(Opcodes.IFEQ, endInject));
            inject.add(new VarInsnNode(Opcodes.ALOAD, getWidget.var));
            inject.add(new TypeInsnNode(Opcodes.CHECKCAST, "de/geolykt/starloader/impl/gui/AsyncWidgetInput"));
            inject.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "de/geolykt/starloader/impl/gui/AsyncWidgetInput", "isAsyncClick", "()Z"));
            inject.add(new JumpInsnNode(Opcodes.IFEQ, endInject));
            insn = invokeAcquire.getNext();
            while (insn != invokeOnMouseDown) {
                if (insn.getOpcode() != -1) {
                    if (insn instanceof VarInsnNode) {
                        inject.add(new VarInsnNode(insn.getOpcode(), ((VarInsnNode) insn).var));
                    } else if (insn instanceof InsnNode) {
                        inject.add(new InsnNode(insn.getOpcode()));
                    } else if (insn instanceof FieldInsnNode) {
                        FieldInsnNode finsn = (FieldInsnNode) insn;
                        inject.add(new FieldInsnNode(finsn.getOpcode(), finsn.owner, finsn.name, finsn.desc));
                    } else if (insn instanceof MethodInsnNode) {
                        MethodInsnNode minsn = (MethodInsnNode) insn;
                        inject.add(new MethodInsnNode(minsn.getOpcode(), minsn.owner, minsn.name, minsn.desc));
                    } else {
                        throw new IllegalStateException("Unknown type: " + insn.getClass().toGenericString());
                    }
                }
                insn = insn.getNext();
            }
            inject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, widgetClass, onMouseDownName, "(FF)Z"));
            inject.add(new JumpInsnNode(Opcodes.IFEQ, jumpNotContaining.label));
            inject.add(new InsnNode(Opcodes.ICONST_1));
            inject.add(new InsnNode(Opcodes.IRETURN));
            inject.add(endInject);

            method.instructions.insert(jumpNotContaining, inject);
        }
        for (MethodNode method : node.methods) {
            // Conveniently the method names and descriptors cannot be obfuscated for inherited methods
            if (!method.name.equals("tap") || !method.desc.equals("(FFII)Z")) {
                continue;
            }
            AbstractInsnNode insn = method.instructions.getFirst();
            while (insn != null) {
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode minsn = (MethodInsnNode) insn;
                    if (minsn.owner.equals(widgetClass) && minsn.name.equals(onMouseUpName) && minsn.desc.equals("(DD)V")) {
                        break;
                    }
                }
                insn = insn.getNext();
            }
            if (insn == null) {
                throw new IllegalStateException("Unable to find method!");
            }
            MethodInsnNode invokeOnMouseUp = (MethodInsnNode) insn;
            while (insn != null) {
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode minsn = (MethodInsnNode) insn;
                    if (minsn.owner.equals(widgetClass) && minsn.name.equals(containsPointName) && minsn.desc.equals("(Lcom/badlogic/gdx/math/Vector2;)Z")) {
                        break;
                    }
                }
                insn = insn.getPrevious();
            }
            if (insn == null) {
                throw new IllegalStateException("Unable to find method!");
            }
            MethodInsnNode invokeContainsPoint = (MethodInsnNode) insn;
            while (insn != null) {
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode minsn = (MethodInsnNode) insn;
                    if (minsn.owner.equals("java/util/concurrent/Semaphore") && minsn.name.equals("acquire") && minsn.desc.equals("(I)V")) {
                        break;
                    }
                }
                insn = insn.getNext();
            }
            if (insn == null) {
                throw new IllegalStateException("Unable to find method!");
            }
            MethodInsnNode invokeAcquire = (MethodInsnNode) insn;
            while (insn != null) {
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode minsn = (MethodInsnNode) insn;
                    if (minsn.owner.equals("java/util/concurrent/Semaphore") && minsn.name.equals("release") && minsn.desc.equals("(I)V")) {
                        break;
                    }
                }
                insn = insn.getNext();
            }
            if (insn == null) {
                throw new IllegalStateException("Unable to find method!");
            }
            MethodInsnNode invokeRelease = (MethodInsnNode) insn;
            while (insn != null) {
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode minsn = (MethodInsnNode) insn;
                    if (minsn.owner.equals("java/util/concurrent/locks/ReentrantLock") && minsn.name.equals("unlock") && minsn.desc.equals("()V")) {
                        break;
                    }
                }
                insn = insn.getPrevious();
            }
            if (insn == null) {
                throw new IllegalStateException("Unable to find method!");
            }
            MethodInsnNode invokeUnlock = (MethodInsnNode) insn;
            insn = invokeOnMouseUp.getNext();
            while (insn.getOpcode() == -1) {
                insn = insn.getNext();
            }
            VarInsnNode getWidget = (VarInsnNode) insn;
            if (!((MethodInsnNode) getWidget.getNext()).owner.equals(widgetClass)) {
                throw new IllegalStateException("Invalid owner");
            }
            insn = invokeContainsPoint;
            while (insn.getOpcode() != Opcodes.IFEQ) {
                insn = insn.getNext();
            }
            JumpInsnNode jumpNotContaining = (JumpInsnNode) insn;

            LabelNode skipA = new LabelNode();
            LabelNode skipB = new LabelNode();
            LabelNode pushF = new LabelNode();
            InsnList injectA = new InsnList();
            injectA.add(new VarInsnNode(Opcodes.ALOAD, getWidget.var));
            injectA.add(new TypeInsnNode(Opcodes.INSTANCEOF, "de/geolykt/starloader/impl/gui/AsyncWidgetInput"));
            injectA.add(new JumpInsnNode(Opcodes.IFEQ, pushF));
            injectA.add(new VarInsnNode(Opcodes.ALOAD, getWidget.var));
            injectA.add(new TypeInsnNode(Opcodes.CHECKCAST, "de/geolykt/starloader/impl/gui/AsyncWidgetInput"));
            injectA.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "de/geolykt/starloader/impl/gui/AsyncWidgetInput", "isAsyncClick", "()Z"));
            injectA.add(new JumpInsnNode(Opcodes.IFEQ, pushF));
            injectA.add(new InsnNode(Opcodes.ICONST_1));
            injectA.add(new JumpInsnNode(Opcodes.GOTO, skipA));
            injectA.add(pushF);
            injectA.add(new InsnNode(Opcodes.ICONST_0));

            method.instructions.insert(jumpNotContaining, injectA);
            method.instructions.insert(invokeAcquire, skipA);
            method.instructions.insert(invokeUnlock, new JumpInsnNode(Opcodes.IFNE, skipB));
            method.instructions.insert(invokeRelease, skipB);
        }
        return true;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean isValidTarget(@NotNull String internalName) {
        return target.equals(internalName);
    }
}

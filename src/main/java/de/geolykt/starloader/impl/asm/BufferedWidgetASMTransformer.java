package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.geolykt.starloader.impl.StarplaneReobfuscateReference;
import de.geolykt.starloader.transformers.ASMTransformer;

public class BufferedWidgetASMTransformer extends ASMTransformer {

    @StarplaneReobfuscateReference
    private static String child = "snoddasmannen/galimulator/ui/BufferedWidgetWrapper.a Lsnoddasmannen/galimulator/ui/Widget;";

    @StarplaneReobfuscateReference
    private static String target = "snoddasmannen/galimulator/ui/BufferedWidgetWrapper";

    private boolean valid = true;

    @Override
    public boolean accept(@NotNull ClassNode node) {
        if (!node.name.equals(target)) {
            return false;
        }
        String[] childSplits = child.split("[ \\.]", 3);
        LabelNode l1 = new LabelNode();
        node.interfaces.add("de/geolykt/starloader/impl/gui/AsyncWidgetInput");
        node.interfaces.add("de/geolykt/starloader/impl/gui/WidgetMouseReleaseListener");
        MethodNode injected = new MethodNode(Opcodes.ACC_PUBLIC, "isAsyncClick", "()Z", null, null);
        injected.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // ALOAD THIS
        injected.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, childSplits[0], childSplits[1], childSplits[2]));
        injected.instructions.add(new TypeInsnNode(Opcodes.INSTANCEOF, "de/geolykt/starloader/impl/gui/AsyncWidgetInput"));
        injected.instructions.add(new JumpInsnNode(Opcodes.IFEQ, l1));
        injected.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // ALOAD THIS
        injected.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, childSplits[0], childSplits[1], childSplits[2]));
        injected.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "de/geolykt/starloader/impl/gui/AsyncWidgetInput", "isAsyncClick", "()Z"));
        injected.instructions.add(new InsnNode(Opcodes.IRETURN));
        injected.instructions.add(l1);
        injected.instructions.add(new InsnNode(Opcodes.ICONST_0));
        injected.instructions.add(new InsnNode(Opcodes.IRETURN));
        node.methods.add(injected);

        l1 = new LabelNode();
        injected = new MethodNode(Opcodes.ACC_PUBLIC, "onMouseUp", "(DD)V", null, null);
        injected.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // ALOAD THIS
        injected.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, childSplits[0], childSplits[1], childSplits[2]));
        injected.instructions.add(new TypeInsnNode(Opcodes.INSTANCEOF, "de/geolykt/starloader/impl/gui/WidgetMouseReleaseListener"));
        injected.instructions.add(new JumpInsnNode(Opcodes.IFEQ, l1));
        injected.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // ALOAD THIS
        injected.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, childSplits[0], childSplits[1], childSplits[2]));
        injected.instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, "de/geolykt/starloader/impl/gui/WidgetMouseReleaseListener"));
        injected.instructions.add(new VarInsnNode(Opcodes.DLOAD, 1)); // DLOAD X
        injected.instructions.add(new VarInsnNode(Opcodes.DLOAD, 3)); // DLOAD Y
        injected.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // ALOAD THIS
        injected.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, childSplits[0], childSplits[1], childSplits[2]));
        injected.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "snoddasmannen/galimulator/ui/Widget", "t", "F"));
        injected.instructions.add(new InsnNode(Opcodes.F2D));
        injected.instructions.add(new InsnNode(Opcodes.DSUB));
        injected.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "de/geolykt/starloader/impl/gui/WidgetMouseReleaseListener", "onMouseUp", "(DD)V"));
        injected.instructions.add(l1);
        injected.instructions.add(new InsnNode(Opcodes.RETURN));
        node.methods.add(injected);
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

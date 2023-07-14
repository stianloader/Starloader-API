package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.gui.SidebarInjector;
import de.geolykt.starloader.api.gui.SidebarInjector.Orientation;
import de.geolykt.starloader.api.gui.rendercache.RenderObject;
import de.geolykt.starloader.impl.SLSidebarInjector;
import de.geolykt.starloader.starplane.annotations.MethodDesc;
import de.geolykt.starloader.starplane.annotations.ReferenceSource;
import de.geolykt.starloader.starplane.annotations.RemapClassReference;
import de.geolykt.starloader.starplane.annotations.RemapMemberReference;
import de.geolykt.starloader.starplane.annotations.RemapMemberReference.ReferenceFormat;
import de.geolykt.starloader.transformers.ASMTransformer;

import snoddasmannen.galimulator.rendersystem.RenderItem;
import snoddasmannen.galimulator.ui.AboutWidget;
import snoddasmannen.galimulator.ui.SidebarWidget;
import snoddasmannen.galimulator.ui.Widget;

/**
 * Code transformer that applies to the UI package and various other UI-related calls.
 */
public final class UIASMTransformer extends ASMTransformer {

    @RemapClassReference(type = AboutWidget.class)
    @NotNull
    private static final String ABOUT_WIDGET_CLASS = ReferenceSource.getStringValue();

    @RemapMemberReference(ownerType = Widget.class, name = "addChild", methodDesc = @MethodDesc(args = Widget.class, ret = Widget.class), format = ReferenceFormat.NAME)
    private static final String ADD_CHILD_NAME = ReferenceSource.getStringValue();

    @RemapMemberReference(ownerType = Widget.class, name = "addChild", methodDesc = @MethodDesc(args = Widget.class, ret = Widget.class), format = ReferenceFormat.DESCRIPTOR)
    private static final String ADD_CHILD_DESC = ReferenceSource.getStringValue();

    private static final Logger LOGGER = LoggerFactory.getLogger(UIASMTransformer.class);

    /**
     * The bytecode name of the class you are currently reading.
     */
    @NotNull
    public static final String THIS_CLASS = "de/geolykt/starloader/impl/asm/UIASMTransformer";

    /**
     * The fully classified name of the class that is responsible for
     * the sidebar. Uses the saner ASM-style syntax.
     *
     * @since 2.0.0
     */
    @RemapClassReference(type = SidebarWidget.class)
    @NotNull
    public static final String MAIN_SIDEBAR_CLASS = ReferenceSource.getStringValue();

    @RemapMemberReference(ownerType = SidebarWidget.class, name = "openGameControl", methodDesc = @MethodDesc(args = {}, ret = void.class), format = ReferenceFormat.COMBINED_LEGACY)
    @NotNull
    public static final String OPEN_GAME_CONTROL_METHOD = ReferenceSource.getStringValue();

    /**
     * The fully classified name of the RenderItem class.
     * Remapped by starplane - contents of this string will vary in built jar.
     *
     * @since 2.0.0
     */
    @RemapClassReference(type = RenderItem.class)
    @NotNull
    public static final String RENDER_ITEM_CLASS = ReferenceSource.getStringValue();

    public static final void sideBarBottom(Object widget) {
        if (widget instanceof Widget && SidebarInjector.getImplementation() instanceof SLSidebarInjector) {
            ((SLSidebarInjector) SidebarInjector.getImplementation()).addAll(Orientation.BOTTOM, (Widget) widget);
        }
    }

    public static final void sideBarTop(Object widget) {
        if (widget instanceof Widget && SidebarInjector.getImplementation() instanceof SLSidebarInjector) {
            ((SLSidebarInjector) SidebarInjector.getImplementation()).addAll(Orientation.TOP, (Widget) widget);
        }
    }

    @Override
    public boolean accept(@NotNull ClassNode node) {
        if (node.name.equals(MAIN_SIDEBAR_CLASS)) {
            transformSidebarClass(node);
            return true;
        } else if (node.name.equals(RENDER_ITEM_CLASS)) {
            node.interfaces.add(Type.getInternalName(RenderObject.class));
            return true;
        } else if (node.name.equals(ABOUT_WIDGET_CLASS)) {
            transformAboutClass(node);
            return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return -9_900;
    }

    @Override
    public boolean isValidTarget(@NotNull String internalName) {
        return internalName.equals(MAIN_SIDEBAR_CLASS)
                || internalName.equals(RENDER_ITEM_CLASS)
                || internalName.equals(ABOUT_WIDGET_CLASS);
    }

    private void sidebarInjectHelper(@NotNull String flag, @NotNull String methodName, @NotNull ClassNode source) {
        MethodNode initializerMethod = null;
        LdcInsnNode flagInsn = null;
        for (MethodNode method : source.methods) {
            for (AbstractInsnNode instruction : method.instructions) {
                if (instruction instanceof LdcInsnNode) {
                    LdcInsnNode ldcInsnNode = (LdcInsnNode) instruction;
                    if (ldcInsnNode.cst.equals(flag)) {
                        if (initializerMethod != null) {
                            LOGGER.warn("Found multiple injection candidates. Unexpected results WILL arise");
                        }
                        initializerMethod = method;
                        flagInsn = ldcInsnNode;
                        break;
                    }
                }
            }
        }
        if (initializerMethod == null || flagInsn == null) {
            LOGGER.error("UI transformer failed to find potential injection candidate in sidebar class " + MAIN_SIDEBAR_CLASS);
            return; // We are out of here
        }
        AbstractInsnNode currentInstruction = flagInsn.getNext();
        boolean popNode = false;
        boolean aloadThis1 = false;
        while (currentInstruction != null) {
            if (!popNode) {
                if (currentInstruction instanceof InsnNode && currentInstruction.getOpcode() == Opcodes.POP) {
                    popNode = true;
                }
            } else {
                // we have to trust our intuition
                if (currentInstruction instanceof VarInsnNode && currentInstruction.getOpcode() == Opcodes.ALOAD) {
                    if (aloadThis1) {
                        VarInsnNode aloadCall = new VarInsnNode(Opcodes.ALOAD, 0); // 0 should be the `this` reference
                        MethodInsnNode methodCall = new MethodInsnNode(Opcodes.INVOKESTATIC, THIS_CLASS, methodName, "(Ljava/lang/Object;)V");
                        initializerMethod.instructions.insertBefore(currentInstruction, aloadCall);
                        initializerMethod.instructions.insert(aloadCall, methodCall);
                        break;
                    } else {
                        aloadThis1 = true;
                    }
                }
            }
            currentInstruction = currentInstruction.getNext();
        }
    }

    private void transformAboutClass(@NotNull ClassNode source) {
        MethodNode constructor = null;
        for (MethodNode method : source.methods) {
            if (method.name.equals("<init>")) {
                if (constructor != null) {
                    LOGGER.warn("Multiple constructors detected for class " + ABOUT_WIDGET_CLASS + ". Transformer will not apply");
                    return;
                }
                constructor = method;
            }
        }
        if (constructor == null) {
            LOGGER.warn("No constructor detected for class " + ABOUT_WIDGET_CLASS + ". Transformer will not apply");
            return;
        }
        MethodInsnNode getShortcutsInsn = null;
        for (AbstractInsnNode insn = constructor.instructions.getFirst(); insn != null; insn = insn.getNext()) {
            if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                if (methodInsn.name.equals("getShortcuts")) {
                    getShortcutsInsn = methodInsn;
                    break;
                }
            }
        }
        MethodInsnNode addChildInsn = null;
        for (AbstractInsnNode insn = getShortcutsInsn; insn != null; insn = insn.getNext()) {
            if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                if (methodInsn.name.equals(ADD_CHILD_NAME) && methodInsn.desc.equals(ADD_CHILD_DESC)) {
                    addChildInsn = methodInsn;
                    break;
                }
            }
        }
        LineNumberNode cutFrom = null;
        for (AbstractInsnNode insn = getShortcutsInsn; insn != null; insn = insn.getPrevious()) {
            if (insn instanceof LineNumberNode) {
                cutFrom = (LineNumberNode) insn;
                break;
            }
        }
        LineNumberNode cutTo = null;
        for (AbstractInsnNode insn = addChildInsn; insn != null; insn = insn.getNext()) {
            if (insn instanceof LineNumberNode) {
                cutTo = (LineNumberNode) insn;
                break;
            }
        }
        if (cutFrom == null) {
            LOGGER.warn("Unable to determine cutFrom instruction for class " + ABOUT_WIDGET_CLASS + ". Transformer will not apply. getShortcutsInsn: {}", getShortcutsInsn);
            return;
        }
        if (cutTo == null) {
            LOGGER.warn("Unable to determine cutTo instruction for class " + ABOUT_WIDGET_CLASS + ". Transformer will not apply. addChildInsn: {}; {}:{}", addChildInsn, ADD_CHILD_NAME, ADD_CHILD_DESC);
            return;
        }
        AbstractInsnNode cutInsn = cutFrom.getNext();
        while (cutInsn != cutTo) {
            AbstractInsnNode t = cutInsn.getNext();
            constructor.instructions.remove(cutInsn);
            cutInsn = t;
        }
        constructor.instructions.insertBefore(cutTo, new VarInsnNode(Opcodes.ALOAD, 0));
        constructor.instructions.insertBefore(cutTo, new MethodInsnNode(Opcodes.INVOKESTATIC, "de/geolykt/starloader/impl/asm/TransformCallbacks", "about$shortcutListReplace", "(L" + ABOUT_WIDGET_CLASS + ";)V"));
    }

    public void transformSidebarClass(@NotNull ClassNode source) {
        sidebarInjectHelper("FileButton.png", "sideBarTop", source);
        sidebarInjectHelper("peoplebutton.png", "sideBarBottom", source);

        String openGameControlMethodName = OPEN_GAME_CONTROL_METHOD.split("[\\.\\(]", 3)[1];
        for (MethodNode method : source.methods) {
            if (method.desc.equals("()V") && method.name.equals(openGameControlMethodName) && (method.access & Opcodes.ACC_STATIC) != 0) {
                method.instructions.clear();
                method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "de/geolykt/starloader/api/gui/openui/UIControl", "openGameControlMenu", "()V"));
                method.instructions.add(new InsnNode(Opcodes.RETURN));
            }
        }
    }
}

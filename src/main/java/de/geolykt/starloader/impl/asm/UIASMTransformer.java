package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.gui.SidebarInjector;
import de.geolykt.starloader.api.gui.SidebarInjector.Orientation;
import de.geolykt.starloader.api.gui.rendercache.RenderObject;
import de.geolykt.starloader.impl.SLSidebarInjector;
import de.geolykt.starloader.impl.StarplaneReobfuscateReference;
import de.geolykt.starloader.transformers.ASMTransformer;

import snoddasmannen.galimulator.ui.Widget;

/**
 * Code transformer that applies to the UI package and various other UI-related calls.
 */
public final class UIASMTransformer extends ASMTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UIASMTransformer.class);

    /**
     * The bytecode name of the class you are currently reading.
     */
    @NotNull
    public static final String THIS_CLASS = "de/geolykt/starloader/impl/asm/UIASMTransformer";

    /**
     * The fully classified name of the class that is responsible for
     * the sidebar. Uses the saner ASM-style syntax.
     */
    @StarplaneReobfuscateReference
    @NotNull
    public static String mainSidebarClass = "snoddasmannen/galimulator/ui/SidebarWidget";

    @StarplaneReobfuscateReference
    @NotNull
    public static String openGameControlMethod = "snoddasmannen/galimulator/ui/SidebarWidget.openGameControl()V";

    /**
     * The fully classified name of the RenderItem class.
     * Remapped by starplane - contents of this string will vary in built jar.
     *
     * @since 2.0.0
     */
    @StarplaneReobfuscateReference
    @NotNull
    public static String renderItemClass = "snoddasmannen/galimulator/rendersystem/RenderItem";

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
        if (node.name.equals(mainSidebarClass)) {
            transformSidebarClass(node);
            return true;
        } else if (node.name.equals(renderItemClass)) {
            node.interfaces.add(Type.getInternalName(RenderObject.class));
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidTraget(@NotNull String internalName) {
        // TODO does this work?
        return internalName.equals(mainSidebarClass) || internalName.equals(renderItemClass);
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
            LOGGER.error("UI transformer failed to find potential injection candidate in sidebar class " + mainSidebarClass);
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

    public void transformSidebarClass(@NotNull ClassNode source) {
        sidebarInjectHelper("FileButton.png", "sideBarTop", source);
        sidebarInjectHelper("peoplebutton.png", "sideBarBottom", source);

        String openGameControlMethodName = openGameControlMethod.split("[\\.\\(]", 3)[1];
        for (MethodNode method : source.methods) {
            if (method.desc.equals("()V") && method.name.equals(openGameControlMethodName) && (method.access & Opcodes.ACC_STATIC) != 0) {
                method.instructions.clear();
                method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "de/geolykt/starloader/api/gui/openui/UIControl", "openGameControlMenu", "()V"));
                method.instructions.add(new InsnNode(Opcodes.RETURN));
            }
        }
    }
}

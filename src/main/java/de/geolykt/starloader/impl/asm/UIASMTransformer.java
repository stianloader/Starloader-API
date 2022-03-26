package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minestom.server.extras.selfmodification.CodeModifier;

import de.geolykt.starloader.api.gui.SidebarInjector;
import de.geolykt.starloader.api.gui.SidebarInjector.Orientation;
import de.geolykt.starloader.impl.SLSidebarInjector;
import de.geolykt.starloader.impl.StarplaneReobfuscateReference;

import snoddasmannen.galimulator.ui.Widget;

/**
 * Code transformer that applies to the UI package.
 */
public final class UIASMTransformer extends CodeModifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(UIASMTransformer.class);

    /**
     * The fully classified name of the class that is responsible for
     * the sidebar. Uses the saner ASM-style syntax.
     */
    @StarplaneReobfuscateReference
    @NotNull
    public final String mainSidebarClass = "snoddasmannen/galimulator/ui/SidebarWidget";

    /**
     * The bytecode name of the class you are currently reading.
     */
    @NotNull
    public static final String THIS_CLASS = "de/geolykt/starloader/impl/asm/UIASMTransformer";

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
    public @Nullable String getNamespace() {
        return "snoddasmannen.galimulator.ui";
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
                // we have to trust our intuitin
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

    @Override
    public boolean transform(ClassNode source) {
        if (source == null) {
            throw new NullPointerException(); // As you can see, the API was designed brilliantly
        }
        if (source.name.equals(mainSidebarClass)) {
            transformSidebarClass(source);
            return true;
        }
        return false;
    }

    public void transformSidebarClass(@NotNull ClassNode source) {
        sidebarInjectHelper("FileButton.png", "sideBarTop", source);
        sidebarInjectHelper("peoplebutton.png", "sideBarBottom", source);
        /*ClassWriter cw = new ClassWriter(0);
        source.accept(cw);
        try (FileOutputStream fos = new FileOutputStream(new File("sidebar.class"))) {
            fos.write(cw.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}

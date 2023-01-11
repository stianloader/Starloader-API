package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.geolykt.starloader.api.actor.StateActorFactory;
import de.geolykt.starloader.transformers.ASMTransformer;

/**
 * ASM Transformer that targets the StateActorCreator interface to make it extend {@link StateActorFactory}.
 *
 * @since 2.0.0
 */
public class StateActorCreatorTransformer extends ASMTransformer {

    @NotNull
    private static final String GALIM_STAR_NAME = "snoddasmannen/galimulator/Star";

    @NotNull
    private static final String GALIM_STATE_ACTOR_NAME = "snoddasmannen/galimulator/actors/StateActor";

    @NotNull
    public static String galimActorField = "snoddasmannen/galimulator/Space.actors Ljava/util/Vector;";

    @NotNull
    private static final String SLAPI_NAMESPACED_KEY_NAME = "de/geolykt/starloader/api/NamespacedKey";

    @NotNull
    private static final String SLAPI_STAR_NAME = "de/geolykt/starloader/api/empire/Star";

    @NotNull
    private static final String SLAPI_STATE_ACTOR_NAME = "de/geolykt/starloader/api/actor/StateActor";

    @NotNull
    private static final String STATE_ACTOR_FACTORY_NAME = "de/geolykt/starloader/api/actor/StateActorFactory";

    @NotNull
    private static final String TARGET_CLASS = "snoddasmannen/galimulator/actors/StateActorCreator";

    private boolean valid = true;

    @Override
    public boolean accept(@NotNull ClassNode node) {
        if (!node.name.equals(TARGET_CLASS)) {
            return false;
        }
        node.interfaces.add(STATE_ACTOR_FACTORY_NAME);
        MethodNode getTypeName = new MethodNode(Opcodes.ACC_PUBLIC, "getTypeName", "()Ljava/lang/String;", null, null);
        getTypeName.instructions = new InsnList();
        getTypeName.instructions.add(new LabelNode());
        getTypeName.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getTypeName.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, TARGET_CLASS, "getShipName", "()Ljava/lang/String;"));
        getTypeName.instructions.add(new InsnNode(Opcodes.ARETURN));
        MethodNode spawnActorMethod = new MethodNode(Opcodes.ACC_PUBLIC, "spawnActor", "(L" + SLAPI_STAR_NAME + ";)L" + SLAPI_STATE_ACTOR_NAME + ";", null, null);
        spawnActorMethod.instructions = new InsnList();
        spawnActorMethod.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "snoddasmannen/galimulator/Space", galimActorField.split("[ \\.]")[1], "Ljava/util/Vector;"));
        spawnActorMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // ALOAD THIS
        spawnActorMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1)); // ALOAD STAR
        spawnActorMethod.instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, GALIM_STAR_NAME)); // Required because java
        spawnActorMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // ALOAD THIS
        spawnActorMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, TARGET_CLASS, "isNative", "()Z"));
        spawnActorMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, TARGET_CLASS, "createShip", "(L" + GALIM_STAR_NAME + ";Z)L" + GALIM_STATE_ACTOR_NAME + ";"));
        spawnActorMethod.instructions.add(new InsnNode(Opcodes.DUP_X1));
        spawnActorMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Vector", "add", "(Ljava/lang/Object;)Z"));
        spawnActorMethod.instructions.add(new InsnNode(Opcodes.POP));
        spawnActorMethod.instructions.add(new InsnNode(Opcodes.ARETURN));
        node.methods.add(getTypeName);
        node.methods.add(spawnActorMethod);

        {
            MethodNode setKey = new MethodNode(Opcodes.ACC_PUBLIC, "setRegistryKey", "(L" + SLAPI_NAMESPACED_KEY_NAME + ";)V", null, null);
            setKey.maxStack = 2;
            setKey.instructions = new InsnList();
            setKey.instructions.add(new TypeInsnNode(Opcodes.NEW, Type.getInternalName(UnsupportedOperationException.class)));
            setKey.instructions.add(new InsnNode(Opcodes.DUP));
            setKey.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, Type.getInternalName(UnsupportedOperationException.class), "<init>", "()V"));
            setKey.instructions.add(new InsnNode(Opcodes.ATHROW));
            node.methods.add(setKey);
        }
        {
            MethodNode getKey = new MethodNode(Opcodes.ACC_PUBLIC, "getRegistryKey", "()L" + SLAPI_NAMESPACED_KEY_NAME + ";", null, null);
            getKey.maxStack = 1;
            getKey.instructions = new InsnList();
            getKey.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            getKey.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "de/geolykt/starloader/impl/registry/StateActorFactoryRegistry", "getKey", "(L" + STATE_ACTOR_FACTORY_NAME + ";)L" + SLAPI_NAMESPACED_KEY_NAME + ";"));
            getKey.instructions.add(new InsnNode(Opcodes.ARETURN));
            node.methods.add(getKey);
        }

        valid = false;
        return true;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean isValidTarget(@NotNull String internalName) {
        return internalName.equals(TARGET_CLASS);
    }
}

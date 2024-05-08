package de.geolykt.starloader.impl.asm;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.gui.modconf.ConfigurationOption;
import de.geolykt.starloader.api.gui.modconf.FloatOption;
import de.geolykt.starloader.api.gui.modconf.IntegerOption;
import de.geolykt.starloader.api.utils.FloatConsumer;
import de.geolykt.starloader.starplane.annotations.ReferenceSource;
import de.geolykt.starloader.starplane.annotations.RemapClassReference;
import de.geolykt.starloader.transformers.ASMTransformer;

/**
 * Transformer whose sole purpose it is to keep the ABI (Application binary interface) stable across releases of SLAPI where
 * possible and appropriate.
 *
 * <p>Note that ABI is different to API. ABI refers to the compiled programs. This transformer cannot mitigate any API breakage
 * that results in a compilation failure; instead, it tries to mitigate the runtime effects, allowing older mods to still run.
 *
 * <p>Like most other SLAPI transformers, the priority of this transformer is at a low -9900.
 *
 * @since 2.0.0
 */
public class ABICompatibilityTransformer extends ASMTransformer {

    @RemapClassReference(type = FloatOption.class)
    private static final String CONF_OPTION_FLOAT = ReferenceSource.getStringValue();

    @RemapClassReference(type = ConfigurationOption.class)
    private static final String CONF_OPTION_GENERIC = ReferenceSource.getStringValue();

    @RemapClassReference(type = IntegerOption.class)
    private static final String CONF_OPTION_INT = ReferenceSource.getStringValue();

    @RemapClassReference(type = FloatConsumer.class)
    private static final String FLOAT_CONSUMER = ReferenceSource.getStringValue();

    @RemapClassReference(type = TransformCallbacks.class)
    private static final String TRANSFORM_CALLBACK = ReferenceSource.getStringValue();

    private static void removeMethod(String name, String desc, List<MethodNode> methods) {
        Iterator<MethodNode> it = methods.iterator();
        while (it.hasNext()) {
            MethodNode m = it.next();
            if (m.name.equals(name) && m.desc.equals(desc)) {
                it.remove();
                return;
            }
        }
        LoggerFactory.getLogger(ABICompatibilityTransformer.class).warn("Unable to remove method {}:{} from {}", name, desc,
                methods.stream().map(m -> m.name + ":" + m.desc).collect(Collectors.toList()));
    }

    @Override
    public boolean accept(@NotNull ClassNode node) {
        if (node.name.equals(CONF_OPTION_GENERIC)) {
            MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, "addValueChangeListener", "(Ljava/util/function/Consumer;)V", null, null);
            method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, TRANSFORM_CALLBACK, "abi$raiseABIError", "(Ljava/lang/Object;)V"));
            method.instructions.add(new InsnNode(Opcodes.RETURN));
            removeMethod(method.name, method.desc, node.methods);
            node.methods.add(method);
            return true;
        } else if (node.name.equals(CONF_OPTION_INT)) {
            MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, "addValueChangeListener", "(Ljava/util/function/IntConsumer;)V", null, null);
            method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, TRANSFORM_CALLBACK, "abi$raiseABIError", "(Ljava/lang/Object;)V"));
            method.instructions.add(new InsnNode(Opcodes.RETURN));
            removeMethod(method.name, method.desc, node.methods);
            node.methods.add(method);
            return true;
        } else if (node.name.equals(CONF_OPTION_FLOAT)) {
            MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, "addValueChangeListener", "(L" + FLOAT_CONSUMER + ";)V", null, null);
            method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, TRANSFORM_CALLBACK, "abi$raiseABIError", "(Ljava/lang/Object;)V"));
            method.instructions.add(new InsnNode(Opcodes.RETURN));
            removeMethod(method.name, method.desc, node.methods);
            node.methods.add(method);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return -9_900;
    }

    @Override
    public boolean isValidTarget(@NotNull String internalName) {
        return CONF_OPTION_GENERIC.equals(internalName)
                || CONF_OPTION_INT.equals(internalName)
                || CONF_OPTION_FLOAT.equals(internalName);
    }
}

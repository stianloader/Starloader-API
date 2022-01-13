package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import net.minestom.server.extras.selfmodification.CodeModifier;

/**
 * A temporary solution to the fact that "snoddasmannen/galimulator/EmpireSpecial" is denumerised by AW
 * but dearly needs the enum flag at runtime.
 *
 * <p>This class will be removed as soon as ObfTools allows to make compile-time specific acess wideners.
 */
public class RuntimeReenumeriser extends CodeModifier {

    @Override
    public boolean transform(ClassNode source) {
        if (source.name.equals("snoddasmannen/galimulator/EmpireSpecial")) {
            source.access |= Opcodes.ACC_ENUM;
            return true;
        }
        return false;
    }

    @Override
    @Nullable
    public String getNamespace() {
        return "snoddasmannen.galimulator";
    }
}

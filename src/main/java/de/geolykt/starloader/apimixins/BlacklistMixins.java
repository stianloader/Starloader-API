package de.geolykt.starloader.apimixins;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;

import snoddasmannen.galimulator.Blacklist;

@SuppressWarnings("serial")
@Mixin(Blacklist.class)
public abstract class BlacklistMixins extends ArrayList<Object> {
    @Redirect(
        method = "a(Ljava/lang/Object;)Z",
        at = @At(value = "INVOKE", desc = @Desc(owner = Blacklist.class, value = "contains", args = Object.class, ret = boolean.class)),
        allow = 1,
        require = 1
    )
    private boolean slapi$onContains(Blacklist reciever, Object arg0) {
        // This exists for mappings such as bStarmap that remap a(Object) to contains(Object).
        // However, since the #contains call is an INVOKEVIRTUAL instruction, this causes issues
        // when using stianloader-remapper. To fix this we change this to an INVOKESPECIAL instruction.
        return super.contains(arg0);
    }
}

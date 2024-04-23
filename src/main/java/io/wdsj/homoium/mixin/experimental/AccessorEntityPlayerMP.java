package io.wdsj.homoium.mixin.experimental;

import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayerMP.class)
public interface AccessorEntityPlayerMP {

    @Accessor("managedPosX")
    double getManagedPosX();

    @Accessor("managedPosZ")
    double getManagedPosZ();
}

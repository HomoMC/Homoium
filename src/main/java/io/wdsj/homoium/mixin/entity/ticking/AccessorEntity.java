package io.wdsj.homoium.mixin.entity.ticking;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface AccessorEntity {
    @Accessor
    int getTicksExisted();
}

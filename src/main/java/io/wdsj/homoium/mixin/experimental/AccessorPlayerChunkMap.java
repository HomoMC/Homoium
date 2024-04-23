package io.wdsj.homoium.mixin.experimental;

import net.minecraft.server.management.PlayerChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerChunkMap.class)
public interface AccessorPlayerChunkMap {
    @Accessor("playerViewRadius")
    int getPlayerViewRadius();
}

package io.wdsj.homoium.mixin.experimental;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IPlayerChunkMapMethods {
    void updateViewDistance(EntityPlayerMP player, int distanceIn);

    void setViewDistance(EntityPlayerMP entityplayer, int i, boolean markSort);
}

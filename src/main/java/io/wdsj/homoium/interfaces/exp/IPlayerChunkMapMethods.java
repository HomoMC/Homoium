package io.wdsj.homoium.interfaces.exp;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IPlayerChunkMapMethods {
    void homoium_RFG$updateViewDistance(EntityPlayerMP player, int distanceIn);

    void setViewDistance(EntityPlayerMP entityplayer, int i, boolean markSort);
}

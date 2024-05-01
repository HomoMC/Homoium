package io.wdsj.homoium.mixin.experimental;

import io.wdsj.homoium.interfaces.exp.IEntityPlayerMPMethods;
import io.wdsj.homoium.interfaces.exp.IPlayerChunkMapMethods;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(PlayerChunkMap.class)
public abstract class MixinPlayerChunkMap implements IPlayerChunkMapMethods {

    @Shadow @Final private WorldServer world;

    @Shadow public abstract void setPlayerViewRadius(int radius);


    @Shadow private int playerViewRadius;

    @Shadow protected abstract boolean overlaps(int x1, int z1, int x2, int z2, int radius);

    @Shadow protected abstract PlayerChunkMapEntry getOrCreateEntry(int chunkX, int chunkZ);

    @Shadow @Nullable public abstract PlayerChunkMapEntry getEntry(int x, int z);

    @Shadow protected abstract void markSortPending();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstruct(WorldServer worldIn, CallbackInfo ci) {
        this.setPlayerViewRadius(3);
    }


    @Override
    public void homoium_RFG$updateViewDistance(EntityPlayerMP player, int distanceIn) {
        final int oldViewDistance = ((IEntityPlayerMPMethods)player).homoium_RFG$getViewDistance();

        // This represents the view distance that we will set on the player
        // It can exist as a negative value
        int playerViewDistance = MathHelper.clamp(distanceIn, 3, 64);

        // This value is the one we actually use to update the chunk map
        // We don't ever want this to be a negative
        int toSet = playerViewDistance;

        if (distanceIn < 0) {
            playerViewDistance = -1;
            toSet = ((AccessorPlayerChunkMap)world.getPlayerChunkMap()).getPlayerViewRadius();
        }

        if (toSet != oldViewDistance) {
            // Order matters
            this.setViewDistance(player, toSet, true);
            ((IEntityPlayerMPMethods)player).homoium_RFG$setViewDistance(playerViewDistance);
        }
    }

    @Override
    public void setViewDistance(EntityPlayerMP entityplayer, int i, boolean markSort) {
        i = MathHelper.clamp(i, 3, 64);
        int oldViewDistance = ((IEntityPlayerMPMethods)entityplayer).homoium_RFG$getViewDistance();
        if (i != oldViewDistance) {
            int j = i - oldViewDistance;
            int k = (int) ((AccessorEntityPlayerMP)entityplayer).getManagedPosX() >> 4;
            int l = (int) ((AccessorEntityPlayerMP)entityplayer).getManagedPosZ() >> 4;
            int i1;
            int j1;
            if (j > 0) {
                for (i1 = k - i; i1 <= k + i; ++i1) {
                    for (j1 = l - i; j1 <= l + i; ++j1) {
                        PlayerChunkMapEntry playerchunk = this.getOrCreateEntry(i1, j1);

                        if (!playerchunk.containsPlayer(entityplayer)) {
                            playerchunk.addPlayer(entityplayer);
                        }
                    }
                }
            } else {
                for (i1 = k - oldViewDistance; i1 <= k + oldViewDistance; ++i1) {
                    for (j1 = l - oldViewDistance; j1 <= l + oldViewDistance; ++j1) {
                        if (!this.overlaps(i1, j1, k, l, i)) {
                            PlayerChunkMapEntry chunk = this.getEntry(i1, j1);
                            if (chunk != null) chunk.removePlayer(entityplayer); // Fix memory leak
                        }
                    }
                }
                if (markSort) {
                    this.markSortPending();
                }
            }
        }
    }


}

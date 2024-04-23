package io.wdsj.homoium.mixin.experimental;

import io.wdsj.homoium.config.Settings;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(PlayerChunkMap.class)
public abstract class MixinPlayerChunkMap implements IPlayerChunkMapMethods{

    @Shadow @Final private WorldServer world;

    @Shadow public abstract void setPlayerViewRadius(int radius);


    @Shadow private int playerViewRadius;

    @Shadow protected abstract boolean overlaps(int x1, int z1, int x2, int z2, int radius);

    @Shadow protected abstract PlayerChunkMapEntry getOrCreateEntry(int chunkX, int chunkZ);

    @Shadow @Nullable public abstract PlayerChunkMapEntry getEntry(int x, int z);

    @Shadow protected abstract void markSortPending();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstruct(WorldServer worldIn, CallbackInfo ci) {
        this.setPlayerViewRadius(Settings.experimental.dynamicViewDistance ? 3 : worldIn.getMinecraftServer().getPlayerList().getViewDistance());
    }

    /**
     * @author Homoium
     * @reason dynamic view distance
     */
    @Overwrite
    public void updateMovingPlayer(EntityPlayerMP player)
    {
        int i = (int)player.posX >> 4;
        int j = (int)player.posZ >> 4;
        double d0 = player.managedPosX - player.posX;
        double d1 = player.managedPosZ - player.posZ;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 >= 64.0D)
        {
            int k = (int)player.managedPosX >> 4;
            int l = (int)player.managedPosZ >> 4;
            final int viewDistance = this.playerViewRadius;
            int i1 = Settings.experimental.dynamicViewDistance ? viewDistance : Math.max(((AccessorPlayerChunkMap)this).getPlayerViewRadius(), viewDistance);
            int j1 = i - k;
            int k1 = j - l;

            if (j1 != 0 || k1 != 0)
            {
                for (int l1 = i - i1; l1 <= i + i1; ++l1)
                {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2)
                    {
                        if (!this.overlaps(l1, i2, k, l, i1))
                        {
                            this.getOrCreateEntry(l1, i2).addPlayer(player);
                        }

                        if (!this.overlaps(l1 - j1, i2 - k1, i, j, i1))
                        {
                            PlayerChunkMapEntry playerchunkmapentry = this.getEntry(l1 - j1, i2 - k1);

                            if (playerchunkmapentry != null)
                            {
                                playerchunkmapentry.removePlayer(player);
                            }
                        }
                    }
                }

                player.managedPosX = player.posX;
                player.managedPosZ = player.posZ;
                this.markSortPending();
            }
        }
    }

    @Override
    public void updateViewDistance(EntityPlayerMP player, int distanceIn) {
        final int oldViewDistance = ((IEntityPlayerMPMethods)player).getViewDistance();

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
            ((IEntityPlayerMPMethods)player).setViewDistance(playerViewDistance);
        }
    }

    @Override
    public void setViewDistance(EntityPlayerMP entityplayer, int i, boolean markSort) {
        i = MathHelper.clamp(i, 3, 64);
        int oldViewDistance = ((IEntityPlayerMPMethods)entityplayer).getViewDistance();
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

package io.wdsj.homoium.mixin.experimental;

import com.mojang.authlib.GameProfile;
import io.wdsj.homoium.config.Settings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends EntityPlayer implements IContainerListener, IEntityPlayerMPMethods {
    @Shadow @Final public MinecraftServer server;

    public MixinEntityPlayerMP(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }
    private int viewDistance = -1;
    @Override
    public int getViewDistance() {
        return viewDistance == -1 ? ((AccessorPlayerChunkMap)((WorldServer) world).getPlayerChunkMap()).getPlayerViewRadius() : viewDistance;
    }
    @Override
    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
        this.furthestViewableBlock = PlayerChunkMap.getFurthestViewableBlock(getViewDistance()); // Beast - Dynamic view distance
    }
    private int clientsideViewDistance = 0;
    private int nextViewDistanceUpdateTick = 0;
    private int furthestViewableBlock = PlayerChunkMap.getFurthestViewableBlock(getViewDistance());
    @Override
    public int getFurthestViewableBlock() { return furthestViewableBlock; }

    @Override
    public int getClientsideViewDistance() { return this.clientsideViewDistance; }

    @Inject(method = "handleClientSettings", at = @At("HEAD"))
    public void handleClientSettings(CPacketClientSettings pkt, CallbackInfo ci) {
        if (Settings.experimental.dynamicViewDistance) {
            if (viewDistance != ((AccessorCPacketClientSettings)pkt).getViewDistance() && nextViewDistanceUpdateTick < server.getTickCounter()) {
                this.nextViewDistanceUpdateTick = server.getTickCounter() + (20 * 15);
                this.clientsideViewDistance = ((AccessorCPacketClientSettings)pkt).getViewDistance();
                int distance = Math.min(clientsideViewDistance, ((AccessorPlayerChunkMap)((WorldServer)world).getPlayerChunkMap()).getPlayerViewRadius());
                ((IPlayerChunkMapMethods)((WorldServer) world).getPlayerChunkMap()).updateViewDistance(((EntityPlayerMP)(Object)this), distance);
                ((WorldServer) world).getEntityTracker().track(this); // Update entity tracker
            }
        }
    }


}

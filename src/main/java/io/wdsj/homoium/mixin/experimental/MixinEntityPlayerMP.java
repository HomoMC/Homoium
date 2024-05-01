package io.wdsj.homoium.mixin.experimental;

import com.mojang.authlib.GameProfile;
import io.wdsj.homoium.interfaces.exp.IEntityPlayerMPMethods;
import io.wdsj.homoium.interfaces.exp.IPlayerChunkMapMethods;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends EntityPlayer implements IContainerListener, IEntityPlayerMPMethods {
    @Shadow @Final public MinecraftServer server;

    public MixinEntityPlayerMP(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }
    @Unique
    private int homoium_RFG$viewDistance = -1;
    @Override
    public int homoium_RFG$getViewDistance() {
        return homoium_RFG$viewDistance == -1 ? ((AccessorPlayerChunkMap)((WorldServer) world).getPlayerChunkMap()).getPlayerViewRadius() : homoium_RFG$viewDistance;
    }
    @Override
    public void homoium_RFG$setViewDistance(int viewDistance) {
        this.homoium_RFG$viewDistance = viewDistance;
        this.homoium_RFG$furthestViewableBlock = PlayerChunkMap.getFurthestViewableBlock(homoium_RFG$getViewDistance()); // Beast - Dynamic view distance
    }
    @Unique
    private int homoium_RFG$clientsideViewDistance = 0;
    @Unique
    private int homoium_RFG$nextViewDistanceUpdateTick = 0;
    @Unique
    private int homoium_RFG$furthestViewableBlock = PlayerChunkMap.getFurthestViewableBlock(homoium_RFG$getViewDistance());
    @Override
    public int homoium_RFG$getFurthestViewableBlock() { return homoium_RFG$furthestViewableBlock; }

    @Override
    public int homoium_RFG$getClientsideViewDistance() { return this.homoium_RFG$clientsideViewDistance; }

    @Inject(method = "handleClientSettings", at = @At("HEAD"))
    public void handleClientSettings(CPacketClientSettings pkt, CallbackInfo ci) {
        if (homoium_RFG$viewDistance != ((AccessorCPacketClientSettings)pkt).getViewDistance() && homoium_RFG$nextViewDistanceUpdateTick < server.getTickCounter()) {
            this.homoium_RFG$nextViewDistanceUpdateTick = server.getTickCounter() + (20 * 15);
            this.homoium_RFG$clientsideViewDistance = ((AccessorCPacketClientSettings)pkt).getViewDistance();
            int distance = Math.min(homoium_RFG$clientsideViewDistance, ((AccessorPlayerChunkMap)((WorldServer)world).getPlayerChunkMap()).getPlayerViewRadius());
            ((IPlayerChunkMapMethods)((WorldServer) world).getPlayerChunkMap()).homoium_RFG$updateViewDistance(((EntityPlayerMP)(Object)this), distance);
            ((WorldServer) world).getEntityTracker().untrack(this);
            ((WorldServer) world).getEntityTracker().track(this); // Update entity tracker
        }
    }


}

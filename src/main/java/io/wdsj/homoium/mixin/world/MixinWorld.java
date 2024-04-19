package io.wdsj.homoium.mixin.world;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class MixinWorld implements IBlockAccess {

    @Shadow public abstract boolean isBlockLoaded(BlockPos pos);

    @Shadow @Final public boolean isRemote;

    @Inject(method = "observedNeighborChanged", at = @At("HEAD"), cancellable = true)
    public void checkBeforeChange(BlockPos pos, Block changedBlock, BlockPos changedBlockPos, CallbackInfo ci) {
        if (!this.isRemote && !this.isBlockLoaded(pos)) ci.cancel();
    }


}

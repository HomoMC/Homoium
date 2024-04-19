package io.wdsj.homoium.mixin.ai.pathfinding;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*
The hottest part of path-finding is reading blocks out from the world.
This makes a number of changes to avoid slow paths in the game and to better inline code.
In testing, it shows a small improvement in path-finding code.
Credits to: Lithium
 */
@Mixin(ChunkCache.class)
public abstract class MixinChunkCache {
    @Unique
    @Final
    private static IBlockState DEFAULT_BLOCK = Blocks.AIR.getDefaultState();

    @Shadow
    protected int chunkX;
    @Shadow
    protected int chunkZ;
    @Shadow
    protected Chunk[][] chunkArray;

    @Unique
    private Chunk[] homoium$chunksFlat;
    @Unique
    private int homoium$xLen, homoium$zLen;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initChunkCache(World world, BlockPos blockposition, BlockPos blockposition1, int i, CallbackInfo ci) {
        this.homoium$xLen = 1 + (blockposition1.getX() >> 4) - (blockposition.getX() >> 4);
        this.homoium$zLen = 1 + (blockposition1.getZ() >> 4) - (blockposition.getZ() >> 4);
        this.homoium$chunksFlat = new Chunk[this.homoium$xLen * this.homoium$zLen];

        for (int x = 0; x < this.homoium$xLen; x++) {
            System.arraycopy(this.chunkArray[x], 0, this.homoium$chunksFlat, x * this.homoium$zLen, this.homoium$zLen);
        }
    }

    @Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
    private void getBlockState(BlockPos blockposition, CallbackInfoReturnable<IBlockState> cir) {
        int y = blockposition.getY();
        if (y >= 0 && y < 256) {
            int x = blockposition.getX();
            int z = blockposition.getZ();
            int chunkX = (x >> 4) - this.chunkX;
            int chunkZ = (z >> 4) - this.chunkZ;
            if (chunkX >= 0 && chunkX < this.homoium$xLen && chunkZ >= 0 && chunkZ < this.homoium$zLen) {
                Chunk chunk = this.homoium$chunksFlat[(chunkX * this.homoium$zLen) + chunkZ];
                if (chunk != null) {
                    ExtendedBlockStorage section = chunk.getBlockStorageArray()[y >> 4];
                    if (section != null) {
                        cir.setReturnValue(section.get(x & 15, y & 15, z & 15));
                    }
                }
            }
        }
        cir.setReturnValue(DEFAULT_BLOCK);
    }
}

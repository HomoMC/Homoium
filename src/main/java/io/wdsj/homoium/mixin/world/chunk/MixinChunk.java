package io.wdsj.homoium.mixin.world.chunk;

import io.wdsj.homoium.interfaces.world.chunk.IMixinChunkMethods;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

/**
 * Cache random ticks.
 * It shows a small improvement in performance.
 */
@Mixin(Chunk.class)
public abstract class MixinChunk implements IMixinChunkMethods {

    @Shadow @Final private World world;
    private int lightningTick;

    @Override
    public boolean shouldDoLightning(Random random) {
        if (this.lightningTick-- <= 0) {
            this.lightningTick = random.nextInt(100000);
            return true;
        }
        return false;
    }

    private int iceAndSnowTick;

    @Override
    public boolean shouldDoIceAndSnow(Random random) {
        if (this.iceAndSnowTick-- <= 0) {
            this.iceAndSnowTick = random.nextInt(16);
            return true;
        }
        return false;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;II)V", at = @At("RETURN"))
    private void cacheRandomTicks(CallbackInfo ci) {
        shouldDoLightning(world.rand);
        shouldDoIceAndSnow(world.rand);
    }
}

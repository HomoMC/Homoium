package io.wdsj.homoium.mixin.entity;

import io.wdsj.homoium.util.FastMath;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer {

    @Redirect(method = "addMountedMovementStat", at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(F)I"))
    private int round(float value) {
        return FastMath.round(value);
    }

    @Redirect(method = "fall", at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(D)J"))
    private long roundFall(double value) {
        return FastMath.round(value);
    }

    @Redirect(method = "addMovementStat", at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(F)I"))
    private int roundMovement(float value) {
        return FastMath.round(value);
    }

    @Redirect(method = "damageEntity", at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(F)I"))
    private int roundDamage(float value) {
        return FastMath.round(value);
    }


}

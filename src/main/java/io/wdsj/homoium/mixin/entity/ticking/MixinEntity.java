package io.wdsj.homoium.mixin.entity.ticking;

import io.wdsj.homoium.Homoium;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
VMP optimization: Skip movement if zero
 */
@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow private AxisAlignedBB boundingBox;
    @Unique
    private boolean homoium$isBoundingBoxChanged = false;

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void checkBeforeMove(MoverType type, double x, double y, double z, CallbackInfo ci) {
        if (!homoium$isBoundingBoxChanged && x == 0.0D && y == 0.0D && z == 0.0D) {
            ci.cancel();
        }
    }

    @Inject(method = "setEntityBoundingBox", at = @At("HEAD"))
    public void checkBeforeSetAABB(AxisAlignedBB bb, CallbackInfo ci) {
        if(this.boundingBox == null)
        {
            Homoium.LOGGER.warn("ATTENTION, ENTITY " + EntityList.getEntityString((Entity)(Object)this) + " has no AABB contact the mod owner about this.");
            return;
        }
        if (!this.boundingBox.equals(bb)) homoium$isBoundingBoxChanged = true;
    }

}

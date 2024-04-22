package io.wdsj.homoium.mixin.entity.ticking;

import io.wdsj.homoium.config.Settings;
import net.minecraft.entity.item.EntityMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityMinecart.class)
public abstract class MixinEntityMinecart {

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/EntityMinecart;canBeRidden()Z", shift = At.Shift.BEFORE) ,cancellable = true, remap = false)
    public void onUpdate(CallbackInfo ci) {
        if (((AccessorEntity) this).getTicksExisted() % Settings.minecartSkipTicks != 0) {
            ci.cancel();
        }
    }
}

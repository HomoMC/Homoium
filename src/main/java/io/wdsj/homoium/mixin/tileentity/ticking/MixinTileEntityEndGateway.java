package io.wdsj.homoium.mixin.tileentity.ticking;

import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.world.WorldProviderEnd;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityEndGateway.class)
public abstract class MixinTileEntityEndGateway extends TileEntityEndPortal {

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void checkBeforeTick(CallbackInfo ci) {
        if (!(this.world.provider instanceof WorldProviderEnd)) {
            ci.cancel();
        }
    }
}

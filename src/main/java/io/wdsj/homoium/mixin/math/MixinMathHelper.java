package io.wdsj.homoium.mixin.math;

import io.wdsj.homoium.util.lithium.CompactSineLUT;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MathHelper.class)
public abstract class MixinMathHelper {
    /**
     * @author None
     * @reason Just overwrite it
     */
    @Overwrite
    public static float sin(float value) {
        return CompactSineLUT.sin(value);
    }

    /**
     * @author None
     * @reason Just overwrite it
     */
    @Overwrite
    public static float cos(float value) {
        return CompactSineLUT.cos(value);
    }
}

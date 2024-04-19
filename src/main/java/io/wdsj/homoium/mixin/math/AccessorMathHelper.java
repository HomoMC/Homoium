package io.wdsj.homoium.mixin.math;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MathHelper.class)
public interface AccessorMathHelper {
    @Accessor("SIN_TABLE")
    static void setSinTable(float[] table) {
        throw new AssertionError();
    }

    @Accessor("SIN_TABLE")
    static float[] getSinTable() {
        throw new AssertionError();
    }
}

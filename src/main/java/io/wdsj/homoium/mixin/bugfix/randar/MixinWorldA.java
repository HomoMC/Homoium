package io.wdsj.homoium.mixin.bugfix.randar;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.*;

import java.util.Random;

@Mixin(World.class)
public abstract class MixinWorldA {

    @Shadow
    public abstract WorldInfo getWorldInfo();

    @Final
    @Unique
    private Random forge_mixin_randarfixer$separateRandOnlyForWorldGen = new Random();

    /**
     * @author RandarFixer
     * @reason Fix randar exploit
     */
    @Overwrite
    public Random setRandomSeed(int seedX, int seedY, int seedZ) {
        long j2 = (long)seedX * 341873128712L + (long)seedY * 132897987541L + this.getWorldInfo().getSeed() + (long)seedZ;
        forge_mixin_randarfixer$separateRandOnlyForWorldGen.setSeed(j2);
        return forge_mixin_randarfixer$separateRandOnlyForWorldGen;
    }
}

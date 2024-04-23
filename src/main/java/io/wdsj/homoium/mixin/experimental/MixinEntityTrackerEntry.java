package io.wdsj.homoium.mixin.experimental;

import io.wdsj.homoium.config.Settings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityTrackerEntry.class)
public abstract class MixinEntityTrackerEntry {

    @Shadow @Final private int range;

    @Shadow private int maxRange;

    @Shadow private long encodedPosX;

    @Shadow private long encodedPosZ;

    @Shadow @Final private Entity trackedEntity;

    /**
     * @author Homoium
     * @reason Dynamic view distance
     */
    @Overwrite
    public boolean isVisibleTo(EntityPlayerMP playerMP)
    {
        double d0 = playerMP.posX - (double)this.encodedPosX / 4096.0D;
        double d1 = playerMP.posZ - (double)this.encodedPosZ / 4096.0D;
        int i = Math.min(this.range, Settings.experimental.dynamicViewDistance ? ((IEntityPlayerMPMethods) playerMP).getFurthestViewableBlock() : this.maxRange);
        return d0 >= (double)(-i) && d0 <= (double)i && d1 >= (double)(-i) && d1 <= (double)i && this.trackedEntity.isSpectatedByPlayer(playerMP);
    }

}

package io.wdsj.homoium.mixin.entity.living;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Shadow public abstract boolean isHandActive();

    @Shadow public abstract ItemStack getHeldItem(EnumHand hand);

    @Shadow public abstract EnumHand getActiveHand();

    @Shadow protected ItemStack activeItemStack;
    @Shadow protected int activeItemStackUseCount;

    @Shadow public abstract int getItemInUseCount();

    @Shadow protected abstract void updateItemUse(ItemStack stack, int eatingParticleCount);

    @Shadow protected abstract void onItemUseFinish();

    @Shadow public abstract void resetActiveHand();

    protected long eatStartTime;
    protected int totalEatTimeTicks;

    /**
     * @author Homoium
     * @reason Lag compensation
     */
    @Overwrite
    protected void updateActiveHand()
    {
        if (this.isHandActive())
        {
            ItemStack itemstack = this.getHeldItem(this.getActiveHand());
            if (net.minecraftforge.common.ForgeHooks.canContinueUsing(this.activeItemStack, itemstack)) this.activeItemStack = itemstack;

            if (itemstack == this.activeItemStack)
            {
                if (!this.activeItemStack.isEmpty())
                {
                    activeItemStackUseCount = net.minecraftforge.event.ForgeEventFactory.onItemUseTick((EntityLivingBase)(Object)this, activeItemStack, activeItemStackUseCount);
                    if (activeItemStackUseCount > 0)
                        activeItemStack.getItem().onUsingTick(activeItemStack,(EntityLivingBase)(Object)this, activeItemStackUseCount);
                }

                if (this.getItemInUseCount() <= 25 && this.getItemInUseCount() % 4 == 0)
                {
                    this.updateItemUse(this.activeItemStack, 5);
                }

                boolean shouldLagCompensate = this.activeItemStack.getItem() instanceof ItemFood && this.eatStartTime != -1 && (System.nanoTime() - this.eatStartTime) > ((2 + this.totalEatTimeTicks) * 50 * (1000 * 1000));
                if ((--this.activeItemStackUseCount <= 0 || shouldLagCompensate) && !this.world.isRemote)
                {
                    this.activeItemStackUseCount = 0;
                    this.onItemUseFinish();
                }
            }
            else
            {
                this.resetActiveHand();
            }
        }
    }

    @Inject(method = "setActiveHand", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;activeItemStackUseCount:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void afterActiveItemStackUseCountSet(CallbackInfo ci) {
        this.totalEatTimeTicks = this.activeItemStackUseCount;
        this.eatStartTime = System.nanoTime();
    }

    @Inject(method = "notifyDataManagerChange", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;activeItemStackUseCount:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER, ordinal = 1))
    private void notifyData(CallbackInfo ci) {
        this.totalEatTimeTicks = 0;
        this.eatStartTime = -1L;
    }

    @Inject(method = "resetActiveHand", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;activeItemStackUseCount:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void afterResetActiveHand(CallbackInfo ci) {
        this.totalEatTimeTicks = 0;
        this.eatStartTime = -1L;
    }
}

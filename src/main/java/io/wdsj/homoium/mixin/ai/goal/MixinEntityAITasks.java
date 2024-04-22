package io.wdsj.homoium.mixin.ai.goal;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.entity.ai.EntityAITasks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(EntityAITasks.class)
public abstract class MixinEntityAITasks {

    @Mutable
    @Shadow
    @Final
    public Set<EntityAITasks.EntityAITaskEntry> taskEntries;

    @Mutable
    @Shadow
    @Final
    private Set<EntityAITasks.EntityAITaskEntry> executingTaskEntries;

    @Inject(method = "<init>(Lnet/minecraft/profiler/Profiler;)V", at = @At("RETURN"))
    private void reinit(CallbackInfo ci) {
        this.taskEntries = new ObjectLinkedOpenHashSet<>();
        this.executingTaskEntries = new ObjectLinkedOpenHashSet<>();
    }
}
package io.wdsj.homoium.mixin.experimental;


import net.minecraft.network.play.client.CPacketClientSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketClientSettings.class)
public interface AccessorCPacketClientSettings {

    @Accessor("view")
    int getViewDistance();
}

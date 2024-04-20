package io.wdsj.homoium;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import io.wdsj.homoium.config.Settings;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class HomoiumMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        MixinExtrasBootstrap.init();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        switch (mixinClassName) {
            case "io.wdsj.homoium.mixin.entity.ticking.MixinEntityMinecart":
                if (!Settings.optimizeMinecartCollision) {
                    return false;
                }
                Homoium.LOGGER.info("Minecart collision optimization enabled");
                break;
            case "io.wdsj.homoium.mixin.entity.ticking.MixinEntityPlayerMP":
                if (!Settings.dontTickFrostWalkForDeadPlayers) {
                    return false;
                }
                Homoium.LOGGER.info("Frost walk optimization enabled");
                break;
            case "io.wdsj.homoium.mixin.tileentity.ticking.MixinTileEntityEndGateway":
                if (!Settings.dontTickEndGatewayForNonEnd) {
                    return false;
                }
                Homoium.LOGGER.info("End gateway optimization enabled");
                break;
            case "io.wdsj.homoium.mixin.world.MixinWorld":
                if (!Settings.preventBlockPhysicsLoadChunk) {
                    return false;
                }
                break;
            case "io.wdsj.homoium.mixin.ai.pathfinding.MixinChunkCache":
                if (!Settings.lithiumOptimization.optimizeChunkCache) {
                    return false;
                }
                break;
            case "io.wdsj.homoium.mixin.math.MixinMathHelper":
                if (!Settings.lithiumOptimization.optimizeMath) {
                    return false;
                }
                break;
            case "io.wdsj.homoium.mixin.world.MixinWorldServer":
                if (!Settings.hibernateWhenNoPlayersOnline) {
                    return false;
                }
            case "io.wdsj.homoium.mixin.world.MixinWorldA":
                if (!Settings.bugFix.fixRandarExploit) {
                    return false;
                }
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}

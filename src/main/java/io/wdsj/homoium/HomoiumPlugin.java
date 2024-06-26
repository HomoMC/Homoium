package io.wdsj.homoium;

import com.google.common.collect.ImmutableMap;
import io.wdsj.homoium.config.Settings;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

@IFMLLoadingPlugin.Name("HomoiumCorePlugin")
public class HomoiumPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {
    public static final boolean isClient = FMLLaunchHandler.side().isClient();

    private static final Map<String, Supplier<Boolean>> serversideMixinConfigs = ImmutableMap.copyOf(new HashMap<String, Supplier<Boolean>>()
    {
        {
            put("mixins.opt.hibernate.server.json", () -> Settings.hibernateWhenNoPlayersOnline);
            put("mixins.misc.lag.compensate.eating.server.json", () -> Settings.miscellaneous.lagCompensateEating);
        }
    });

    private static final Map<String, Supplier<Boolean>> commonSideMixinConfigs = ImmutableMap.copyOf(new HashMap<String, Supplier<Boolean>>()
    {
        {
            put("mixins.opt.lithium.math.common.json", () -> Settings.lithiumOptimization.optimizeMath);
            put("mixins.opt.entity.movement.common.json", () -> Settings.optimizeEntityMovement);
            put("mixins.opt.minecart.collision.common.json", () -> Settings.optimizeMinecartCollision);
            put("mixins.opt.entity.frostwalk.common.json", () -> Settings.dontTickFrostWalkForDeadPlayers);
            put("mixins.opt.block.physics.common.json", () -> Settings.preventBlockPhysicsLoadChunk);
            put("mixins.opt.tile.endgateway.common.json", () -> Settings.dontTickEndGatewayForNonEnd);
            put("mixins.bugfix.randar.common.json", () -> Settings.bugFix.fixRandarExploit);
            put("mixins.opt.lithium.ai.goal.common.json", () -> Settings.lithiumOptimization.optimizeAiGoal);
            put("mixins.misc.prevent.teleport.dead.players.common.json", () -> Settings.miscellaneous.preventTeleportDeadPlayers);
            put("mixins.opt.cache.random.ticks.common.json", () -> Settings.cacheRandomTicks);
            put("mixins.opt.carpet.math.common.json", () -> Settings.carpetOptimization.carpetOptimizeMath);
            put("mixins.exp.dyn.view.common.json", () -> Settings.experimental.dynamicViewDistance);
        }
    });
    @Override
    public List<String> getMixinConfigs() {
        List<String> configs = new ArrayList<>();
        if (!isClient) configs.addAll(serversideMixinConfigs.keySet());
        configs.addAll(commonSideMixinConfigs.keySet());
        return configs;
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        Supplier<Boolean> sidedSupplier = isClient ? null : serversideMixinConfigs.get(mixinConfig);
        Supplier<Boolean> commonSupplier = commonSideMixinConfigs.get(mixinConfig);
        if (sidedSupplier != null) {
            return sidedSupplier.get();
        }
        if (commonSupplier != null) {
            return commonSupplier.get();
        }
        return true;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}

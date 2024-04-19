package io.wdsj.homoium.config;

import io.wdsj.homoium.Homoium;
import net.minecraftforge.common.config.Config;

@Config(modid = Homoium.MOD_ID)
public class Settings {
    @Config.Comment("Should we optimize minecart collision?")
    @Config.RequiresMcRestart
    public static boolean optimizeMinecartCollision = true;

    @Config.RequiresMcRestart
    public static int minecartSkipTicks = 30;

    @Config.Comment("Lithium optimizations")
    @Config.RequiresMcRestart
    public static Lithium lithiumOptimization = new Lithium();
    public static class Lithium {
        @Config.Comment("Should we optimize chunk cache?")
        @Config.RequiresMcRestart
        public boolean optimizeChunkCache = true;
        @Config.Comment("Should we optimize math?")
        @Config.RequiresMcRestart
        public boolean optimizeMath = true;
    }

    @Config.Comment("Should we optimize entity movement?")
    @Config.RequiresMcRestart
    public static boolean optimizeEntityMovement = true;

    @Config.Comment("Prevent block physics from loading the chunk?")
    @Config.RequiresMcRestart
    public static boolean preventBlockPhysicsLoadChunk = true;

    @Config.Comment("Don't tick frostwalk for dead players")
    @Config.RequiresMcRestart
    public static boolean dontTickFrostWalkForDeadPlayers = true;

    @Config.Comment("Don't tick end gateway in other dimension")
    @Config.RequiresMcRestart
    public static boolean dontTickEndGatewayForNonEnd = true;
}

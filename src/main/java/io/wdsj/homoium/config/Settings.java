package io.wdsj.homoium.config;

import com.cleanroommc.configanytime.ConfigAnytime;
import io.wdsj.homoium.Homoium;
import net.minecraftforge.common.config.Config;

@Config(modid = Homoium.MOD_ID)
public class Settings {
    @Config.Comment("Should we optimize minecart collision?")
    @Config.RequiresMcRestart
    public static boolean optimizeMinecartCollision = true;

    @Config.Comment("How many minecart ticks should we skip?")
    @Config.RequiresMcRestart
    public static int minecartSkipTicks = 30;

    @Config.Comment("Should we cache random ticks?")
    @Config.RequiresMcRestart
    public static boolean cacheRandomTicks = false;

    @Config.Comment("Lithium optimizations")
    @Config.RequiresMcRestart
    public static Lithium lithiumOptimization = new Lithium();
    public static class Lithium {
        @Config.Comment("Should we optimize math?")
        @Config.RequiresMcRestart
        public boolean optimizeMath = true;

        @Config.Comment("Should we optimize ai goal?")
        @Config.RequiresMcRestart
        public boolean optimizeAiGoal = false;
    }

    @Config.Comment("Carpet optimizations")
    public static Carpet carpetOptimization = new Carpet();

    public static class Carpet {
        @Config.Comment("Should we optimize math?")
        @Config.RequiresMcRestart
        public boolean carpetOptimizeMath = true;
    }
    @Config.Comment("Bug fixes")
    @Config.RequiresMcRestart
    public static BugFix bugFix = new BugFix();
    public static class BugFix {
        @Config.Comment("Should we patch the Randar exploit?")
        @Config.RequiresMcRestart
        public boolean fixRandarExploit = true;
    }

    @Config.Comment("Misc features")
    @Config.RequiresMcRestart
    public static Misc miscellaneous = new Misc();

    public static class Misc {
        @Config.Comment("Should we use lag compensate eating?")
        @Config.RequiresMcRestart
        public boolean lagCompensateEating = false;

        @Config.Comment("Should we prevent teleport dead players?")
        @Config.RequiresMcRestart
        public boolean preventTeleportDeadPlayers = false;
    }


    @Config.Comment("Experimental features")
    @Config.RequiresMcRestart
    public static Experimental experimental = new Experimental();

    public static class Experimental {
        @Config.Comment("Dynamic view distance")
        @Config.RequiresMcRestart
        public boolean dynamicViewDistance = false;

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

    @Config.Comment("Hibernate server when no players online?")
    @Config.RequiresMcRestart
    public static boolean hibernateWhenNoPlayersOnline = false;

    @Config.Comment("Don't tick end gateway in other dimension")
    @Config.RequiresMcRestart
    public static boolean dontTickEndGatewayForNonEnd = true;

    static {
        ConfigAnytime.register(Settings.class);
    }
}

package io.wdsj.homoium;

import io.wdsj.homoium.config.Settings;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.wdsj.homoium.HomoiumPlugin.isClient;


@Mod(modid = Homoium.MOD_ID, name = Homoium.MOD_NAME, version = Homoium.VERSION, dependencies = Homoium.DEPENDENCY)
public class Homoium {
    public static final String MOD_ID = "homoium";
    public static final String MOD_NAME = "Homoium";
    public static final String VERSION = "1.0.5";
    public static final String DEPENDENCY = "required-after:mixinbooter@[8.8,);";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        LOGGER.info("YJSP is in your server:/");

        if (Settings.experimental.dynamicViewDistance && !isClient) {
            LOGGER.warn("Dynamic view distance is experimental, and may cause issues.");
        }
    }
}

package io.wdsj.homoium;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Homoium.MOD_ID, name = Homoium.MOD_NAME, version = Homoium.VERSION)
public class Homoium {
    public static final String MOD_ID = "homoium";
    public static final String MOD_NAME = "Homoium";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        LOGGER.info("YJSP is in your server:/");
    }
}

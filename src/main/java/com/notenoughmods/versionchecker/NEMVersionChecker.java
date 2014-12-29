package com.notenoughmods.versionchecker;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(modid = NEMVersionChecker.MODID,
     name = NEMVersionChecker.NAME,
     version = NEMVersionChecker.VERSION)
public class NEMVersionChecker {

    public static final String NAME = "NEM Version Checker";
    public static final String MODID = "NEM-VersionChecker";
    public static final String VERSION = "0.1";

    @Instance(MODID)
    private static NEMVersionChecker instance;

    private Logger log;

    private NEMModInfo[] modInformation = null;

    private boolean disabled = false;

    private NEMEventListener listener;

    public String getMCVersion() {
        return MinecraftForge.MC_VERSION;
    }

    public void disable() {
        disabled = true;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) throws NoSuchFieldException, SecurityException {
        log = evt.getModLog();
        if(evt.getSide().equals(Side.CLIENT)) {
            FMLCommonHandler.instance().bus().register(new NEMEventListener());
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        new NEMVersionDownloader(getMCVersion());
    }

    public boolean isDisabled(){ return disabled; }

    public static NEMVersionChecker getInstance () { return instance; }

    public Logger getLog() { return log; }

    public NEMModInfo[] getModInformation() { return modInformation; }

    protected void setModInformation(NEMModInfo[] mods) { this.modInformation = mods; }
}

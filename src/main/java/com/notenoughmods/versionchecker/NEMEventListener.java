package com.notenoughmods.versionchecker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.client.GuiSlotModList;
import cpw.mods.fml.common.ModContainer;

public class NEMEventListener {

    private Field modList;
    private Field parent;
    private Field mods;
    private Field listWidth;
    private Field buttonList;

    public NEMEventListener() throws NoSuchFieldException, SecurityException {
        modList = GuiModList.class.getDeclaredField("modList");
        parent = GuiSlotModList.class.getDeclaredField("parent");
        mods = GuiSlotModList.class.getDeclaredField("mods");
        listWidth = GuiScrollingList.class.getDeclaredField("listWidth");
        try {
            buttonList = GuiScreen.class.getDeclaredField("buttonList");
        } catch(Exception e) {
            buttonList = GuiScreen.class.getDeclaredField("field_146292_n");
        }
        modList.setAccessible(true);
        parent.setAccessible(true);
        mods.setAccessible(true);
        listWidth.setAccessible(true);
        buttonList.setAccessible(true);
    }

    @SubscribeEvent(priority= EventPriority.HIGHEST)
    public void renderTick(RenderTickEvent evt) {
        if (!NEMVersionChecker.getInstance().isDisabled()) {
            if (evt.type.equals(Type.RENDER)) {
                if (FMLClientHandler.instance().getClient().currentScreen != null && FMLClientHandler.instance().getClient().currentScreen.getClass() == GuiModList.class) {
                    try {
                        GuiSlotModList old = (GuiSlotModList) modList.get(FMLClientHandler.instance().getClient().currentScreen);
                        if (old != null && (old.getClass() != NEMGuiSlotModList.class)) {
                            GuiModList one = (GuiModList) parent.get(old);
                            ArrayList<ModContainer> two = (ArrayList<ModContainer>) mods.get(old);
                            Integer three = (Integer) listWidth.get(old);
                            NEMGuiSlotModList newGui = new NEMGuiSlotModList(one, two, three);
                            newGui.registerScrollButtons((List) buttonList.get(FMLClientHandler.instance().getClient().currentScreen), 7, 8);
                            modList.set(FMLClientHandler.instance().getClient().currentScreen, newGui);
                            new NEMVersionDownloader(NEMVersionChecker.getInstance().getMCVersion());
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

package com.drazuam.runicarcana.client.event;

import com.drazuam.runicarcana.client.gui.GuiChalk;
import com.drazuam.runicarcana.common.event.IEventHandler;
import com.drazuam.runicarcana.common.item.ItemChalkDefault;
import com.drazuam.runicarcana.common.keybind.ModKeybind;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * Created by Joel on 2/21/2017.
 */
public class KeyEventHandler implements IEventHandler {

    public static boolean isChalkGuiOpen = false;

    @SubscribeEvent
    public void keyEvent(InputEvent.KeyInputEvent keyInputEvent)
    {
        //Check if the chalk keybind was pressed, and that the player is holding chalk in one of thier hands
        if (ModKeybind.keybindChalk.isPressed()&&Minecraft.getMinecraft().currentScreen==null
                &&((Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.MAIN_HAND)!=null&&Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemChalkDefault)
                ||(Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.OFF_HAND)!=null&&Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemChalkDefault)))
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiChalk());

        }
    }

}

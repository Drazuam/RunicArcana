package com.latenighters.runicarcana.client.event;

import com.latenighters.runicarcana.client.KeyBindings;
import com.latenighters.runicarcana.client.gui.ScreenChalk;
import com.latenighters.runicarcana.common.items.ChalkItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;

import static com.latenighters.runicarcana.RunicArcana.MODID;

@Mod.EventBusSubscriber
public class KeyEventHandler {

    @SubscribeEvent
    public static void onKeyInputEvent(InputEvent.KeyInputEvent event)
    {
        if(KeyBindings.CHALK.isKeyDown() && Minecraft.getInstance().currentScreen == null
            && ((Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND)!=null && Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ChalkItem)
            || (Minecraft.getInstance().player.getHeldItem(Hand.OFF_HAND)!=null   && Minecraft.getInstance().player.getHeldItem(Hand.OFF_HAND).getItem()  instanceof ChalkItem)))
        {
            Minecraft.getInstance().displayGuiScreen(new ScreenChalk(new TranslationTextComponent(MODID+"gui.chalk_title")));
        }

    }

    public static void registerKeyBindings()
    {
        ArrayList<KeyBinding> bindings = new ArrayList<KeyBinding>();

        bindings.add(KeyBindings.CHALK);

        for(KeyBinding binding : bindings)
        {
            ClientRegistry.registerKeyBinding(binding);
        }
    }

}

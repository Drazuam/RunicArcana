package com.latenighters.runicarcana.client.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.latenighters.runicarcana.RunicArcana.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GuiTextures {

    public static final ResourceLocation CHALK_BACKGROUND = new ResourceLocation(MODID , "/textures/gui/chalk_gui.png");
    public static final ResourceLocation CHALK_POPUP      = new ResourceLocation(MODID, "/textures/gui/popup.png");
    public static final ResourceLocation CHALK_SELECT     = new ResourceLocation(MODID, "/textures/gui/selected_box.png");

    @SubscribeEvent
    public static void onGuiStitchEvent(TextureStitchEvent.Pre event)
    {
//        ResourceLocation stitching = event.getMap().getTextureLocation();
//        if(!stitching.equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE))
//        {
//            event.addSprite(CHALK_BACKGROUND);
//        }

    }
}

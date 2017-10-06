package com.drazuam.runicarcana.client.event;

import com.drazuam.runicarcana.common.enchantment.DustModelHandler;
import com.drazuam.runicarcana.common.event.IEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Joel on 2/20/2017.
 */
public class StitchEventHandler implements IEventHandler {

    @SubscribeEvent
    public void textureStuff(TextureStitchEvent.Pre event)
    {
//        if (event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
//            for (DustModelHandler.Textures tex : DustModelHandler.Textures.values()) {
//                TextureAtlasSprite texAt = event.getMap().registerSprite(new ResourceLocation(tex.location));
//                System.out.println(texAt.toString());
//
//            }
//        }

        if (event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
            for (DustModelHandler.DustTypes dustTypes : DustModelHandler.DustTypes.values()) {
                TextureAtlasSprite texAt = event.getMap().registerSprite(new ResourceLocation(dustTypes.texture));
                //System.out.println(texAt.toString());

            }
        }

    }




}

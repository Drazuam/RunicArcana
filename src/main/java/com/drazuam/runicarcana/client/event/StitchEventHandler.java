package com.drazuam.runicarcana.client.event;

import com.drazuam.runicarcana.api.enchantment.IDustSymbol;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.event.IEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;

/**
 * Created by Joel on 2/20/2017.
 */
public class StitchEventHandler implements IEventHandler {

    @SubscribeEvent
    public void textureStuff(TextureStitchEvent.Pre event)
    {
        if (event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {

            for (LinkedList<IDustSymbol> category : ModDust.dustRegistry)
            {
                for(IDustSymbol dust : category)
                {

                    String textureStr = dust.getTexture();
                    int index = textureStr.lastIndexOf(".png");
                    String texture =  "runicarcana:"+textureStr.substring(9,index);
                    event.getMap().registerSprite(new ResourceLocation(texture));
                }

            }


        }
    }
}

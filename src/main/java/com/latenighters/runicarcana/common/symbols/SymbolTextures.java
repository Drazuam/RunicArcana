package com.latenighters.runicarcana.common.symbols;

import com.latenighters.runicarcana.RunicArcana;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.latenighters.runicarcana.RunicArcana.MODID;


import static com.latenighters.runicarcana.RunicArcana.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SymbolTextures {

    public static final ResourceLocation DEBUG = new ResourceLocation(MODID , "symbols/symbol_x");



    @SubscribeEvent
    public static void onStitchEvent(TextureStitchEvent.Pre event)
    {
        ResourceLocation stitching = event.getMap().getTextureLocation();
        if(!stitching.equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE))
        {
            return;
        }
        for (ResourceLocation tex:textureList)
        {
            event.addSprite(tex);
        }
    }

    public static void consolidateTextures(final RegistryEvent.Register<Symbol> evt)
    {

        for(Symbol symbol : evt.getRegistry().getValues())
        {
            if(!textureList.contains(symbol.getTexture()))
            {
                textureList.add(symbol.getTexture());
            }
        }
    }

    private static ArrayList<ResourceLocation> textureList = new ArrayList<ResourceLocation>();

}

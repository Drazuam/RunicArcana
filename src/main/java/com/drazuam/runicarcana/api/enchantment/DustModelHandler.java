package com.drazuam.runicarcana.api.enchantment;

import com.drazuam.runicarcana.common.RunicArcana;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * Created by Joel on 2/19/2017.
 */
public class DustModelHandler {


    public static IBakedModel getBakedModel(String modelLocation){

        IModel model;
        try {
            model = ModelLoaderRegistry.getModel(new ResourceLocation(RunicArcana.MODID, modelLocation));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        IBakedModel bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.BLOCK, DustModelHandler::textureGetter);

        return bakedModel;
    }

    public static TextureAtlasSprite textureGetter( ResourceLocation location)
    {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
    }


}

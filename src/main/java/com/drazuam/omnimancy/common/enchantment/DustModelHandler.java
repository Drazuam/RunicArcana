package com.drazuam.omnimancy.common.enchantment;

import com.drazuam.omnimancy.common.Omnimancy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;

import static com.drazuam.omnimancy.client.enchantment.lib.LibResources.*;

/**
 * Created by Joel on 2/19/2017.
 */
public class DustModelHandler {


    //Dust type enumeration; add all new dust types here
    public enum DustTypes{
        START(0,MODEL_DUST_START, 3, "textures/block/dustStart.png","dustStart"),
        CHANGE(1,MODEL_DUST_CHANGE,3, "textures/block/dustChange.png","dustChange"),
        CONNECT(2,MODEL_DUST_CONNECT,1, "textures/block/dustConnector.png","dustConnector"),
        DICKBUTT(3,MODEL_DUST_DICKBUTT,3,"textures/block/dustDickbutt.png","dustDickbutt"),
        IN(4,MODEL_DUST_IN,1,"textures/block/dustIn.png","dustIn"),
        OUT(5,MODEL_DUST_OUT,1,"textures/block/dustOut.png","dustOut"),
        MATH(6,MODEL_DUST_MATH,3,"textures/block/dustMath.png","dustMath"),
        CONSTANT(7,MODEL_DUST_CONSTANT,3,"textures/block/dustConstant.png","dustConstant"),
        COMPARE(8,MODEL_DUST_COMPARE,3,"textures/block/dustCompare.png","dustCompare"),
        NAME(9,MODEL_DUST_NAME,3,"textures/block/dustName.png","dustName"),
        COMBINE(10,MODEL_DUST_COMBINE,3,"textures/block/dustCombine.png","dustCombine"),
        BREAK(11,MODEL_DUST_BREAK,3,"textures/block/dustBreak.png","dustBreak"),
        SIGHT(12,MODEL_DUST_SIGHT,3,"textures/block/dustSight.png","dustSight"),
        PROJECTION(13,MODEL_DUST_PROJECTION,3,"textures/block/dustProjection.png","dustProjection"),
        MOVE(14,MODEL_DUST_MOVE,3,"textures/block/dustMove.png","dustVelocity");


        private final String modelLocation;
        private final int size;
        private final int ID;
        private IBakedModel bakedModel;
        public final ResourceLocation location;
        public final String defaultName;
        public final String texture;

        DustTypes(final int newID, final String newModelLocation, final int newSize, String newLocation, String newName)
        {
            modelLocation = newModelLocation;
            size = newSize;
            ID = newID;
            location = new ResourceLocation(Omnimancy.MODID, newLocation);
            defaultName = newName;
            String textureStr = newLocation;
            int index = textureStr.lastIndexOf(".png");
            texture =  "omnimancy:"+textureStr.substring(9,index);
        }

        public void setBakedModel(IBakedModel newBakedModel){bakedModel = newBakedModel;}
        public IBakedModel getBakedModel(){
            if (bakedModel == null) {
                IModel model;
                try {
                    model = ModelLoaderRegistry.getModel(new ResourceLocation(Omnimancy.MODID, modelLocation));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.BLOCK, DustModelHandler::textureGetter);

                System.out.println("[Omnicraft] created model for " + modelLocation);
            }

            return bakedModel;
        }

        public int getSize(){return size;}
        public String getModel(){return modelLocation;}
        public int getID(){return ID;}

    }

    public static TextureAtlasSprite textureGetter( ResourceLocation location)
    {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
    }


}

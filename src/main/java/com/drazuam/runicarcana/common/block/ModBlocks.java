package com.drazuam.runicarcana.common.block;

import com.drazuam.runicarcana.common.RunicArcana;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Joel on 2/18/2017.
 */
public class ModBlocks {

    public static Block chalkBase ;

    public static void preInit()
    {
        chalkBase = new BlockChalkBase("chalk_base");

        registerBlocks();
    }

    public static void registerBlocks()
    {

        registerBlock(chalkBase ,"chalk_base");
    }

    public static void registerBlock(Block block, String name)
    {
        GameRegistry.register(block, new ResourceLocation(RunicArcana.MOD_ID, name));
        GameRegistry.register(new ItemBlock(block), new ResourceLocation(RunicArcana.MOD_ID,name));
    }

    public static void registerRenders()
    {

        registerRender(chalkBase );
    }

    public static void registerRender(Block block)
    {
        Item item = Item.getItemFromBlock(block);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0 , new ModelResourceLocation(RunicArcana.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));

    }

}

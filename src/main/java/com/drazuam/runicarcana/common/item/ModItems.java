package com.drazuam.runicarcana.common.item;

import com.drazuam.runicarcana.common.RunicArcana;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Joel on 2/18/2017.
 */
public class ModItems {

    public static Item infusionItem;
    public static Item defaultChalkItem;
    public static Item paperScrapItem;

    public static void preInit()
    {

        defaultChalkItem    = new ItemChalkDefault("chalk_default_item");
        infusionItem        = new ItemInfusionStone("infusion_stone_item");
        paperScrapItem      = new ItemPaperScrap("paper_scrap_item");

        registerItems();
    }

    public static void registerItems()
    {
        GameRegistry.register(infusionItem, new ResourceLocation(RunicArcana.MOD_ID, "infusion_stone_item"));
        GameRegistry.register(defaultChalkItem, new ResourceLocation(RunicArcana.MOD_ID, "chalk_default_item"));
        GameRegistry.register(paperScrapItem, new ResourceLocation(RunicArcana.MOD_ID, "paper_scrap_item"));
    }

    public static void registerRenders()
    {
        registerRender(infusionItem);
        registerRender(defaultChalkItem);
        registerRender(paperScrapItem);
    }

    public static void registerRender(Item item)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0 , new ModelResourceLocation(RunicArcana.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }
}

package com.drazuam.runicarcana.common.recipes;

import com.drazuam.runicarcana.common.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Joel on 2/26/2017.
 */
public class ModRecipesVanilla {

    public static void registerRecipes()
    {
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.defaultChalkItem),new ItemStack(Items.DYE,1,15), Items.DIAMOND, Items.CLAY_BALL, new ItemStack(Items.DYE,1,15), new ItemStack(Items.DYE,1,15));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.paperScrapItem,9), Items.PAPER);
    }





}

package com.drazuam.runicarcana.common.item;

import com.drazuam.runicarcana.client.gui.GuiPaperScrap;
import com.drazuam.runicarcana.common.RunicArcana;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


/**
 * Created by Joel on 2/20/2017.
 */
public class ItemPaperScrap extends Item {

    public ItemPaperScrap(String name)
    {
        setUnlocalizedName(name);
        setCreativeTab(RunicArcana.creativeTabRunicArcana);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {

        if(worldIn.isRemote)
        {
            //if the scrap has nbtdata for text, open it.  Otherwise open a blank scrap
            if(itemStackIn.getTagCompound()==null || !itemStackIn.getTagCompound().hasKey("text"))
                Minecraft.getMinecraft().displayGuiScreen(new GuiPaperScrap(""));
            else
                Minecraft.getMinecraft().displayGuiScreen(new GuiPaperScrap(itemStackIn.getTagCompound().getString("text")));
        }


        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }



}

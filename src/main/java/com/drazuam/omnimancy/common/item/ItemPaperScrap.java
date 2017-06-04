package com.drazuam.omnimancy.common.item;

import com.drazuam.omnimancy.client.gui.GuiPaperScrap;
import com.drazuam.omnimancy.common.Omnimancy;
import com.drazuam.omnimancy.common.block.ModBlocks;
import com.drazuam.omnimancy.common.enchantment.*;
import com.drazuam.omnimancy.common.enchantment.Symbols.DustSymbolConnector;
import com.drazuam.omnimancy.common.tileentity.TileEntityChalkBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.io.*;
import java.util.LinkedList;


/**
 * Created by Joel on 2/20/2017.
 */
public class ItemPaperScrap extends Item {

    public ItemPaperScrap(String name)
    {
        setUnlocalizedName(name);
        setCreativeTab(Omnimancy.creativeTabOmnimancy);
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

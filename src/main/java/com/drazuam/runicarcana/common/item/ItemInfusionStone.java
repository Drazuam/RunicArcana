package com.drazuam.runicarcana.common.item;

import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.common.block.ModBlocks;
import com.drazuam.runicarcana.common.enchantment.*;
import com.drazuam.runicarcana.common.enchantment.Signals.CompiledSymbol;
import com.drazuam.runicarcana.common.enchantment.Symbols.DustSymbolStart;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Joel on 2/24/2017.
 */
public class ItemInfusionStone extends Item {


    public ItemInfusionStone(String name)
    {
        setUnlocalizedName(name);
        setCreativeTab(RunicArcana.creativeTabRunicArcana);
        setMaxStackSize(1);
    }


    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {


        if(!worldIn.isRemote&&worldIn.getBlockState(pos).getBlock()==ModBlocks.chalkBase)
        {
            TileEntityChalkBase te = (TileEntityChalkBase)worldIn.getTileEntity(pos);
            DefaultDustSymbol startDust = te.getDustAt(hitX,hitZ);
            if(startDust instanceof DustSymbolStart)
            {
                System.out.println("start script compilation");
                ItemStack enchantItem = te.getItem();
                if(enchantItem==null)return EnumActionResult.SUCCESS;
                if(enchantItem.isItemEnchanted())return EnumActionResult.SUCCESS;
                if (enchantItem.getTagCompound()==null)enchantItem.setTagCompound(new NBTTagCompound());
                enchantItem.getTagCompound().setByteArray("omniScript",ModDust.getFormationArray((DustSymbolStart)startDust));
                enchantItem.addEnchantment(ModEnchantment.runicenchantment, 1);
                CompiledSymbol[] script = ModDust.getScriptFromItem(enchantItem);
                //InventoryHelper.dropInventoryItems(worldIn, te.getPos(), te);
                te.updateRendering();
                return EnumActionResult.SUCCESS;

            }
        }

        return EnumActionResult.PASS;
    }








}

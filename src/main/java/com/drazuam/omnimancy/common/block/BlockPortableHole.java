package com.drazuam.omnimancy.common.block;

import com.drazuam.omnimancy.common.enchantment.DefaultDustSymbol;
import com.drazuam.omnimancy.common.enchantment.DustIOSymbol;
import com.drazuam.omnimancy.common.item.ModItems;
import com.drazuam.omnimancy.common.tileentity.TileEntityChalkBase;
import com.drazuam.omnimancy.common.tileentity.TileEntityPortableHole;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Joel on 2/19/2017.
 */
public class BlockPortableHole extends BlockAir implements ITileEntityProvider{


    public BlockPortableHole(String name)
    {
        super();
        this.setUnlocalizedName(name);
        this.setHardness(-1.0F);
        this.fullBlock = false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }


    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }


    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPortableHole();
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

}

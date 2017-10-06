package com.drazuam.runicarcana.common.block;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.enchantment.DustIOSymbol;
import com.drazuam.runicarcana.common.item.ModItems;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.block.Block;
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
public class BlockChalkBase extends Block implements ITileEntityProvider{

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0F,0F,0F,0.0625*16F,0.0625*0.5000000074505806F,0.0625*16F);

    public BlockChalkBase(String name)
    {
        super(Material.CIRCUITS);
        this.setUnlocalizedName(name);
        this.setHardness(0.5F);
        //this.setCreativeTab(RunicArcana.creativeTabOmnimancy);
        this.fullBlock = false;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if(!worldIn.isRemote) {
            System.out.println("left clicked");
            TileEntityChalkBase te = (TileEntityChalkBase)worldIn.getTileEntity(pos);
            RayTraceResult rt = playerIn.rayTrace(9.0F,1.0F);

            //get the hitX and hitZ since the function refuses to provide it
            double hitX = rt.hitVec.xCoord-rt.getBlockPos().getX();
            //double hitY = rt.hitVec.yCoord-rt.getBlockPos().getY();
            double hitZ = rt.hitVec.zCoord-rt.getBlockPos().getZ();

            //get the dust the player clicked. If there isn't one, exit function
            //((TileEntityChalkBase) te).getDustAt(hitX,hitZ).getIODust((int) (hitX * 3), (int) (hitZ * 3))!=null)
            DefaultDustSymbol dust = te.getDustAt(hitX,hitZ);
            if(dust==null){
                te.updateRendering();
                te.checktoDestroy();
                return;
            }

            //get the iodust the player clicked.
            DustIOSymbol ioDust = (DustIOSymbol)dust.getIODust((int) (hitX * 3), (int) (hitZ * 3));

            //if there is no ioDust, remove the original dust from everything
            if(ioDust==null)
            {
                te.dustList.remove(dust);
                for(DustIOSymbol ioDustChild : dust.ioDusts)
                {
                    ioDustChild.removeConnections();
                }

            }
            //otherwise jsut remove the ioDust and all connections
            else
            {
                ioDust.removeConnections();
                dust.ioDusts.remove(ioDust);
            }
            te.updateRendering();
            te.checktoDestroy();
        }
        else {
            ((TileEntityChalkBase) worldIn.getTileEntity(pos)).updateRendering();
        }

    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityChalkBase te = (TileEntityChalkBase)worldIn.getTileEntity(pos);
        if(te == null)return false;

        if(hand == EnumHand.OFF_HAND)return false;

        if(heldItem==null&&te.hasItem())
        {
            if(worldIn.isRemote)
            {
                te.markDirty();
                te.removeStackFromSlot(0);
                return true;
            }
            InventoryHelper.dropInventoryItems(worldIn,playerIn,te);
            te.setInventorySlotContents(0,null);
            return true;
        }
        else if(!worldIn.isRemote&&heldItem!=null&&heldItem.getItem()!= ModItems.defaultChalkItem &&!te.hasItem()&&te.isItemValidForSlot(0,heldItem))
        {
            ItemStack newItem = heldItem.copy();
            newItem.stackSize = 1;
            heldItem.stackSize--;
            if(heldItem.stackSize<1)heldItem=null;
            te.setInventorySlotContents(0,newItem);
            te.updateRendering();
            return true;
        }
        return false;




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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if(!worldIn.isRemote) {
            if(!worldIn.getBlockState(pos.down()).isOpaqueCube())
            {
                worldIn.destroyBlock(pos,false);
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        if(worldIn.getBlockState(pos.down()).isOpaqueCube())
        {
            return true;
        }
        return false;
    }



    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityChalkBase();
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityChalkBase te = (TileEntityChalkBase)worldIn.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(worldIn, pos, te);
        for(DefaultDustSymbol dust : te.dustList) {
            te.dustList.remove(dust);
            for (DustIOSymbol ioDustChild : dust.ioDusts) {
                ioDustChild.removeConnections();
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(stack.hasDisplayName()) {
            ((TileEntityChalkBase)worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
    }




}

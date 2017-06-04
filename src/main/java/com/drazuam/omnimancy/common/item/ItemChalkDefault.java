package com.drazuam.omnimancy.common.item;

import com.drazuam.omnimancy.common.Omnimancy;
import com.drazuam.omnimancy.common.block.ModBlocks;
import com.drazuam.omnimancy.common.enchantment.*;
import com.drazuam.omnimancy.common.enchantment.Symbols.DustSymbolConnector;
import com.drazuam.omnimancy.common.tileentity.TileEntityChalkBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
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
public class ItemChalkDefault extends Item {

    public ItemChalkDefault(String name)
    {
        setUnlocalizedName(name);
        setCreativeTab(Omnimancy.creativeTabOmnimancy);
        setMaxStackSize(1);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }



    //This doesn't get called very often, so I'm going to do a lot of checks in it.
    //This shouldn't affect performance, even though the whole process seems quite lengthy.
    //Could probably be cleaned up with a few variables to keep checks fast
    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState newState = worldIn.getBlockState(pos.up());
        if(worldIn.isRemote)return EnumActionResult.SUCCESS;
        if(worldIn.getBlockState(pos).isFullBlock()&&worldIn.getBlockState(pos.up())== Blocks.AIR.getDefaultState() &&facing==EnumFacing.UP)
        {
            worldIn.setBlockState(pos.up(), ModBlocks.chalkBase.getDefaultState());

            TileEntity te = worldIn.getTileEntity(pos.up());
            if(te instanceof TileEntityChalkBase)
            {
                if(stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("dustID")) {
                    int f = MathHelper.floor_double((double)(playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                    DefaultDustSymbol dust = null;
                    for (LinkedList<DefaultDustSymbol> category : ModDust.dustRegistry) {
                        for (DefaultDustSymbol regDust : category) {
                            if(regDust.dustType.getID()==stack.getTagCompound().getInteger("dustID"))
                            {
                                try {
                                    dust = regDust.getClass().newInstance().setXZFB((int) (hitX * 3), (int) (hitZ * 3), f,(TileEntityChalkBase)te);
                                }catch(IllegalAccessException|InstantiationException e)
                                {
                                    //ignore
                                }
                            }
                        }
                    }


                    if (dust!=null&&dust.dustType!= DustModelHandler.DustTypes.IN&&
                            dust.dustType!= DustModelHandler.DustTypes.OUT&&
                            ((TileEntityChalkBase) te).addDust(hitX, hitZ, f, dust)) {
                        stack.damageItem(1, playerIn);

                        return EnumActionResult.SUCCESS;
                    } else worldIn.setBlockToAir(pos.up());
                }
            }
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityChalkBase)
        {
            if(stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("dustID")) {
                int f = MathHelper.floor_double((double)(playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                DefaultDustSymbol dust = null;
                for (LinkedList<DefaultDustSymbol> category : ModDust.dustRegistry) {
                    for (DefaultDustSymbol regDust : category) {
                        if(regDust.dustType.getID()==stack.getTagCompound().getInteger("dustID"))
                        {
                            try {
                                dust = regDust.getClass().newInstance().setXZFB((int) (hitX * 3), (int) (hitZ * 3), f,(TileEntityChalkBase)te);
                            }catch(IllegalAccessException|InstantiationException e)
                            {
                                //ignore
                            }
                        }
                    }
                }


                if (dust!=null&&((TileEntityChalkBase) te).addDust(hitX, hitZ, f, dust)) {
                    stack.damageItem(1, playerIn);
                    return EnumActionResult.SUCCESS;
                }else
                {
                    if(playerIn.isSneaking()&&!worldIn.isRemote)
                    {
                        if(stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("linkedDust"))
                        {
                            //case for linking a stored connector
                            if((getDustFromByteArray(stack.getTagCompound().getByteArray("linkedDust"))).dustType == DustModelHandler.DustTypes.CONNECT)
                            {
                                //case if liknking stored connector to new connector
                                if(((TileEntityChalkBase) te).getDustAt(hitX,hitZ).dustType == DustModelHandler.DustTypes.CONNECT) {
                                    DefaultDustSymbol oldDust = (getDustFromByteArray(stack.getTagCompound().getByteArray("linkedDust")));
                                    oldDust = oldDust.getParent().getDustAt(oldDust.x / 3.0F, oldDust.z / 3.0F);
                                    if (oldDust.addConnectionLine((DustIOSymbol)((TileEntityChalkBase) te).getDustAt(hitX, hitZ), ModDust.ConnectionType.BOOLEAN)) {
                                        stack.getTagCompound().removeTag("linkedDust");
                                        return EnumActionResult.SUCCESS;
                                    }
                                }
                                //case if linking stored connector to new IO
                                else if(((TileEntityChalkBase) te).getDustAt(hitX,hitZ).getIODust((int) (hitX * 3), (int) (hitZ * 3)) instanceof DustInSymbol)  {

                                    DefaultDustSymbol oldDust = (getDustFromByteArray(stack.getTagCompound().getByteArray("linkedDust")));
                                    oldDust = oldDust.getParent().getDustAt(oldDust.x / 3.0F, oldDust.z / 3.0F);
                                    if (oldDust.addConnectionLine((DustIOSymbol)((TileEntityChalkBase) te).getDustAt(hitX, hitZ).getIODust((int) (hitX * 3), (int) (hitZ * 3)), ModDust.ConnectionType.BOOLEAN)) {
                                        stack.getTagCompound().removeTag("linkedDust");
                                        return EnumActionResult.SUCCESS;
                                    }
                                }

                            }
                            //only occurs when linking an old IO
                            else {
                                //linking old IO to new connector
                                if(((TileEntityChalkBase) te).getDustAt(hitX,hitZ).dustType == DustModelHandler.DustTypes.CONNECT) {
                                    DustIOSymbol oldDust = (DustIOSymbol) getDustFromByteArray(stack.getTagCompound().getByteArray("linkedDust"));
                                    TileEntityChalkBase oldChalk = oldDust.getParent();
                                    DefaultDustSymbol oldDustParent = oldChalk.getDustAt((float) oldDust.parent.x / 3.0F, (float) oldDust.parent.z / 3.0F);
                                    if (oldDustParent == null) return EnumActionResult.SUCCESS;
                                    oldDust = (DustIOSymbol) oldDustParent.getIODust(oldDust.x, oldDust.z);
                                    if (oldDust.addConnectionLine((DustIOSymbol)((TileEntityChalkBase) te).getDustAt(hitX, hitZ), ModDust.ConnectionType.BOOLEAN)) {
                                        stack.getTagCompound().removeTag("linkedDust");
                                        return EnumActionResult.SUCCESS;
                                    }
                                }
                                //linking old IO to new IO
                                else if(((TileEntityChalkBase) te).getDustAt(hitX,hitZ).getIODust((int) (hitX * 3), (int) (hitZ * 3)) instanceof DustInSymbol)
                                {

                                    DustIOSymbol oldDust = (DustIOSymbol) getDustFromByteArray(stack.getTagCompound().getByteArray("linkedDust"));
                                    TileEntityChalkBase oldChalk = oldDust.getParent();
                                    DefaultDustSymbol oldDustParent = oldChalk.getDustAt((float) oldDust.parent.x / 3.0F, (float) oldDust.parent.z / 3.0F);
                                    if (oldDustParent == null) return EnumActionResult.SUCCESS;
                                    oldDust = (DustIOSymbol) oldDustParent.getIODust(oldDust.x, oldDust.z);


                                    if (oldDust.addConnectionLine((DustIOSymbol)((TileEntityChalkBase) te).getDustAt(hitX, hitZ).getIODust((int) (hitX * 3), (int) (hitZ * 3)), ModDust.ConnectionType.BOOLEAN)) {
                                        stack.getTagCompound().removeTag("linkedDust");
                                        return EnumActionResult.SUCCESS;
                                    }
                                }
                            }
                        }
                        else{
                            if(stack.getTagCompound()==null) stack.setTagCompound(new NBTTagCompound());
                            DefaultDustSymbol newDust = ((TileEntityChalkBase) te).getDustAt(hitX,hitZ).getIODust((int)(hitX*3),(int)(hitZ*3));
                            if(newDust!=null&&(newDust instanceof DustOutSymbol|| newDust instanceof DustSymbolConnector)) {
                                stack.getTagCompound().setByteArray("linkedDust", getByteArrayFromDust(newDust));
                                return EnumActionResult.SUCCESS;
                            }
                        }





                    }
                    else if(!playerIn.isSneaking()&((TileEntityChalkBase) te).getDustAt(hitX,hitZ)!=null &&
                            ((TileEntityChalkBase) te).getDustAt(hitX,hitZ).getIODust((int) (hitX * 3), (int) (hitZ * 3))!=null)
                    {

                        DustIOSymbol changingDust = (DustIOSymbol)((TileEntityChalkBase) te).getDustAt(hitX,hitZ).getIODust((int) (hitX * 3), (int) (hitZ * 3));

                        if(changingDust.connectionLines.size()==0 && changingDust.parent.getNextSignal(changingDust)!=null) {
                            //stack.getTagCompound().removeTag("linkedDust");
                            return EnumActionResult.SUCCESS;
                        }


                    }
                    else if(dust!=null&&dust.dustType== DustModelHandler.DustTypes.IN&&((TileEntityChalkBase) te).getDustAt(hitX,hitZ).dustType!= DustModelHandler.DustTypes.CONNECT)
                    {
                        if(((TileEntityChalkBase) te).getDustAt(hitX,hitZ).getIODust((int)(hitX*3),(int)(hitZ*3))==null) {
                            ((TileEntityChalkBase) te).getDustAt(hitX, hitZ).addIODust(true, (int) (hitX * 3), (int) (hitZ * 3));
                            return EnumActionResult.SUCCESS;
                        }
                    }
                    else if(dust!=null&&dust.dustType== DustModelHandler.DustTypes.OUT&&((TileEntityChalkBase) te).getDustAt(hitX,hitZ).dustType!= DustModelHandler.DustTypes.CONNECT)
                    {
                        if(((TileEntityChalkBase) te).getDustAt(hitX,hitZ).getIODust((int)(hitX*3),(int)(hitZ*3))==null) {
                            ((TileEntityChalkBase) te).getDustAt(hitX, hitZ).addIODust(false, (int) (hitX * 3), (int) (hitZ * 3));
                            return EnumActionResult.SUCCESS;
                        }
                    }



                }
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if(playerIn.isSneaking())
        {
            if(itemStackIn.getTagCompound()!=null&&itemStackIn.getTagCompound().hasKey("linkedDust"))
                itemStackIn.getTagCompound().removeTag("linkedDust");
        }


        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public int getMaxDamage() {
        return 200;
    }

    private byte[] getByteArrayFromDust(DefaultDustSymbol dusty)
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out;
        byte[] bytes = null;
        try{
            out = new ObjectOutputStream(byteStream);
            out.writeObject(dusty);
            out.flush();
            bytes = byteStream.toByteArray();
            byteStream.close();

        } catch (IOException ex) {
            try {
                byteStream.close();
            }
            catch (IOException e) {
                //whatever
            }

        }
        return bytes;
    }

    private DefaultDustSymbol getDustFromByteArray(byte[] bytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            try {
                o = in.readObject();
            } catch (ClassNotFoundException e) {
                //ignore
            }
            in.close();

        } catch (IOException ex) {
            try {
                if(in!=null)in.close();
            }
            catch (IOException ee) {
                //ignore again
            }
        }
        return (DefaultDustSymbol)o;
    }


}

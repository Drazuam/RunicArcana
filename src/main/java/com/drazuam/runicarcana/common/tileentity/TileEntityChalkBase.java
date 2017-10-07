package com.drazuam.runicarcana.common.tileentity;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joel on 2/19/2017.
 */
public class TileEntityChalkBase extends TileEntity implements IInventory{

    public LinkedList<DefaultDustSymbol> dustList;

    private ItemStack[] inventory;
    private String customName;



    public TileEntityChalkBase() {
        super();
        dustList = new LinkedList<DefaultDustSymbol>();
        this.inventory = new ItemStack[this.getSizeInventory()];
    }

    public DefaultDustSymbol getDustAt(double hitX, double hitZ)
    {
        return getDustAt((float)hitX,(float)hitZ);
    }

    public DefaultDustSymbol getDustAt(float hitX, float hitZ)
    {
        int X = (int)(hitX*3);
        int Z = (int)(hitZ*3);
        for(DefaultDustSymbol dust : dustList)
        {
            int size = 0;
            for(int i=X-(size-1)/2; i<=X+(size-1)/2;i++)
                for(int j=Z-(size-1)/2; j<=Z+(size-1)/2;j++)
                    if(dust.checkOccupied(i,j))
                        return dust;
        }
        for(int moveX = -1; moveX<=1; moveX+=1)
        {
            for(int moveZ = -1; moveZ<=1; moveZ+=1)
            {
                int newX = X-moveX*3;
                int newZ = Z-moveZ*3;
                //heh good luck
                TileEntity te = this.getWorld().getTileEntity(this.pos.add(moveX,0,moveZ));
                if (te instanceof TileEntityChalkBase) {
                    for (DefaultDustSymbol dust : ((TileEntityChalkBase) te).dustList) {
                        int size = 0;
                        for (int i = newX - (size - 1) / 2; i <= newX + (size - 1) / 2; i++)
                            for (int j = newZ - (size - 1) / 2; j <= newZ + (size - 1) / 2; j++)
                                if (dust.checkOccupied(i, j))
                                    return dust;
                    }
                }
            }
        }

        return null;
    }

    public boolean addDust(float hitX, float hitZ, int f, DefaultDustSymbol dustSymbol)
    {
        boolean temp = tryToAdd(dustSymbol,(int)(hitX*3),(int)(hitZ*3));

        markDirty();
        IBlockState state = worldObj.getBlockState(pos);
        worldObj.notifyBlockUpdate(pos,state, state, 3);
        return temp;
    }

    public void checktoDestroy()
    {
        if(dustList.size()<1)
        {
            this.worldObj.destroyBlock(pos,false);
        }
    }

    public boolean hasItem()
    {
        return getStackInSlot(0)!=null;
    }

    public ItemStack getItem()
    {
        return getStackInSlot(0);
    }

    public void updateRendering()
    {
        markDirty();
        IBlockState state = worldObj.getBlockState(pos);
        worldObj.notifyBlockUpdate(pos,state, state, 3);
    }

    public DefaultDustSymbol getBiggestDust()
    {
        int maxSize=0;
        DefaultDustSymbol dustMax = null;
        for(DefaultDustSymbol dust : dustList)
        {
            if (dust.getSize()>maxSize)
            {
                dustMax = dust;
                maxSize = dust.getSize();
            }
        }

        return dustMax;

    }



    public String getCustomName()
    {
        return customName;
    }

    public void setCustomName(String customName1)
    {
        customName = customName1;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= this.getSizeInventory())
            return null;
        return this.inventory[index];
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index) != null) {
            ItemStack itemstack;

            if (this.getStackInSlot(index).stackSize <= count) {
                itemstack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, null);
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).stackSize <= 0) {
                    this.setInventorySlotContents(index, null);
                } else {
                    //Just to show that changes happened
                    this.setInventorySlotContents(index, this.getStackInSlot(index));
                }


                IBlockState state = worldObj.getBlockState(pos);
                worldObj.notifyBlockUpdate(pos,state, state, 3);
                markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = this.getStackInSlot(index);
        this.setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        if (index < 0 || index >= this.getSizeInventory())
            return;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
            stack.stackSize = this.getInventoryStackLimit();

        if (stack != null && stack.stackSize == 0)
            stack = null;

        this.inventory[index] = stack;
        if(this.getWorld()!=null&&!this.getWorld().isRemote) {
            IBlockState state = worldObj.getBlockState(pos);
            worldObj.notifyBlockUpdate(pos,state, state, 3);
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return getBiggestDust().willAccept(stack);
        //return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.setInventorySlotContents(0,null);
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.chalk_block";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName!=null&&!this.customName.equals("");
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    private byte[] getByteArrayFromList(LinkedList<DefaultDustSymbol> dustyList)
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out;
        byte[] bytes = null;
        try{
            out = new ObjectOutputStream(byteStream);
            out.writeObject(dustyList);
            out.flush();
            bytes = byteStream.toByteArray();

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

    private LinkedList<DefaultDustSymbol> getListFromByteArray(byte[] bytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        LinkedList<DefaultDustSymbol> dustyList;
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
        return doSafeCast(o, DefaultDustSymbol.class);
    }

    private <T, L extends List<T>> List<T> doSafeCast(Object listObject,
                                                      Class<T> type,
                                                      Class<L> listClass) {
        try {
            List<T> result = listClass.newInstance();


            if (listObject instanceof List) {
                List<?> list = (List<?>) listObject;

                for (Object obj : list) {
                    if (type.isInstance(obj)) {
                        result.add(type.cast(obj));
                    }
                }
            }
            return result;
        }
        catch(IllegalAccessException|InstantiationException e)
        {
            //ignore, mostly
            return null;
        }
    }

    private <T> LinkedList<T> doSafeCast(Object listObject, Class<T> type) {
        LinkedList<T> result = new LinkedList<T>();

        if (listObject instanceof List) {
            List<?> list = (List<?>)listObject;

            for (Object obj: list) {
                if (type.isInstance(obj)) {
                    result.add(type.cast(obj));
                }
            }
        }

        return result;
    }


    //This function lol.  It's a little silly.
    public boolean tryToAdd(DefaultDustSymbol symbol, int X, int Z)
    {
        for(DefaultDustSymbol dust : dustList)
        {
            int size = symbol.getSize();
            for(int i=X-(size-1)/2; i<=X+(size-1)/2;i++)
                for(int j=Z-(size-1)/2; j<=Z+(size-1)/2;j++)
                    if(dust.checkOccupied(i,j))
                        return false;
        }
        for(int moveX = -1; moveX<=1; moveX+=1)
        {
            for(int moveZ = -1; moveZ<=1; moveZ+=1)
            {
                int newX = X-moveX*3;
                int newZ = Z-moveZ*3;
                //heh good luck
                TileEntity te = this.getWorld().getTileEntity(this.pos.add(moveX,0,moveZ));
                if (te instanceof TileEntityChalkBase) {
                    for (DefaultDustSymbol dust : ((TileEntityChalkBase) te).dustList) {
                        int size = symbol.getSize();
                        for (int i = newX - (size - 1) / 2; i <= newX + (size - 1) / 2; i++)
                            for (int j = newZ - (size - 1) / 2; j <= newZ + (size - 1) / 2; j++)
                                if (dust.checkOccupied(i, j))
                                    return false;
                    }
                }
            }
        }

        dustList.add(symbol);
        return true;
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        //compound.setInteger("DustID",this.dustType.getID());
        compound.setByteArray("Symbols",getByteArrayFromList(dustList));

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        compound.setTag("Items",list);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.getCustomName());
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.readUpdateTag(compound);





    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tagCom = pkt.getNbtCompound();
        readUpdateTag(tagCom);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tagCom = new NBTTagCompound();
        this.writeUpdateTag(tagCom);
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(),tagCom);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        writeUpdateTag(tag);
        return tag;
    }

    public void writeUpdateTag(NBTTagCompound tag)
    {

        //tag.setInteger("DustID",this.dustType.getID());
        tag.setByteArray("Symbols",getByteArrayFromList(dustList));

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        tag.setTag("Items",list);
        if (this.hasCustomName()) {
            tag.setString("CustomName", this.getCustomName());
        }

    }

    public void readUpdateTag(NBTTagCompound tag)
    {
        dustList = getListFromByteArray(tag.getByteArray("Symbols"));

        NBTTagList list = tag.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
        }

        if (tag.hasKey("CustomName", 8)) {
            this.setCustomName(tag.getString("CustomName"));
        }

    }


}

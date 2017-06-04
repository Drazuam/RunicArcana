package com.drazuam.omnimancy.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Joel on 3/11/2017.
 */
public abstract class VanishingTileEntity extends TileEntity {

    private IBlockState prev;
    private TileEntity tile;
    private int alive;

    public VanishingTileEntity()
    {
        //register the tile entity to allow ticking
        FMLCommonHandler.instance().bus().register(this);
    }

    public void SetFields(IBlockState prevState, TileEntity te, double time)
    {
        //20 ticks in a second - keep this block alive for an amount of ticks
        alive = (int)(time*20);
        prev = prevState;
        tile = te;

    }



    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent event)
    {
        //only run this stuff if we are in the right world
        if(!event.world.equals(this.worldObj))return;

        //tick down time alive
        alive--;

        //if we've run out of time, delete self and replace with previous block and tile entity.
        //I wonder if this will work?
        if(alive<=0)
        {
            this.worldObj.setBlockState(this.pos,this.prev);
            this.worldObj.setTileEntity(this.pos,this.tile);
        }
    }


}

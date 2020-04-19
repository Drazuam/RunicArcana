package com.latenighters.runicarcana.common.symbols;

import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.Position;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.items.CapabilityItemHandler;

public class ExpulsionSymbol extends Symbol {
    public ExpulsionSymbol() {
        super("symbol_expulsion", SymbolTextures.EXPEL, SymbolCategory.DEFAULT);
    }

    @Override
    public void onTick(DrawnSymbol symbol, World world, IChunk chunk, BlockPos drawnOn, Direction blockFace) {

        if(symbol.getTicksAlive()%20!=0)return;

        if(!world.isRemote())
        {
            TileEntity tileEntity = world.getTileEntity(drawnOn);

            //first check for an inventory
            IInventory inventory = HopperTileEntity.getInventoryAtPosition(world,drawnOn);

            //if we cant find an inventory, check for an Item Handler Capability
            if (tileEntity!=null)
            {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap ->{

                    int numSlots = cap.getSlots();
                    for (int i=0; i<numSlots; i++)
                    {
                        if(cap.getStackInSlot(i).getCount()>0)
                        {
                            ItemStack item = cap.extractItem(i,1,false);
                            DefaultDispenseItemBehavior.doDispense(world,item,1,blockFace, new Position(drawnOn.getX(), drawnOn.getY(), drawnOn.getZ()));
                            return;
                        }
                    }

                });
                return;
            }

            //if we do have an inventory,

        }

    }
}

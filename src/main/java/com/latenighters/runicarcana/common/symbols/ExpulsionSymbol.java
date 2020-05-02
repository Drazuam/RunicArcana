package com.latenighters.runicarcana.common.symbols;

import com.latenighters.runicarcana.common.symbols.backend.*;
import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.Position;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ExpulsionSymbol extends Symbol {
    public ExpulsionSymbol() {
        super("symbol_expulsion", SymbolTextures.EXPEL, SymbolCategory.DEFAULT);
    }

    private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, @Nullable Direction side) {
        if (!inventoryIn.isItemValidForSlot(index, stack)) {
            return false;
        } else {
            return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side);
        }
    }

    private static ItemStack insertStack(@Nullable IInventory source, IInventory destination, ItemStack stack, int index, @Nullable Direction direction) {
        ItemStack itemstack = destination.getStackInSlot(index);
        if (canInsertItemInSlot(destination, stack, index, direction)) {
            boolean flag = false;
            if (itemstack.isEmpty()) {
                destination.setInventorySlotContents(index, stack);
                stack = ItemStack.EMPTY;
                flag = true;
            } else if (canCombine(itemstack, stack)) {
                int i = stack.getMaxStackSize() - itemstack.getCount();
                int j = Math.min(stack.getCount(), i);
                stack.shrink(j);
                itemstack.grow(j);
                flag = j > 0;
            }
            if (flag) {
                destination.markDirty();
            }
        }
        return stack;
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        if (stack1.getItem() != stack2.getItem()) {
            return false;
        } else if (stack1.getDamage() != stack2.getDamage()) {
            return false;
        } else if (stack1.getCount() > stack1.getMaxStackSize()) {
            return false;
        } else {
            return ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
    }


    @Override
    protected void registerFunctions() {

        //put all Hashable Tuples
        HashableTuple<String,DataType> enableInput = new HashableTuple<>("Enabled",DataType.BOOLEAN);
        HashableTuple<String,DataType> entityInput = new HashableTuple<>("Target", DataType.ENTITY);
        HashableTuple<String,DataType> blockFaceInput = new HashableTuple<>("Target", DataType.BLOCK_FACE);

        List<HashableTuple<String, DataType>> requiredInputs = new ArrayList<HashableTuple<String, DataType>>();
        requiredInputs.add(enableInput);
        requiredInputs.add(entityInput);
        requiredInputs.add(blockFaceInput);

        this.functions.add(new IFunctional() {

            final List<HashableTuple<String, DataType>> requiredInputsFinal = requiredInputs;

            @Override
            public String getName() {
                return null;
            }

            @Override
            public List<HashableTuple<String, DataType>> getRequiredInputs() {
                return requiredInputsFinal;
            }

            @Override
            public String getOutputString(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args){

                Boolean enabled = new Boolean(true);
                Entity entity = null;

                boolean gotEntityInput = false;
                boolean gotInventoryInput = false;

                for(HashableTuple<String, Object> arg : args)
                {
                    if(arg==null)continue;
                    if(arg.getA().equals("Enabled"))
                        enabled = (Boolean)arg.getB()!=null ? (Boolean)arg.getB() : enabled;
                    else if(arg.getA().equals(entityInput.getA())) {
                        entity = (Entity) arg.getB();
                        gotEntityInput = true;
                    }
                    else if(arg.getA().equals(blockFaceInput.getA())){

                    }
                }
                if (enabled) return "Enabled";

                return "Disabled";
            }

            @Override
            public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args) {

                //default values
                Boolean enabled = new Boolean(true);
                Entity entity = null;

//                Map<String,Object> toFill = new HashMap<>();
//                toFill.put(enableInput.getA(), new AtomicReference<>(enabled));
//                toFill.put(entityInput.getA(), new AtomicReference<>(entity));

                boolean gotEntityInput = false;
                boolean gotInventoryInput = false;

                for(HashableTuple<String, Object> arg : args)
                {
                    if(arg==null)continue;
                    if(arg.getA().equals("Enabled"))
                        enabled = (Boolean)arg.getB()!=null ? (Boolean)arg.getB() : enabled;
                    else if(arg.getA().equals(entityInput.getA())) {
                        entity = (Entity) arg.getB();
                        gotEntityInput = true;
                    }
                    else if(arg.getA().equals(blockFaceInput.getA())){

                    }
                }


                if(enabled && !(gotEntityInput && entity == null))
                {
                    DrawnSymbol symbol = (DrawnSymbol)object;
                    World world = chunk.getWorld();
                    BlockPos drawnOn = symbol.getDrawnOn();
                    Direction blockFace = symbol.getBlockFace();

                    if(symbol.getTicksAlive()%20!=0)return null;

                    if(!world.isRemote())
                    {
                        //check for tile entities with item handler capability
                        TileEntity tileEntity   = world.getTileEntity(drawnOn);
                        TileEntity tileEntityTo = world.getTileEntity(drawnOn.offset(blockFace));

                        boolean capabilityFrom = false;
                        boolean capabilityTo   = false;
                        if(tileEntity!=null)
                            capabilityFrom = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent();
                        if(tileEntityTo!=null)
                            capabilityTo   = tileEntityTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent();

                        //check for an inventory
                        IInventory inventory   = HopperTileEntity.getInventoryAtPosition(world,drawnOn);
                        IInventory inventoryTo = null;

                        if(entity instanceof PlayerEntity)
                            inventoryTo = ((PlayerEntity)entity).inventory;
                        else
                            inventoryTo = HopperTileEntity.getInventoryAtPosition(world,drawnOn.offset(blockFace));


                        //first handle inventory to inventory - this can easily support minecarts, etc
                        if(inventory!=null && inventoryTo!=null)
                        {
                            if(inventory.isEmpty()) return null;
                            int numSlots = inventory.getSizeInventory();
                            boolean transferred = false;
                            for (int i=0; i<numSlots; i++)
                            {
                                if (inventory.getStackInSlot(i).getCount()==0) continue;
                                ItemStack itemStack = inventory.getStackInSlot(i).copy();
                                inventory.getStackInSlot(i).shrink(1);
                                itemStack.setCount(1);
                                int prevCount = itemStack.getCount();
                                ItemStack leftover = HopperTileEntity.putStackInInventoryAllSlots(inventory, inventoryTo, itemStack, blockFace.getOpposite());
                                if(leftover.getCount()<prevCount) {
                                    transferred = true;
                                    break;
                                }
                            }
                            if (transferred)
                            {
                                symbol.applyServerTorque(80, chunk);
                            }
                        }
                        else if(inventory!=null)
                        {
                            if(inventory.isEmpty()) return null;
                            int numSlots = inventory.getSizeInventory();
                            boolean transferred = false;
                            for (int i=0; i<numSlots; i++)
                            {
                                if(inventory.getStackInSlot(i).getCount()< 1)continue;

                                ItemStack toDrop = inventory.getStackInSlot(i).copy();
                                toDrop.setCount(1);
                                inventory.getStackInSlot(i).shrink(1);

                                Position dropFrom;
                                switch(symbol.getBlockFace())
                                {
                                    case UP:
                                        dropFrom = new Position(drawnOn.getX()+0.5, drawnOn.getY()+1.1, drawnOn.getZ()+0.5);
                                        break;
                                    case DOWN:
                                        dropFrom = new Position(drawnOn.getX()+0.5, drawnOn.getY()-0.1, drawnOn.getZ()+0.5);
                                        break;
                                    case NORTH:
                                        dropFrom = new Position(drawnOn.getX()+0.5, drawnOn.getY()+0.5, drawnOn.getZ()-0.1);
                                        break;
                                    case SOUTH:
                                        dropFrom = new Position(drawnOn.getX()+0.5, drawnOn.getY()+0.5, drawnOn.getZ()+1.1);
                                        break;
                                    case EAST:
                                        dropFrom = new Position(drawnOn.getX()+1.1, drawnOn.getY()+0.5, drawnOn.getZ()+0.5);
                                        break;
                                    case WEST:
                                    default:
                                        dropFrom = new Position(drawnOn.getX()-0.1, drawnOn.getY()+0.5, drawnOn.getZ()+0.5);
                                        break;
                                }

                                DefaultDispenseItemBehavior.doDispense(world,toDrop,1,blockFace, dropFrom);
                                symbol.applyServerTorque(80, chunk);
                                break;
                            }
                        }
                        else if(capabilityFrom)
                        {
                            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap ->{

                                int numSlots = cap.getSlots();
                                for (int i=0; i<numSlots; i++)
                                {
                                    if(cap.getStackInSlot(i).getCount()>0)
                                    {
                                        ItemStack item = cap.extractItem(i,1,false);
                                        Position dropFrom;
                                        switch(symbol.getBlockFace())
                                        {
                                            case UP:
                                                dropFrom = new Position(drawnOn.getX()+0.5, drawnOn.getY()+1.1, drawnOn.getZ()+0.5);
                                                break;
                                            case DOWN:
                                                dropFrom = new Position(drawnOn.getX()+0.5, drawnOn.getY()-0.1, drawnOn.getZ()+0.5);
                                                break;
                                            case NORTH:
                                                dropFrom = new Position(drawnOn.getX()+0.5, drawnOn.getY()+0.5, drawnOn.getZ()-0.1);
                                                break;
                                            case SOUTH:
                                                dropFrom = new Position(drawnOn.getX()+0.5, drawnOn.getY()+0.5, drawnOn.getZ()+1.1);
                                                break;
                                            case EAST:
                                                dropFrom = new Position(drawnOn.getX()+1.1, drawnOn.getY()+0.5, drawnOn.getZ()+0.5);
                                                break;
                                            case WEST:
                                            default:
                                                dropFrom = new Position(drawnOn.getX()-0.1, drawnOn.getY()+0.5, drawnOn.getZ()+0.5);
                                                break;
                                        }

                                        DefaultDispenseItemBehavior.doDispense(world,item,1,blockFace, dropFrom);
                                        symbol.applyServerTorque(80, chunk);
                                        break;
                                    }
                                }
                            });
                        }
                        //if we do have an inventory,
                    }

                }
                return null;
            }

            @Override
            public DataType getOutputType() {
                return null;
            }

            @Override
            public List<HashableTuple<String, DataType>> getTriggers() {
                return null;
            }
        });
    }
}

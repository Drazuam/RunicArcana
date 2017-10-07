package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolBreak extends DefaultDustSymbol {


    public static final String MODEL_LOCATION = "block/dust/"+"dustBreak";
    public static final String TEXTURE_LOCATION = "textures/block/dustBreak.png";
    public static final String DEFAULT_NAME = "dustBreak";

    public DustSymbolBreak(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.breakSymbol.dustType);

    }

    public DustSymbolBreak()
    {
        super(0,0,0,null, ModDust.breakSymbol.dustType);
        addMethods();

    }

    public DustSymbolBreak(short newDustType) {
        super(newDustType);
        addMethods();
    }

    private void addMethods()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL,  Signal.SigFlow.IN, "Break", DustSymbolBreak::BreakBlock ,0));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.IN, "Block Position",null,1));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done",null,2));

    }

    public static Object BreakBlock(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)args[0];
        BlockPos pos = ModDust.parseBlockPos(executor.resolveInput((short)1));
        if(!executor.player.worldObj.isRemote) {
            breakBreak(executor.RunicItem,executor.player.worldObj,executor.player,pos,executor.initBlockStrength);
        }
        executor.addProcesses(5);
        executor.resolveOutput((short)2,true);

        //TODO: seperate mana costs for breaking a block vs right clicking

        return null;
    }


    //TODO: *DONT* blatantly steal this from ticon

    public static void breakBreak(ItemStack stack, World world, EntityPlayer player, BlockPos pos, float refStrength) {
        // prevent calling that stuff for air blocks, could lead to unexpected behaviour since it fires events
        if (world.isAirBlock(pos)) {
            return;
        }

        //if(!(player instanceof EntityPlayerMP)) {
        //return;
        //}

        // check if the block can be broken, since extra block breaks shouldn't instantly break stuff like obsidian
        // or precious ores you can't harvest while mining stone
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        // only effective materials
        if (!ForgeHooks.isToolEffective(world, pos, stack)) {
            return;
        }

        float strength = ForgeHooks.blockStrength(state, player, world, pos);

        // only harvestable blocks that aren't impossibly slow to harvest
        if (!ForgeHooks.canHarvestBlock(block, player, world, pos) || refStrength / strength > 10f) {
            return;
        }

        // From this point on it's clear that the player CAN break the block

        if(player.capabilities.isCreativeMode) {
            block.onBlockHarvested(world, pos, state, player);
            if(block.removedByPlayer(state, world, pos, player, false)) {
                block.onBlockDestroyedByPlayer(world, pos, state);
            }

            // send update to client
            if(!world.isRemote) {
                if (player instanceof EntityPlayerMP && ((EntityPlayerMP) player).connection != null) {
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(world, pos));
                }
            }
            return;
        }

        // callback to the tool the player uses. Called on both sides. This damages the tool n stuff.
        stack.onBlockDestroyed(world, state, pos, player);

        // server sided handling
        if (!world.isRemote) {
            // send the blockbreak event
            int xp = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(), (EntityPlayerMP) player, pos);
            if (xp == -1) {
                return;
            }


            // serverside we reproduce ItemInWorldManager.tryHarvestBlock

            TileEntity tileEntity = world.getTileEntity(pos);
            // ItemInWorldManager.removeBlock
            if (block.removedByPlayer(state, world, pos, player, true)) // boolean is if block can be harvested, checked above
            {
                block.onBlockDestroyedByPlayer(world, pos, state);
                block.harvestBlock(world, player, pos, state, tileEntity, stack);
                block.dropXpOnBlockBreak(world, pos, xp);
            }

            if (player instanceof EntityPlayerMP && ((EntityPlayerMP) player).connection != null) {
                ((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(world, pos));
            }
        }
        // client sided handling
        else {
            PlayerControllerMP pcmp = Minecraft.getMinecraft().playerController;
            // clientside we do a "this clock has been clicked on long enough to be broken" call. This should not send any new packets
            // the code above, executed on the server, sends a block-updates that give us the correct state of the block we destroy.

            // following code can be found in PlayerControllerMP.onPlayerDestroyBlock
            world.playBroadcastSound(2001, pos, Block.getStateId(state));
            if (block.removedByPlayer(state, world, pos, player, true)) {
                block.onBlockDestroyedByPlayer(world, pos, state);
            }
            // callback to the tool
            stack.onBlockDestroyed(world, state, pos, player);

            if (stack.stackSize == 0 && stack == player.getHeldItemMainhand()) {
                ForgeEventFactory.onPlayerDestroyItem(player, stack, EnumHand.MAIN_HAND);
                player.setHeldItem(EnumHand.MAIN_HAND, null);
            }

            // send an update to the server, so we get an update back
            //if(PHConstruct.extraBlockUpdates)
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, Minecraft
                    .getMinecraft().objectMouseOver.sideHit));
        }
    }

    @Override
    public String getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    public String getTexture() {
        return TEXTURE_LOCATION;
    }

    @Override
    public String getModelLocation() {
        return MODEL_LOCATION;
    }

    @Override
    public ITextComponent getDisplayName(String name) {
        return name==null ? new TextComponentTranslation("dust."+DEFAULT_NAME+".name") : new TextComponentTranslation("dust."+name+".name");
    }
}


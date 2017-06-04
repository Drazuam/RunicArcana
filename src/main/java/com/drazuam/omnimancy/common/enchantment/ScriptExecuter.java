package com.drazuam.omnimancy.common.enchantment;

import com.drazuam.omnimancy.common.enchantment.Signals.CompiledSymbol;
import com.drazuam.omnimancy.common.enchantment.Signals.Signal;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Joel on 2/27/2017.
 */
public class ScriptExecuter {

    private CompiledSymbol[] compiledSymbols;
    public final EntityPlayer player;
    public Short currentSymbol;
    public final ItemStack OmniItem;
    public boolean variablesChanged;
    public final StartPoint startPoint;
    private LeftOffPoint leftOffPoint;
    public static final int processesPerTick = 30;
    private int processesThisTick;
    private final Queue<Object> actionQueue = Lists.newLinkedList();
    private int inputsInRow;

    private final int MAX_ALLOWED_PROCESSES;
    private static final int MAX_ALLOWED_INPUT = 30;

    public final IBlockState initBlock;
    public final float initBlockStrength;
    public final BlockPos initPos;


    public ScriptExecuter(CompiledSymbol[] newSymbols, EntityPlayer newPlayer, ItemStack enchantedItem, StartPoint newStartPoint)
    {

        compiledSymbols = newSymbols.clone();
        player = newPlayer;
        currentSymbol = 0;
        OmniItem = enchantedItem;
        variablesChanged = false;
        startPoint = newStartPoint;
        MAX_ALLOWED_PROCESSES = processesPerTick+MAX_ALLOWED_INPUT;
        inputsInRow = 0;
        initPos = player.rayTrace(6,1.0F).getBlockPos();
        initBlock = player.worldObj.getBlockState(initPos);
        initBlockStrength = ForgeHooks.blockStrength(initBlock, player, player.worldObj, initPos);

        switch(startPoint)
        {
            case SNEAK_RIGHT:
                actionQueue.add(new Short((short)0));
                actionQueue.add(new Short((short)1));
                actionQueue.add(true);
                break;
            case RIGHT_CLICK:
                actionQueue.add(new Short((short)0));
                actionQueue.add(new Short((short)0));
                actionQueue.add(true);
                break;
            case BLOCK_BREAK:
                actionQueue.add(new Short((short)0));
                actionQueue.add(new Short((short)3));
                actionQueue.add(true);
                break;
            default:
                break;
        }

        if(!ModDust.runningScripts.contains(this)&&!player.worldObj.isRemote)
        {
            ModDust.runningScripts.add(this);
            MinecraftForge.EVENT_BUS.register(this);
        }


    }

    public ScriptExecuter(CompiledSymbol[] newSymbols, EntityPlayer newPlayer, ItemStack enchantedItem, StartPoint newStartPoint, BlockPos newPos)
    {

        compiledSymbols = newSymbols.clone();
        player = newPlayer;
        currentSymbol = 0;
        OmniItem = enchantedItem;
        variablesChanged = false;
        startPoint = newStartPoint;
        MAX_ALLOWED_PROCESSES = processesPerTick+MAX_ALLOWED_INPUT;
        inputsInRow = 0;
        initPos = newPos;
        initBlock = player.worldObj.getBlockState(initPos);
        initBlockStrength = ForgeHooks.blockStrength(initBlock, player, player.worldObj, initPos);

        switch(startPoint)
        {
            case SNEAK_RIGHT:
                actionQueue.add(new Short((short)0));
                actionQueue.add(new Short((short)1));
                actionQueue.add(true);
                break;
            case RIGHT_CLICK:
                actionQueue.add(new Short((short)0));
                actionQueue.add(new Short((short)0));
                actionQueue.add(true);
                break;
            case BLOCK_BREAK:
                actionQueue.add(new Short((short)0));
                actionQueue.add(new Short((short)3));
                actionQueue.add(true);
                break;
            default:
                break;
        }

        if(!ModDust.runningScripts.contains(this)&&!player.worldObj.isRemote)
        {
            ModDust.runningScripts.add(this);
            MinecraftForge.EVENT_BUS.register(this);
        }


    }

//    public void rightClick()
//    {
//        if(!player.isSneaking()) {
//            ModDust.dustRegistry.getFirst().getFirst().getSignal(0).doFunction(this);
//            startPoint = StartPoint.RIGHT_CLICK;
//        }
//        else {
//            ModDust.dustRegistry.getFirst().getFirst().getSignal(1).doFunction(this);
//            startPoint = StartPoint.SNEAK_RIGHT;
//        }
//        if(variablesChanged)
//            ModDust.loadScriptToItem(compiledSymbols, OmniItem);
//    }

    @SubscribeEvent
    public void doNextTick(TickEvent.WorldTickEvent event)
    {

        //only tick in this dimension
        if(event.world.provider.getDimension()!=this.player.worldObj.provider.getDimension())
        {
            return;
        }
        //make sure player is still holding item
        if((this.startPoint==StartPoint.RIGHT_CLICK||this.startPoint==StartPoint.SNEAK_RIGHT||this.startPoint==StartPoint.BLOCK_BREAK)
            &&!(this.player.getHeldItem(EnumHand.MAIN_HAND)==OmniItem||this.player.getHeldItem(EnumHand.OFF_HAND)==OmniItem)) {
            done();
            return;
        }





        this.processesThisTick=0;
        variablesChanged=false;
        //update script
        compiledSymbols = ModDust.getScriptFromItem(OmniItem);

        //run through our list until we've hit max processes
        while(this.processesThisTick<processesPerTick&&actionQueue.size()>2)
        {
            processesThisTick++;
            try {
                resolveNextOutput();
            }
            catch(NullPointerException e)
            {

            }
        }

        if(actionQueue.size()<3) done();

        if(variablesChanged)
            ModDust.loadScriptToItem(compiledSymbols, OmniItem);
    }

    public void resolveNextOutput()
    {
        //make sure we're starting on a Short and we have at least 3 elements in our queue.
        if(!(actionQueue.peek() instanceof Short)||actionQueue.size()<3) {
            done();
            return;
        }
        inputsInRow=0;
        //get the action elements
        this.currentSymbol=(Short)actionQueue.poll();
        Short SigID = (Short)actionQueue.poll();
        Object o  = actionQueue.poll();

        ArrayList<Short>  locations = getLocationsOfSignal(SigID);

        if(locations.size()==0)return;

        //previous working symbol - store for when we're done with this one.
        short prevSymbol = this.currentSymbol;


        for(short location:locations) {
            //the ID of the signal in the dust we want to send to
            short sendToSignal = compiledSymbols[this.currentSymbol].connections[location + 1];

            //move our current symbol to the new working symbol
            currentSymbol = (short)compiledSymbols[currentSymbol].connections[location - 1];

            //get our new symbol that we must resolve
            Signal toResolve = ModDust.getDustFromID(compiledSymbols[currentSymbol].ID).getSignal(sendToSignal);

            //resolve the symbol
            Object[] params = {this, o};
            toResolve.doFunction(params);

            currentSymbol = prevSymbol;

        }
    }

    public void done()
    {
        ModDust.runningScripts.remove(this);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void addProcesses(int ticks)
    {
        processesThisTick+=ticks;
    }

    public Object resolveInput(short SigID)
    {
        //all inputs require at least one process
        processesThisTick++;
        inputsInRow++;
        //if we are looping infinitely within only inputs, without a way to stop the process with an output resolve, kill self
        //punish player for trying to break mod
        if(inputsInRow>=MAX_ALLOWED_INPUT)
        {
            OmniItem.setStackDisplayName("Dust Overflow - Too many inputs in a row!");
            OmniItem.getTagCompound().removeTag("omniScript");
            done();
            return null;
        }

        //the location of the output signal position (in compiledSymbols[]) in compiledSymbols[currentSymbol].connections[]
        short location = getLocationOfSignal(SigID);

        //previous working symbol - store for when we're done with this one.
        short prevSymbol = this.currentSymbol;

        //the ID of the signal in the dust we want to send to
        short sendToSignal = compiledSymbols[this.currentSymbol].connections[location+1];

        //if location comes back with a -1 it means there's no symbol to resolve
        if(location==-1)return null;

        //move our current symbol to the new working symbol
        currentSymbol = (short)compiledSymbols[currentSymbol].connections[location-1];

        //get our new symbol that we must resolve
        Signal toResolve = ModDust.getDustFromID(compiledSymbols[currentSymbol].ID).getSignal(sendToSignal);

        //resolve the symbol
        Object toReturn = null;
        try {
            toReturn = toResolve.doFunction(this);
        }
        catch(StackOverflowError|NullPointerException e)
        {
            if(e instanceof NullPointerException){
                OmniItem.setStackDisplayName("Null Dust");
                OmniItem.getTagCompound().removeTag("omniScript");
                done();
            }
            else {
                OmniItem.setStackDisplayName("Dust Overflow");
                OmniItem.getTagCompound().removeTag("omniScript");
                done();
            }
        }
        currentSymbol = prevSymbol;
        return toReturn;
    }

    public void resolveOutput(Short SigID, Object o)
    {
        processesThisTick++;

        actionQueue.add(currentSymbol);
        actionQueue.add(SigID);
        actionQueue.add(o);
    }

    public void resolveOutput(Short[] SigIDs, Object o)
    {
        //everything takes at least one process
        processesThisTick++;

        for(Short SigID:SigIDs) {
            actionQueue.add(currentSymbol);
            actionQueue.add(SigID);
            actionQueue.add(o);
        }
    }




    public String getVariable()
    {
        return compiledSymbols[currentSymbol].variable;
    }

    public void setVariable(String var)
    {
        compiledSymbols[currentSymbol].variable=var;
    }


    private short getLocationOfSignal(short SigID)
    {
        for(short i=1; i<compiledSymbols[currentSymbol].connections.length; i+=3)
        {
            if(compiledSymbols[currentSymbol].connections[i]==SigID)
                return i;
        }
        return -1;
    }

    private ArrayList<Short>  getLocationsOfSignal(short SigID)
    {
        ArrayList<Short> returnShorts = new ArrayList<Short>();
        for(short i=1; i<compiledSymbols[currentSymbol].connections.length; i+=3)
        {
            if(compiledSymbols[currentSymbol].connections[i]==SigID)
                returnShorts.add(i);
        }

        return returnShorts;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScriptExecuter executer = (ScriptExecuter) o;

        if (!OmniItem.equals(executer.OmniItem)) return false;
        return startPoint == executer.startPoint;

    }

    @Override
    public int hashCode() {
        int result = OmniItem.hashCode();
        result = 31 * result + startPoint.hashCode();
        return result;
    }

    public enum StartPoint{
        RIGHT_CLICK,
        SNEAK_RIGHT,
        BLOCK_BREAK,
        ARMOR_HIT;
}

enum LeftOffPoint{
    RESOLVING_INPUT,
    RESOLVING_OUTPUT;
}


}

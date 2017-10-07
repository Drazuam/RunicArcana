package com.drazuam.runicarcana.api.enchantment;

import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.common.enchantment.*;
import com.drazuam.runicarcana.common.enchantment.Signals.CompiledSymbol;
import com.drazuam.runicarcana.common.enchantment.Symbols.*;
import gnu.trove.set.hash.THashSet;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Joel on 2/21/2017.
 */
public class ModDust {

    public static LinkedList<LinkedList<IDustSymbol>> dustRegistry = new LinkedList<LinkedList<IDustSymbol>>();

    public static final Set<ScriptExecutor> runningScripts = new THashSet<ScriptExecutor>();

    public static DefaultDustSymbol startSymbol     = new DustSymbolStart(getNextDustID());
    public static DefaultDustSymbol changeSymbol    = new DustSymbolChange(getNextDustID());
    public static DefaultDustSymbol connectSymbol   = new DustSymbolConnector(getNextDustID());
    public static DefaultDustSymbol dickbuttSymbol  = new DustSymbolDickButt(getNextDustID());
    public static DefaultDustSymbol inSymbol        = new DustInSymbol(getNextDustID());
    public static DefaultDustSymbol outSymbol       = new DustOutSymbol(getNextDustID());
    public static DefaultDustSymbol mathSymbol      = new DustSymbolMath(getNextDustID());
    public static DefaultDustSymbol constantSymbol  = new DustSymbolConstant(getNextDustID());
    public static DefaultDustSymbol compareSymbol   = new DustSymbolCompare(getNextDustID());
    public static DefaultDustSymbol nameSymbol      = new DustSymbolName(getNextDustID());
    public static DefaultDustSymbol orSymbol        = new DustSymbolOr(getNextDustID());
    public static DefaultDustSymbol breakSymbol     = new DustSymbolBreak(getNextDustID());
    public static DefaultDustSymbol sightSymbol     = new DustSymbolSight(getNextDustID());
    public static DefaultDustSymbol projectionSymbol = new DustSymbolProjection(getNextDustID());
    public static DefaultDustSymbol velocitySymbol  = new DustSymbolVelocity(getNextDustID());



    public static short currDustID=0;

    public static void registerDustsToGui()
    {
        //GuiChalk.registerDust(0,DefaultDustSymbol.class);
        registerDust(0,startSymbol);
        registerDust(0,changeSymbol);
        //registerDust(0,connectSymbol); --> Connector symbol has been removed for now
        registerDust(0,dickbuttSymbol);
        registerDust(0,inSymbol);
        registerDust(0,outSymbol);
        registerDust(0,mathSymbol);
        registerDust(0,constantSymbol);
        registerDust(0,compareSymbol);
        registerDust(0,nameSymbol);
        registerDust(0,orSymbol);
        registerDust(0,breakSymbol);
        registerDust(0,sightSymbol);
        registerDust(0,projectionSymbol);
        registerDust(0,velocitySymbol);
    }

    public static short getNextDustID()
    {
        return currDustID++;
    }


    public static void registerDust(int category, DefaultDustSymbol dust)
    {
        if(dustRegistry==null)
        {
            dustRegistry = new LinkedList<LinkedList<IDustSymbol>>();
        }
        while(dustRegistry.size()<=category)
        {
            dustRegistry.add(new LinkedList<IDustSymbol>());
        }
        dust.dustType = getNextDustID();
        dustRegistry.get(category).add(dust);
    }

    public enum ConnectionType{
        BOOLEAN(null);

        public final ResourceLocation texture;

        ConnectionType(String newLocation)
        {
            if(newLocation==null)
                texture = new ResourceLocation(RunicArcana.MODID,"textures/block/dustConnectLineDefault.png");
            else
                texture = new ResourceLocation(RunicArcana.MODID, newLocation);
        }
    }

    public static byte[] getFormationArray(DustSymbolStart startDust)
    {
        ArrayList<DefaultDustSymbol> formationList = new ArrayList<DefaultDustSymbol>();
        formationList.add(startDust);
        ListIterator<DefaultDustSymbol> iter = formationList.listIterator();
        while(iter.hasNext())
        {
            for(DustIOSymbol ioDust : iter.next().ioDusts)
            {

                for(DustConnectionLine line : ioDust.connectionLines)
                {

                    if(!formationList.contains(line.child.parent)) {
                        //stupid patch job.  children of connection lines previously were not getting their lines added to the list.
                        //if(!line.child.connectionLines.contains(line))
                        //    line.child.connectionLines.add(line);
                        iter.add(line.child.parent);
                        iter.previous();
                    }
                    if(!formationList.contains(line.parent.parent)) {
                        iter.add(line.parent.parent);
                        iter.previous();
                    }

                }
            }
        }

        //declare empty array to store our serializable script in.
        CompiledSymbol[] script = new CompiledSymbol[formationList.size()];
        int index = 0;

        //start filling that shit yo
        for(DefaultDustSymbol dust : formationList)
        {
            int numLines = 0;
            for(DustIOSymbol ioDust : dust.ioDusts)
            {
                numLines = numLines + ioDust.connectionLines.size();
            }

            //construct the current symbol
            CompiledSymbol currSym = new CompiledSymbol(dust.getDustID(), numLines);
            currSym.variable = dust.getVariable();
            int dustInd = 0;
            for(DustIOSymbol ioDust : dust.ioDusts)
            {
                for(DustConnectionLine line : ioDust.connectionLines)
                {
                    //if the io dust is an input, the other end is an output and vice versa.  Because of this, there must be different
                    //locig for the two different classes
                    if(ioDust instanceof DustOutSymbol)
                        currSym.addAction((byte)ioDust.getSignal().ID,(byte)line.child.getSignal().ID,getIDFromDust(line.child.parent, formationList),dustInd++);
                    else if(ioDust instanceof DustInSymbol)
                        currSym.addAction((byte)ioDust.getSignal().ID,(byte)line.parent.getSignal().ID,getIDFromDust(line.parent.parent, formationList),dustInd++);
                }

            }

            //add finished symbol to script
            script[index++]=currSym;
        }

        //serialize that shit into an array of bytes
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out;
        byte[] bytes = null;
        try{
            out = new ObjectOutputStream(byteStream);
            out.writeObject(script);
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

        //return serialized script
        return bytes;
    }


    public static byte getIDFromDust(DefaultDustSymbol dustSearch, ArrayList<DefaultDustSymbol> listDust)
    {
        byte returnNum = 0;
        for(int i =0; i<listDust.size();i++)
        {
            if(listDust.get(returnNum)==dustSearch)
                return returnNum;
            returnNum++;
        }
        return 0;
    }


    public static IDustSymbol getDustFromID(short ID)
    {
        for (LinkedList<IDustSymbol> category : ModDust.dustRegistry)
        {
            for(IDustSymbol dust : category)
            {
                if(dust.getDustID()==ID)
                    return dust;
            }

        }
        return null;

    }

    public static CompiledSymbol[] getScriptFromItem(ItemStack itemStack){
        if(itemStack.getTagCompound()==null||!itemStack.getTagCompound().hasKey("omniScript"))return null;
        byte[] bytes = itemStack.getTagCompound().getByteArray("omniScript");

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
        return (CompiledSymbol[])(o);
    }

    public static void loadScriptToItem(CompiledSymbol[] script, ItemStack stack)
    {

        //serialize that shit into an array of bytes
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out;
        byte[] bytes = null;
        try{
            out = new ObjectOutputStream(byteStream);
            out.writeObject(script);
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

        if(stack.getTagCompound()!=null)
            stack.getTagCompound().setByteArray("omniScript",bytes);

    }

    public static Double parseNumber(Object o)
    {
        if(o instanceof Double)
        {
            return (Double)o;
        }
        else if(o instanceof String)
        {
            try{
                return Double.parseDouble((String)o);
            }
            catch(NumberFormatException e)
            {
                return (Double)(double)0;
            }
        }
        return null;
    }

    public static BlockPos parseBlockPos(Object o)
    {
        if(o instanceof BlockPos)
        {
            return (BlockPos)o;
        }
        else if(o instanceof String)
        {
            Pattern p = Pattern.compile("^(-?\\d+(\\.\\d)?)[ ,]+(-?\\d+(\\.\\d)?)[ ,]+(-?\\d+(\\.\\d)?)");
            Matcher m = p.matcher((String)o);
            try {
                m.find();
                int x = Integer.parseInt(m.group(1));
                int y = Integer.parseInt(m.group(3));
                int z = Integer.parseInt(m.group(5));
                return new BlockPos(x,y,z);

            }
            catch(NumberFormatException e)
            {
                return new BlockPos(0,0,0);
            }
        }
        return null;
    }

    public static String BlockPosToString(BlockPos pos)
    {
        return ""+pos.getX()+","+pos.getY()+","+pos.getZ();
    }

    public static String VectorToString(Vec3d vec)
    {
        return ""+vec.xCoord+","+vec.yCoord+","+vec.zCoord;
    }

    public static Vec3d parseVector(Object o)
    {
        if(o instanceof Vec3d)
        {
            return (Vec3d)o;
        }
        else if(o instanceof String)
        {
            Pattern p = Pattern.compile("^(-?\\d+(\\.\\d)?)[ ,]+(-?\\d+(\\.\\d)?)[ ,]+(-?\\d+(\\.\\d)?)");
            Matcher m = p.matcher((String)o);
            try {
                m.find();
                int x = Integer.parseInt(m.group(1));
                int y = Integer.parseInt(m.group(3));
                int z = Integer.parseInt(m.group(5));
                return new Vec3d(x,y,z);

            }
            catch(NumberFormatException e)
            {
                return new Vec3d(0,1,0);
            }
        }
        return null;
    }



}

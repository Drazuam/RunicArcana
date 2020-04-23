package com.latenighters.runicarcana.common.symbols.backend;

import com.latenighters.runicarcana.common.symbols.SymbolTextures;
import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public abstract class Symbol extends net.minecraftforge.registries.ForgeRegistryEntry<Symbol> {

    protected String name;
    protected ResourceLocation texture;
    protected int id = -1;
    protected final SymbolCategory category;

    protected List<IFunctional> functions = new ArrayList<IFunctional>();
    protected List<HashableTuple<String,DataType>> inputs = new ArrayList<HashableTuple<String,DataType>>();
    protected List<IFunctional> outputs = new ArrayList<IFunctional>();
    protected List<HashableTuple<HashableTuple<String, DataType>, IFunctional>> triggers = new ArrayList<HashableTuple<HashableTuple<String, DataType>, IFunctional>>();

    public Symbol(String name, ResourceLocation texture, SymbolCategory category) {
        this.name = name;
        this.texture = texture;
        this.category = category;
        this.setRegistryName(new ResourceLocation(MODID, this.name));

        this.registerFunctions();

        //roll up the functions into the functional object fields
        for (IFunctional function : functions)
        {
            //synchronize the inputs from all the functions
            for(HashableTuple<String,DataType> input : inputs) {
                //check to see if inputs already has the same DataType and String registered
                function.getRequiredInputs().removeIf(finput ->
                {
                    if (function.getRequiredInputs().contains(finput)) return false;
                    if (input.getA().equals(finput.getA()) && input.getB().type.equals(finput.getB().type)) {
                        //if we do, remove the input from the function and replace it with the already registered input
                        function.getRequiredInputs().add(input);
                        return true;
                    }
                    return false;
                });
            }

            //add each input into the object inputs
            function.getRequiredInputs().forEach(finput->{
                if(!inputs.contains(finput))
                    inputs.add(finput);
            });
        }
    }

    public Symbol(String name)
    {
        this(name, SymbolTextures.DEBUG, SymbolCategory.DEFAULT);
    }

    public String getName()
    {
        return name;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    //we really shouldn't be using this
    public void onTick(DrawnSymbol symbol, World world, Chunk chunk, BlockPos drawnOn, Direction blockFace) {

    }

    protected abstract void registerFunctions();

    public List<IFunctional> getFunctions() {
        return functions;
    }

    public List<HashableTuple<String,DataType>> getInputs(){
        return inputs;
    }

    public List<IFunctional> getOutputs() {
        return outputs;
    }

    public List<HashableTuple<HashableTuple<String, DataType>, IFunctional>> getTriggers()
    {
        return triggers;
    }

    public static class DummySymbol extends Symbol{
        public DummySymbol(String name) {
            super(name);
        }

        @Override
        protected void registerFunctions() {

        }
    }

    public SymbolCategory getCategory()
    {
        return category;
    }

    public int getId() {
        return id;
    }

    //put DrawnSymbol data into contents of packet buffer
    public void encode(final DrawnSymbol symbol, final PacketBuffer buf)
    {
        buf.writeInt(symbol.blockFace.getIndex());
        buf.writeInt(symbol.drawnOn.getX());
        buf.writeInt(symbol.drawnOn.getY());
        buf.writeInt(symbol.drawnOn.getZ());
    }

    //take contents of packet buffer and put into DrawnSymbol
    public void decode(final DrawnSymbol symbol, final PacketBuffer buf)
    {
        symbol.blockFace = Direction.byIndex(buf.readInt());
        symbol.drawnOn = new BlockPos(buf.readInt(),buf.readInt(),buf.readInt());
    }



}

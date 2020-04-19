package com.latenighters.runicarcana.common.symbols.backend;

import com.latenighters.runicarcana.common.symbols.SymbolTextures;
import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public abstract class Symbol extends net.minecraftforge.registries.ForgeRegistryEntry<Symbol> {

    protected String name;
    protected ResourceLocation texture;
    protected int id = -1;
    protected final SymbolCategory category;

    public Symbol(String name, ResourceLocation texture, SymbolCategory category) {
        this.name = name;
        this.texture = texture;
        this.category = category;
        this.setRegistryName(new ResourceLocation(MODID, this.name));
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

    public void onTick(DrawnSymbol symbol, World world, Chunk chunk, BlockPos drawnOn, Direction blockFace) {

    }

    public static class DummySymbol extends Symbol{
        public DummySymbol(String name) {
            super(name);
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

package com.latenighters.runicarcana.common.symbols;

import net.minecraft.util.ResourceLocation;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class Symbol extends net.minecraftforge.registries.ForgeRegistryEntry<Symbol>{

    protected String name;
    protected ResourceLocation texture;

    public Symbol(String name, ResourceLocation texture) {
        this.name = name;
        this.texture = texture;
        this.setRegistryName(new ResourceLocation(MODID, this.name));
    }

    public Symbol(String name)
    {
        this(name, SymbolTextures.DEBUG);
    }

    public String getName()
    {
        return name;
    }

    public static class DummySymbol extends Symbol{
        public DummySymbol(String name) {
            super(name);
        }
    }
}

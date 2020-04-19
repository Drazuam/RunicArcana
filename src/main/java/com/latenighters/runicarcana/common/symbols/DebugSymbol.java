package com.latenighters.runicarcana.common.symbols;

import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class DebugSymbol extends Symbol {
    public DebugSymbol() {
        super("symbol_debug", SymbolTextures.DEBUG, SymbolCategory.DEFAULT);
    }

}

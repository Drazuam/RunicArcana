package com.latenighters.runicarcana.common.symbols;

import com.latenighters.runicarcana.common.symbols.backend.Symbol;
import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;

public class DebugSymbol extends Symbol {
    public DebugSymbol() {
        super("symbol_debug", SymbolTextures.DEBUG, SymbolCategory.DEFAULT);
    }

}

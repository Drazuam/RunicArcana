package com.latenighters.runicarcana.common.symbols.categories;

import com.latenighters.runicarcana.common.symbols.backend.Symbol;
import com.latenighters.runicarcana.common.symbols.Symbols;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class SymbolCategory {

    public static SymbolCategory DEFAULT = new SymbolCategory(Symbols.DEBUG, MODID+".symbol.category.default");

    private Symbol displaySymbol;
    private final String unlocalizedName;

    public static void generateCategories()
    {
        //TODO figure out why static initialization isn't working
        DEFAULT.setDisplaySymbol(Symbols.EXPULSION);
    }

    public SymbolCategory(Symbol displaySymbol, String unlocalizedName) {
        this.displaySymbol = displaySymbol;
        this.unlocalizedName = unlocalizedName;
    }

    public Symbol getDisplaySymbol() {
        return displaySymbol;
    }

    public void setDisplaySymbol(Symbol displaySymbol) {
        this.displaySymbol = displaySymbol;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }
}

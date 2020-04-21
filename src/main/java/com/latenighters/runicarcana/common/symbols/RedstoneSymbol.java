package com.latenighters.runicarcana.common.symbols;

import com.latenighters.runicarcana.common.symbols.backend.*;
import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;
import net.minecraft.util.Tuple;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class RedstoneSymbol extends Symbol {

    public RedstoneSymbol() {
        super("symbol_redstone", SymbolTextures.REDSTONE, SymbolCategory.DEFAULT);
    }

    @Override
    protected void registerFunctions() {

        functions.add(new IFunctional() {
            @Override
            public String getName() {
                return "redstone_tick";
            }

            @Override
            public List<Tuple<String, DataType>> getRequiredInputs() {
                return new ArrayList<>();
            }

            @Override
            public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<Tuple<String, Object>> args) {

                if(chunk.getWorld().isRemote) return null;

                DrawnSymbol symbol = (DrawnSymbol)object;
                if(symbol.getTicksAlive()%20!=0) return null;

                int redstoneLevel =  chunk.getWorld().getRedstonePower(symbol.getDrawnOn(), symbol.getBlockFace());

                symbol.applyServerTorque(8*redstoneLevel, chunk);

                return null;
            }

            @Override
            public DataType getOutputType() {
                return null;
            }

            @Override
            public List<Tuple<String, DataType>> getTriggers() {
                return new ArrayList<>();
            }
        });

        outputs.add(new IFunctional() {
            @Override
            public String getName() {
                return "Redstone On";
            }

            @Override
            public List<Tuple<String, DataType>> getRequiredInputs() {
                return new ArrayList<>();
            }

            @Override
            public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<Tuple<String, Object>> args) {

                DrawnSymbol symbol = (DrawnSymbol)object;
                int redstoneLevel =  chunk.getWorld().getRedstonePower(symbol.getDrawnOn(), symbol.getBlockFace());
                Boolean retval = redstoneLevel > 0;
                return retval;
            }

            @Override
            public DataType getOutputType() {
                return DataType.BOOLEAN;
            }

            @Override
            public List<Tuple<String, DataType>> getTriggers() {
                return new ArrayList<>();
            }
        });

    }
}

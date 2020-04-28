package com.latenighters.runicarcana.common.symbols;

import com.latenighters.runicarcana.common.symbols.backend.*;
import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.Position;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InsertionSymbol extends Symbol {
    public InsertionSymbol() {
        super("symbol_insertion", SymbolTextures.INSERT, SymbolCategory.DEFAULT);
    }

    @Override
    protected void registerFunctions() {

        //put all Hashable Tuples
        HashableTuple<String,DataType> enableInput = new HashableTuple<>("Enabled",DataType.BOOLEAN);

        List<HashableTuple<String, DataType>> requiredInputs = new ArrayList<HashableTuple<String, DataType>>();
        requiredInputs.add(enableInput);

        this.functions.add(new IFunctional() {

            final List<HashableTuple<String, DataType>> requiredInputsFinal = requiredInputs;

            @Override
            public String getName() {
                return null;
            }

            @Override
            public List<HashableTuple<String, DataType>> getRequiredInputs() {
                return requiredInputsFinal;
            }

            @Override
            public String getOutputString(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args) {
                return "...";
            }

            @Override
            public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args) {

                return null;
            }

            @Override
            public DataType getOutputType() {
                return null;
            }

            @Override
            public List<HashableTuple<String, DataType>> getTriggers() {
                return null;
            }
        });
    }
}

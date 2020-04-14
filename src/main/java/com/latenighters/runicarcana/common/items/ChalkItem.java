package com.latenighters.runicarcana.common.items;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.capabilities.ISymbolHandler;
import com.latenighters.runicarcana.common.capabilities.SymbolHandler;
import com.latenighters.runicarcana.common.setup.ModSetup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.util.LazyOptional;

public class ChalkItem extends Item {

    public ChalkItem() {
        super(new Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {

        LazyOptional<ISymbolHandler> symbolOp = context.getWorld().getChunkAt(context.getPos()).getCapability(RunicArcana.SYMBOL_CAP);
        symbolOp.ifPresent(symbols -> {









        });


        return super.onItemUse(context);
    }
}

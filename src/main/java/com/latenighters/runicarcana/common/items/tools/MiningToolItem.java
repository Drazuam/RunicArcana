package com.latenighters.runicarcana.common.items.tools;

import net.minecraftforge.common.ToolType;

import java.util.Arrays;

public class MiningToolItem extends AbstractToolItem {


    public MiningToolItem() {
        super(Arrays.asList(ToolType.PICKAXE, ToolType.SHOVEL, ToolType.AXE));
    }


}

package com.latenighters.runicarcana.common.setup;

import com.latenighters.runicarcana.RunicArcana;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

    public class ModSetup {

    public static final ItemGroup ITEM_GROUP = new ItemGroup(RunicArcana.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registration.CHALK.get());
        }
    };
}

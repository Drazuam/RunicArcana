package com.drazuam.runicarcana.common.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by Joel on 2/18/2017.
 */
public class CreativeTabRunicArcana extends CreativeTabs
{
    public CreativeTabRunicArcana(int index, String label) {
        super(index, label);
    }

    @Override
    public Item getTabIconItem() {
        return Items.BOOK;
    }
}

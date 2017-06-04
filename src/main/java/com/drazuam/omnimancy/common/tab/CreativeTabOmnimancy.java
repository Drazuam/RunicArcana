package com.drazuam.omnimancy.common.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by Joel on 2/18/2017.
 */
public class CreativeTabOmnimancy extends CreativeTabs
{
    public CreativeTabOmnimancy(int index, String label) {
        super(index, label);
    }

    @Override
    public Item getTabIconItem() {
        return Items.BOOK;
    }
}

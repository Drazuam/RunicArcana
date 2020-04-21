package com.latenighters.runicarcana.common.items;

//IClickable is an item that can be right clicked in the GUI

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

public interface IClickable {

    //do the action - this is performed server side
    //returns true when behavior is as expected
    boolean onClick(PlayerEntity player, ItemStack itemStack, Container container, int slot);

}


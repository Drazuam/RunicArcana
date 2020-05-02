package com.latenighters.runicarcana.common.items;

import com.latenighters.runicarcana.common.setup.ModSetup;
import net.minecraft.entity.item.FireworkRocketEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class DebugItem extends Item {
    public DebugItem() {
        super(new Properties().group(ModSetup.ITEM_GROUP).maxStackSize(1));
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (playerIn.isElytraFlying()) {
            ItemStack rocketStack = new ItemStack(Items.FIREWORK_ROCKET, 1);
            if (!worldIn.isRemote) {
                worldIn.addEntity(new FireworkRocketEntity(worldIn, rocketStack, playerIn));
            }
            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}

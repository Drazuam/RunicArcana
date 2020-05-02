package com.latenighters.runicarcana.common.items.armor;

import com.latenighters.runicarcana.common.items.IClickable;
import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.FireworkRocketEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PrincipicChestplateItem extends AbstractPrincipicArmor implements IClickable {
    private static final ItemStack rocketStack = new ItemStack(Items.FIREWORK_ROCKET, 1);
    private static final int boostCooldown = 30;

    public PrincipicChestplateItem() {
        super(EquipmentSlotType.CHEST);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(isEnabled(stack))
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_chestplate.effect_enabled"));
        else
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_chestplate.effect_disabled"));
        tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_chestplate.boost"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(Util.loreStyle("lore.runicarcana.principic_chestplate"));
    }

    public static void checkTimerNBT(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if(nbt==null)
            nbt = new CompoundNBT();
        if(!nbt.contains("timer"))
            nbt.putInt("timer", 0);
        stack.setTag(nbt);
    }

    public static int getTimer(ItemStack stack){
        checkNBT(stack);
        return stack.getTag().getInt("timer");
    }

    public static void setTimer(ItemStack stack, Integer time){
        checkNBT(stack);
        CompoundNBT nbt = stack.getTag();
        nbt.putInt("timer", time);
        stack.setTag(nbt);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        // TODO: This breaks when you save the world until you land.
        if (player.isSteppingCarefully() && getTimer(stack) < player.ticksExisted && player.isElytraFlying()) {
            if (!world.isRemote){
                world.addEntity(new FireworkRocketEntity(world, rocketStack, player));
            }
            setTimer(stack, player.ticksExisted + boostCooldown);
        }
        if (!player.isElytraFlying() && player.onGround && getTimer(stack) != 0)
            setTimer(stack, 0);
    }

}

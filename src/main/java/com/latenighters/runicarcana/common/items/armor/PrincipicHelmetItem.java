package com.latenighters.runicarcana.common.items.armor;

import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PrincipicHelmetItem extends AbstractPrincipicArmor {

    public PrincipicHelmetItem() {
        super(EquipmentSlotType.HEAD);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(isEnabled(stack))
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_helmet.effect_enabled"));
        else
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_helmet.effect_disabled"));
        tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_helmet"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(Util.loreStyle("lore.runicarcana.principic_helmet"));
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        player.setAir(300);
    }
}

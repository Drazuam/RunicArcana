package com.latenighters.runicarcana.common.items.armor;

import com.latenighters.runicarcana.common.items.IClickable;
import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

//import top.theillusivec4.caelus.api.event.RenderElytraEvent;


public class PrincipicChestplateItem extends AbstractPrincipicArmor implements IClickable {

    public PrincipicChestplateItem() {
        super(EquipmentSlotType.CHEST);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(isEnabled(stack))
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_chestplate.effect_enabled"));
        else
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_chestplate.effect_disabled"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(Util.loreStyle("lore.runicarcana.principic_chestplate"));

    }

}

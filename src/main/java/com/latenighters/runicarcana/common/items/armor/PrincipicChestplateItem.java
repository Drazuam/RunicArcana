package com.latenighters.runicarcana.common.items.armor;

import com.latenighters.runicarcana.common.items.IClickable;
import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.caelus.api.CaelusAPI;
//import top.theillusivec4.caelus.api.event.RenderElytraEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static com.latenighters.runicarcana.RunicArcana.MODID;


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

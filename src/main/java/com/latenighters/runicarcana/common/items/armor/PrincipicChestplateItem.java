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
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
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

    public static AttributeModifier FLIGHT_MODIFIER = new AttributeModifier(
            UUID.fromString("1e59d018-c9b1-4152-a474-7d318cc41a00"), MODID + "flight_modifier", 1.0d,
            AttributeModifier.Operation.ADDITION);

//    public static void onRenderElytraEvent(RenderElytraEvent evt) {
//        evt.setRenderElytra(false);
//    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(isEnabled(stack))
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_chestplate.effect_enabled"));
        else
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_chestplate.effect_disabled"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(Util.loreStyle("lore.runicarcana.principic_chestplate"));

    }

    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent evt)
    {

        if (!(evt.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }

        if (evt.getSlot() != EquipmentSlotType.CHEST) {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity) evt.getEntity();
        ItemStack equipment = evt.getTo();
        IAttributeInstance attributeInstance = playerEntity.getAttribute(CaelusAPI.ELYTRA_FLIGHT);
        attributeInstance.removeModifier(FLIGHT_MODIFIER);

        if (equipment.getItem() instanceof PrincipicChestplateItem && isEnabled(equipment)) {
            attributeInstance.applyModifier(FLIGHT_MODIFIER);
        }
    }

}

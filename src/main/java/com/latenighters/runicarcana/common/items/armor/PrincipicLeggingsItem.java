package com.latenighters.runicarcana.common.items.armor;

import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static com.latenighters.runicarcana.RunicArcana.MODID;


@Mod.EventBusSubscriber
public class PrincipicLeggingsItem extends AbstractPrincipicArmor {

    private static final float LEGGINGS_FOV_MODIFIER = 1.15f;

    public PrincipicLeggingsItem() {
        super(EquipmentSlotType.LEGS);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(isEnabled(stack))
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_leggings.effect_enabled"));
        else
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_leggings.effect_disabled"));

        tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_leggings"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(Util.loreStyle("lore.runicarcana.principic_leggings"));
    }

    public static AttributeModifier SPEED_MODIFIER = new AttributeModifier(
            UUID.fromString("de861576-3578-42ed-bb87-2254446c80ec"), MODID + "speed_modifier", 0.3d,
            AttributeModifier.Operation.ADDITION);


    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent evt)
    {
        if (!(evt.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }

        if (evt.getSlot() != EquipmentSlotType.LEGS) {
            return;
        }


        PlayerEntity playerEntity = (PlayerEntity) evt.getEntity();
        ItemStack equipment = evt.getTo();
        IAttributeInstance attributeInstance = playerEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        attributeInstance.removeModifier(SPEED_MODIFIER);

        if (equipment.getItem() instanceof PrincipicLeggingsItem && ((PrincipicLeggingsItem) equipment.getItem()).isEnabled(equipment))
        {
            attributeInstance.applyModifier(SPEED_MODIFIER);
        }

    }

    @SubscribeEvent
    public static void onFovChange(FOVUpdateEvent evt)
    {
        for (ItemStack equip : Minecraft.getInstance().player.getEquipmentAndArmor())
        {
            if(equip.getItem() instanceof PrincipicLeggingsItem)
            {
                if (isEnabled(equip)) {
                    evt.setNewfov(LEGGINGS_FOV_MODIFIER);
                } else {
                    evt.setNewfov(Math.min(evt.getNewfov(), LEGGINGS_FOV_MODIFIER));
                }
            }
        }
    }
}

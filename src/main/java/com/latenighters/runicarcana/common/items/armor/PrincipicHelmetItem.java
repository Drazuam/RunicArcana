package com.latenighters.runicarcana.common.items.armor;

import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber
public class PrincipicHelmetItem extends AbstractPrincipicArmor {

    private static Effect night_vision = Effects.NIGHT_VISION;
    private static DataParameter<Integer> potionEffects = null;

    public PrincipicHelmetItem() {
        super(EquipmentSlotType.HEAD);
        onSetup();
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


    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if(player==null)return;
            if(player.getEquipmentAndArmor()==null)return;

            boolean isEnabled = false;
            for(ItemStack equipment : player.getEquipmentAndArmor())
            {
                if(equipment.getEquipmentSlot() == EquipmentSlotType.HEAD && equipment.getItem() instanceof PrincipicHelmetItem)
                {
                    isEnabled = true;
                    break;
                }
            }

            Entity entity = Minecraft.getInstance().getRenderViewEntity();
            if(isEnabled)
                entity = null;

            if (entity != null)
                entity.getDataManager().set(potionEffects, 0);
        }
    }

    public static void onSetup()
    {
        //Grab LivingEntity.POTION_EFFECTS
        Field potionEffectsField = ObfuscationReflectionHelper.findField(LivingEntity.class,"field_184633_f");
        if(potionEffectsField!=null)
        {
            potionEffectsField.setAccessible(true);
            try {
                potionEffects = (DataParameter<Integer>)potionEffectsField.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        player.setAir(300);
    }

    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent evt)
    {
        if (!(evt.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }

        if (evt.getSlot() != EquipmentSlotType.HEAD) {
            return;
        }

        PlayerEntity playerEntity = (PlayerEntity) evt.getEntity();
        ItemStack equipment = evt.getTo();

        if (!(equipment.getItem() instanceof PrincipicHelmetItem) || !((PrincipicHelmetItem) equipment.getItem()).isEnabled(equipment))
        {
            playerEntity.removePotionEffect(night_vision);
            if(Minecraft.getInstance().world != null) {
                Entity entity = Minecraft.getInstance().getRenderViewEntity();
                if(entity!=null) {
                    Collection<EffectInstance> effects = ((LivingEntity)entity).getActivePotionEffects();
                    if (!effects.isEmpty())
                        entity.getDataManager().set(potionEffects, PotionUtils.getPotionColorFromEffectList(effects));
                }
            }
        }
        else if((equipment.getItem() instanceof PrincipicHelmetItem) && ((PrincipicHelmetItem) equipment.getItem()).isEnabled(equipment))
        {
            playerEntity.addPotionEffect(new EffectInstance(night_vision, Integer.MAX_VALUE));
            if(Minecraft.getInstance().world != null) {
                Entity entity = Minecraft.getInstance().getRenderViewEntity();
                if(entity!=null) {
                    entity.getDataManager().set(potionEffects, 0);
                }
            }
        }

    }

}

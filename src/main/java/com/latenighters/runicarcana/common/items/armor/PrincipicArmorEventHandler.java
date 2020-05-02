package com.latenighters.runicarcana.common.items.armor;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.network.NetworkSync;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import top.theillusivec4.caelus.api.CaelusAPI;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.UUID;

import static com.latenighters.runicarcana.RunicArcana.MODID;
import static com.latenighters.runicarcana.common.items.armor.AbstractPrincipicArmor.isEnabled;

@Mod.EventBusSubscriber
public class PrincipicArmorEventHandler {

    private static final float STEP_ASSIST_HEIGHT  = 1.0f;
    private static final float DEFAULT_STEP_HEIGHT = 0.6f;
    private static final float LEGGINGS_FOV_MODIFIER = 1.15f;

    public static AttributeModifier FLIGHT_MODIFIER = new AttributeModifier(
            UUID.fromString("1e59d018-c9b1-4152-a474-7d318cc41a00"), MODID + "flight_modifier", 1.0d,
            AttributeModifier.Operation.ADDITION);

    private static Effect night_vision = Effects.NIGHT_VISION;
    private static DataParameter<Integer> potionEffects = null;

    public static AttributeModifier SPEED_MODIFIER = new AttributeModifier(
            UUID.fromString("de861576-3578-42ed-bb87-2254446c80ec"), MODID + "speed_modifier", 0.3d,
            AttributeModifier.Operation.ADDITION);

    @SubscribeEvent
    public static void onFeetEquipmentChange(LivingEquipmentChangeEvent evt) {

        if (!(evt.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }

        if (evt.getSlot() != EquipmentSlotType.FEET) {
            return;
        }

        ServerPlayerEntity playerEntity = (ServerPlayerEntity) evt.getEntity();
        ItemStack equipment = evt.getTo();

        if(equipment.getItem() instanceof PrincipicBootsItem && isEnabled(equipment))
            playerEntity.stepHeight = STEP_ASSIST_HEIGHT;
        else
            playerEntity.stepHeight = DEFAULT_STEP_HEIGHT;

        NetworkSync.INSTANCE.sendTo(new PrincipicBootsItem.StepSyncMessage(playerEntity.stepHeight), playerEntity.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    @SubscribeEvent
    public static void onChestEquipmentChange(LivingEquipmentChangeEvent evt)
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

    @SubscribeEvent
    public static void onHelmetEquipmentChange(LivingEquipmentChangeEvent evt)
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
            if(RunicArcana.proxy.getWorld() != null) {
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
            if(RunicArcana.proxy.getWorld() != null) {
                Entity entity = Minecraft.getInstance().getRenderViewEntity();
                if(entity!=null) {
                    entity.getDataManager().set(potionEffects, 0);
                }
            }
        }

    }

    public static void onSetup(FMLLoadCompleteEvent event)
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

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientTickEvent(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START) {
            PlayerEntity player = RunicArcana.proxy.getPlayer();
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

    @SubscribeEvent
    public static void onLeggingsEquipmentChange(LivingEquipmentChangeEvent evt)
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
    @OnlyIn(Dist.CLIENT)
    public static void onFovChange(FOVUpdateEvent evt)
    {
        for (ItemStack equip : RunicArcana.proxy.getPlayer().getEquipmentAndArmor())
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

package com.latenighters.runicarcana.common.items.tools;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnull;

public class ArcanaMaterial implements IItemTier, IArmorMaterial {
    public static final ArcanaMaterial instance = new ArcanaMaterial();

    public int getItemEnchantability(){
        return 0;
    }

    public Ingredient getItemRepairMaterial(){
        return Ingredient.EMPTY;
    };

    @Override
    public int getDurability(EquipmentSlotType slotIn) {
        return 0;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return 0;
    }

    @Override
    public int getMaxUses() {
        return 0;
    }

    @Override
    public float getEfficiency() {
        return 0;
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getHarvestLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return getItemEnchantability();
    }

    @Override
    public SoundEvent getSoundEvent() {
        return null;
    }

    @Nonnull
    @Override
    public Ingredient getRepairMaterial() {
        return getItemRepairMaterial();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public float getToughness() {
        return 0;
    }
}

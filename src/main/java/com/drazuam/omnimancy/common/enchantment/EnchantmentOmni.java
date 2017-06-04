package com.drazuam.omnimancy.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * Created by Joel on 2/26/2017.
 */
public class EnchantmentOmni extends Enchantment{

    protected EnchantmentOmni(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
    {
        super(rarityIn, EnumEnchantmentType.ALL, slots);
        this.setName("omnienchant");
    }


    //dont let any other enchantments be put on together with the omnienchant
    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return false;
    }


    //for now, you shouldn't be able to get the enchantment on books
    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 8;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public Rarity getRarity() {
        return super.getRarity();
    }
}

package com.drazuam.omnimancy.common.enchantment;

import com.drazuam.omnimancy.common.Omnimancy;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Joel on 2/27/2017.
 */
public class ModEnchantment {

    public static Enchantment omnienchantment;

    public static void registerEnchantments()
    {
        omnienchantment = new EnchantmentOmni(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND);

        GameRegistry.register(omnienchantment, new ResourceLocation(Omnimancy.MODID, omnienchantment.getName()));

    }


}

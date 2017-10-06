package com.drazuam.runicarcana.common.enchantment;

import com.drazuam.runicarcana.common.RunicArcana;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Joel on 2/27/2017.
 */
public class ModEnchantment {

    public static Enchantment runicenchantment;

    public static void registerEnchantments()
    {
        runicenchantment = new EnchantmentOmni(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND);

        GameRegistry.register(runicenchantment, new ResourceLocation(RunicArcana.MODID, runicenchantment.getName()));

    }


}

package com.latenighters.runicarcana.common.items.armor;

import com.latenighters.runicarcana.common.items.IClickable;
import com.latenighters.runicarcana.common.setup.ModSetup;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractPrincipicArmor extends ArmorItem implements IClickable {

    private static final IArmorMaterial principic =
            new IArmorMaterial() {
                @Override
                public int getDurability(@Nonnull EquipmentSlotType slotIn) {
                    return -1;
                }

                @Override
                public int getDamageReductionAmount(@Nonnull EquipmentSlotType slotIn) {
                    return 1;
                }

                @Override
                public int getEnchantability() {
                    return 100;
                }

                @Override
                @Nonnull
                public SoundEvent getSoundEvent() {
                    return SoundEvents.ITEM_ARMOR_EQUIP_GOLD;
                }

                @Override
                @Nonnull
                public Ingredient getRepairMaterial() {
                    return Ingredient.EMPTY;
                }

                @Override
                public String getName() {
                    return "runicarcana:principic";
                }

                @Override
                public float getToughness() {
                    return 1;
                }
            };

    public AbstractPrincipicArmor(EquipmentSlotType slot) {
        super(
                principic,
                slot,
                new Properties()
                    .maxDamage(-1)
                    .maxStackSize(1)
                    .rarity(Rarity.EPIC)
                    .group(ModSetup.ITEM_GROUP)
        );

        DispenserBlock.registerDispenseBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);

        this.addPropertyOverride(new ResourceLocation("toggled"), (stack, world, livingEntity)
                -> AbstractPrincipicArmor.isEnabled(stack) ? 1.0f : 0.0f);

    }

    @Override
    public int getItemEnchantability() {
        return 15;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return this.getItemStackLimit(stack) == 1;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        return super.getDisplayName(stack).applyTextStyle(TextFormatting.GOLD);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("tooltip.runicarcana.principic_armor"));
    }

    public static void checkNBT(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if(nbt==null)
            nbt = new CompoundNBT();
        if(!nbt.contains("Enabled"))
            nbt.putBoolean("Enabled",true);
        stack.setTag(nbt);
    }

    public static boolean isEnabled(ItemStack stack){
        checkNBT(stack);
        return stack.getTag().getBoolean("Enabled");
    }

    public static void setEnabled(ItemStack stack, Boolean mode){
        checkNBT(stack);
        CompoundNBT nbt = stack.getTag();
        nbt.putBoolean("Enabled",mode);
        stack.setTag(nbt);
    }

    @Override
    public boolean onClick(PlayerEntity player, ItemStack itemStack, Container container, int slot) {
        setEnabled(itemStack, !isEnabled(itemStack));
        return true;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (playerIn.isSteppingCarefully()) {
            ItemStack armorItem = playerIn.getHeldItem(handIn);
            setEnabled(armorItem, !isEnabled(armorItem));
            return new ActionResult<>(ActionResultType.SUCCESS, armorItem);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}

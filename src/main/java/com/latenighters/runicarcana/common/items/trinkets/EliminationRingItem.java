package com.latenighters.runicarcana.common.items.trinkets;

import com.latenighters.runicarcana.capabilities.BasicCurioProvider;
import com.latenighters.runicarcana.common.setup.ModSetup;
import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EliminationRingItem extends Item {

    private static final int range = 10;

    public EliminationRingItem() {
        super(new Properties()
                .maxStackSize(1)
                .group(ModSetup.ITEM_GROUP)
        );
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(Util.tooltipStyle("tooltip.runicarcana.elimination_ring", range));
        tooltip.add(Util.loreStyle("lore.runicarcana.elimination_ring"));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new BasicCurioProvider(new ICurio() {

            @Override
            public void onCurioTick(String identifier, int index, LivingEntity livingEntity) {

                if (livingEntity.ticksExisted % 5 == 0) {
                    List<Entity> toKill = livingEntity.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(
                            livingEntity.getPosX()+range,livingEntity.getPosY()+range, livingEntity.getPosZ()+range,
                            livingEntity.getPosX()-range,livingEntity.getPosY()-range, livingEntity.getPosZ()-range));

                    for (Entity e:toKill) {
                        if (e instanceof IMob){
                            e.setFire(1);
                            ((MonsterEntity)e).setHealth(0);
                            ((MonsterEntity)e).spawnExplosionParticle();
                        }
                    }
                }
            }

            @Override
            public void playEquipSound(LivingEntity livingEntity) {
                livingEntity.world
                        .playSound(null, livingEntity.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
                                SoundCategory.NEUTRAL, 1.0f, 1.0f);
            }

            @Override
            @Nonnull
            public DropRule getDropRule(LivingEntity livingEntity) {
                return DropRule.ALWAYS_KEEP;
            }

            @Override
            public boolean canRightClickEquip() {
                return true;
            }
        }) {};
    }
}


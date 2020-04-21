package com.latenighters.runicarcana.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Random;

public class Util {

    public static ITextComponent loreStyle(ITextComponent textComponent){
        return textComponent.applyTextStyle(TextFormatting.DARK_PURPLE).applyTextStyle(TextFormatting.ITALIC);
    }

    public static ITextComponent loreStyle(String key, Object...args){
        return loreStyle(new TranslationTextComponent(key, args));
    }

    public static ITextComponent tooltipStyle(ITextComponent textComponent){
        return textComponent.applyTextStyle(TextFormatting.GRAY);
    }

    public static ITextComponent tooltipStyle(String key, Object... args){
        return tooltipStyle(new TranslationTextComponent(key, args));
    }

    public static void setPositionAndRotationAndUpdate(Entity entity, Double x, Double y, Double z, Float yaw, Float pitch) {
        entity.setPositionAndUpdate(x, y, z);
        entity.setPositionAndRotation(x, y, z, yaw, pitch);
    }

    public static void setPositionAndRotationAndUpdate(Entity entity, Double x, Double y, Double z){
        setPositionAndRotationAndUpdate(entity, x, y, z, entity.rotationYaw, entity.rotationPitch);
    }

    public static boolean checkHeadspace(World world, BlockPos pos){
        return world.isAirBlock(pos) && world.isAirBlock(pos.up());
    }

    public static void spawnParticleGroup(World world, BasicParticleType type, LivingEntity entity) {
        final Random rand = new Random();
        if (world.isRemote) {
            for (int i = 0; i < 30; ++i) {
                double d0 = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(
                        type,
                        entity.getPosX() + (double) (rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity.getWidth() - d0 * 10.0D,
                        entity.getPosY() + (double) (rand.nextFloat() * entity.getHeight()) - d1 * 10.0D,
                        entity.getPosZ() + (double) (rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity.getWidth() - d2 * 10.0D, d0, d1, d2
                );
            }

        }
    }

}

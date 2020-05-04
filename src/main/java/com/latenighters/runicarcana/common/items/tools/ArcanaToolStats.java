package com.latenighters.runicarcana.common.items.tools;

import net.minecraft.nbt.CompoundNBT;

public class ArcanaToolStats {
    private float attackDamage;
    private float attackSpeed;
//    private int harvestLevel; // canHarvestBlock only provides a block state, so you can't check the NBT to determine harvest level.
    private float destroySpeed;

    public ArcanaToolStats(float attackDamage, float attackSpeed, int harvestLevel, float destroySpeed) {
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.destroySpeed = destroySpeed;
    }

    public ArcanaToolStats(CompoundNBT statsNBT){
        float atkDamage = statsNBT.getFloat("atkDamage");
        float atkSpeed = statsNBT.getFloat("atkSpeed");
        int harvestLvl = statsNBT.getInt("harvestLvl");
        float destroySpd = statsNBT.getFloat("destroySpd");

        this.attackDamage = atkDamage;
        this.attackSpeed = atkSpeed;
        this.destroySpeed = destroySpd;
    }

    public CompoundNBT makeNBT(){
        CompoundNBT tag = new CompoundNBT();
        // Could be changed to float array to save packet bytes.
        tag.putFloat("atkDamage", this.attackDamage);
        tag.putFloat("atkSpeed", this.attackSpeed);
        tag.putFloat("destroySpd", this.destroySpeed);
        return tag;
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public float getDestroySpeed() {
        return destroySpeed;
    }

    public void setDestroySpeed(float destroySpeed) {
        this.destroySpeed = destroySpeed;
    }
}

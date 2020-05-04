package com.latenighters.runicarcana.common.items.tools;

import net.minecraft.nbt.CompoundNBT;

public class ArcanaToolStats {
    private float attackDamage;
    private float attackSpeed;
//    private int harvestLevel; // canHarvestBlock only provides a block state, so you can't check the NBT to determine harvest level.
    private float destroySpeed;
    private float toolRange; // <= 0 is default.

    public ArcanaToolStats(float attackDamage, float attackSpeed, float destroySpeed, float toolRange) {
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.destroySpeed = destroySpeed;
        this.toolRange = toolRange;
    }

    public ArcanaToolStats(CompoundNBT statsNBT){
        this.attackDamage = statsNBT.getFloat("atkDamage");
        this.attackSpeed = statsNBT.getFloat("atkSpeed");
        this.destroySpeed = statsNBT.getFloat("destroySpd");
        this.toolRange = statsNBT.getFloat("toolRange");
    }

    public CompoundNBT makeNBT(){
        CompoundNBT tag = new CompoundNBT();
        // Could be changed to float array to save packet bytes.
        tag.putFloat("atkDamage", this.attackDamage);
        tag.putFloat("atkSpeed", this.attackSpeed);
        tag.putFloat("destroySpd", this.destroySpeed);
        tag.putFloat("toolRange", this.toolRange);
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

    public float getToolRange() {
        return toolRange;
    }

    public void setToolRange(float toolRange) {
        this.toolRange = toolRange;
    }
}

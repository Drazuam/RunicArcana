package com.drazuam.runicarcana.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.world.World;

/**
 * Created by Joel on 10/8/2017.
 */
public class EntityLargeSettableFireball extends EntityLargeFireball {

    public EntityLargeSettableFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(worldIn);
        this.accelerationX = accelX;
        this.accelerationY = accelY;
        this.accelerationZ = accelZ;

        this.setPosition(x,y,z);
    }
}

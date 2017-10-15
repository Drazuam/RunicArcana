package com.drazuam.runicarcana.client.Particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created on 10/15/2017 by Matt
 */

public class EarthStrikeFX extends Particle
{
    private final ResourceLocation EarthStrikeFX = new ResourceLocation("runicarcana:particles/EarthStrikeFX");
    private final float damage;
    private final EntityPlayer caster;

    public EarthStrikeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float damage, EntityPlayer caster)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);

        this.particleGravity = 0.0F;
        this.particleMaxAge = 20;

        final float ALPHA_VALUE = 0.99F;
        this.particleAlpha = ALPHA_VALUE;

        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;

        this.damage = damage;
        this.caster = caster;

        this.multipleParticleScaleBy((float) (new Random().nextDouble() * 2.0D));

        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(EarthStrikeFX.toString());
        setParticleTexture(sprite);
    }

    @Override
    public int getFXLayer()
    {
        return 1;
    }

    @Override
    public boolean isTransparent()
    {

        return false;
    }

    @Override
    public int getBrightnessForRender(float p_189214_1_)
    {
        final int FULL_BRIGHTNESS_VALUE = 0xf000f0;
        return FULL_BRIGHTNESS_VALUE;
    }

    @Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        moveEntity(motionX, motionY, motionZ);

        AxisAlignedBB range = new AxisAlignedBB(posX - 0.5, posY, posZ - 0.5, posX + 0.5, posY + 0.5, posZ + 0.5);
        List<EntityLivingBase> mobsInRange = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, range);

        for (EntityLivingBase target : mobsInRange)
        {
            if (target != caster)
            {
                target.attackEntityFrom(DamageSource.magic, damage);
                this.isCollided = true;
                break;
            }
        }

        if (isCollided)
        {
            this.setExpired();
        }

        if (prevPosY == posY && motionY > 0)
        {
            this.setExpired();
        }

        if (this.particleMaxAge-- <= 0)
        {
            this.setExpired();
        }
    }

    @Override
    public void renderParticle(VertexBuffer vertexBuffer, Entity entity, float partialTick,
                               float edgeLRdirectionX, float edgeUDdirectionY, float edgeLRdirectionZ,
                               float edgeUDdirectionX, float edgeUDdirectionZ)
    {
        double minU = this.particleTexture.getMinU();
        double maxU = this.particleTexture.getMaxU();
        double minV = this.particleTexture.getMinV();
        double maxV = this.particleTexture.getMaxV();

        double scale = 0.1F * this.particleScale;  // vanilla scaling factor
        final double scaleLR = scale;
        final double scaleUD = scale;
        double x = this.prevPosX + (this.posX - this.prevPosX) * partialTick - interpPosX;
        double y = this.prevPosY + (this.posY - this.prevPosY) * partialTick - interpPosY;
        double z = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTick - interpPosZ;

        int combinedBrightness = this.getBrightnessForRender(partialTick);
        int skyLightTimes16 = combinedBrightness >> 16 & 65535;
        int blockLightTimes16 = combinedBrightness & 65535;

        vertexBuffer.pos(x - edgeLRdirectionX * scaleLR - edgeUDdirectionX * scaleUD, y - edgeUDdirectionY * scaleUD, z - edgeLRdirectionZ * scaleLR - edgeUDdirectionZ * scaleUD).tex(maxU, maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(skyLightTimes16, blockLightTimes16).endVertex();
        vertexBuffer.pos(x - edgeLRdirectionX * scaleLR + edgeUDdirectionX * scaleUD, y + edgeUDdirectionY * scaleUD, z - edgeLRdirectionZ * scaleLR + edgeUDdirectionZ * scaleUD).tex(maxU, minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(skyLightTimes16, blockLightTimes16).endVertex();
        vertexBuffer.pos(x + edgeLRdirectionX * scaleLR + edgeUDdirectionX * scaleUD, y + edgeUDdirectionY * scaleUD, z + edgeLRdirectionZ * scaleLR + edgeUDdirectionZ * scaleUD).tex(minU, minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(skyLightTimes16, blockLightTimes16).endVertex();
        vertexBuffer.pos(x + edgeLRdirectionX * scaleLR - edgeUDdirectionX * scaleUD, y - edgeUDdirectionY * scaleUD, z + edgeLRdirectionZ * scaleLR - edgeUDdirectionZ * scaleUD).tex(minU, maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(skyLightTimes16, blockLightTimes16).endVertex();

    }
}

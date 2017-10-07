package com.drazuam.runicarcana.client.enchantment;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.enchantment.DustIOSymbol;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.item.ModItems;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Created by Joel on 2/19/2017.
 */


@SideOnly(Side.CLIENT)
public class RendererDust extends TileEntitySpecialRenderer<TileEntityChalkBase> {

    @Override
    public void renderTileEntityAt(TileEntityChalkBase te, double x, double y, double z, float partialTicks, int destroyStage) {
        Minecraft mc = Minecraft.getMinecraft();
        Boolean playerIsHoldingChalk =  (mc.thePlayer.getHeldItem(EnumHand.MAIN_HAND)!=null&&mc.thePlayer.getHeldItem(EnumHand.MAIN_HAND).getItem()==ModItems.defaultChalkItem
                || mc.thePlayer.getHeldItem(EnumHand.OFF_HAND)!=null&&mc.thePlayer.getHeldItem(EnumHand.OFF_HAND).getItem()==ModItems.defaultChalkItem);

        for (DefaultDustSymbol dust: te.dustList) {
            GlStateManager.pushAttrib();
            GlStateManager.pushMatrix();

            //tessellator.getBuffer().setTranslation(x,y,z);
            // Translate to the location of our tile entity
            GlStateManager.translate(x, y, z);

            GL11.glEnable(GL11.GL_BLEND);
            GlStateManager.enableAlpha();
            //GlStateManager.color(0,255,0);

            //Do spinny shit
            GlStateManager.translate(0.5, 0, 0.5);
            GlStateManager.translate((float)(dust.x - 1)*(1.0F/3), 0, (float)(dust.z - 1)*(1.0F/3));
            GlStateManager.rotate((float)(180-dust.f*90),0,1,0);
            if (dust.getDustID() == ModDust.startSymbol.getDustID() && te.hasItem()) {
                long angle = (System.currentTimeMillis() / 60) % 360;
                //GlStateManager.translate(0, 0.07 * Math.sin(Math.PI * angle / 180), 0);
                GlStateManager.rotate(angle, 0, 1, 0);
            }

            GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

            RenderHelper.disableStandardItemLighting();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            } else {
                GlStateManager.shadeModel(GL11.GL_FLAT);

            }
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            World world = te.getWorld();
            // Translate back to local view coordinates so that we can do the acual rendering here

            Tessellator tessellator = Tessellator.getInstance();
            tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

            IBakedModel bakedModel = dust.getBakedModel();
            mc.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, bakedModel, world.getBlockState(te.getPos()), te.getPos(), Tessellator.getInstance().getBuffer(), false, Double.doubleToLongBits(Math.random()));
            //CHANGING COLORS
            //tessellator.getBuffer().putColorRGB_F4(255, 80, 255);
            tessellator.draw();

            //GL11.glDisable(GL11.GL_BLEND);
            //RenderHelper.enableStandardItemLighting();

            GlStateManager.popMatrix();


            for (DustIOSymbol dustIO: dust.ioDusts) {
                //GlStateManager.pushAttrib();
                GlStateManager.pushMatrix();

                //tessellator.getBuffer().setTranslation(x,y,z);
                // Translate to the location of our tile entity
                GlStateManager.translate(x, y, z);
                this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                //Do spinny shit
                GlStateManager.translate(0.5, 0, 0.5);
                GlStateManager.translate((float)(dustIO.x - 1)*(1.0F/3), 0, (float)(dustIO.z - 1)*(1.0F/3));
                GlStateManager.rotate((float)(180-dustIO.f*90),0,1,0);
                //if (dust.dustType == DustModelHandler.DustTypes.START && te.hasItem()) {
                    long angle = (System.currentTimeMillis() / 20) % 360;
                    //GlStateManager.translate(0, 0.07 * Math.sin(Math.PI * angle / 180), 0);
                    GlStateManager.rotate(-angle, 0, 1, 0);
                //}

                GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

                RenderHelper.disableStandardItemLighting();

                // Translate back to local view coordinates so that we can do the acual rendering here
                tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

                IBakedModel newbakedModel = dustIO.getBakedModel();
                mc.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, newbakedModel, world.getBlockState(te.getPos()), te.getPos(), Tessellator.getInstance().getBuffer(), false, Double.doubleToLongBits(Math.random()));
                //CHANGING COLORS
                Color color = new Color(dustIO.getSignal().type.color);
                tessellator.getBuffer().putColorRGB_F4(color.getRed()/255.0F,color.getGreen()/255.0F,color.getBlue()/255.0F);
                tessellator.draw();

                //GL11.glDisable(GL11.GL_BLEND);
                //RenderHelper.enableStandardItemLighting();

                GlStateManager.popMatrix();
                //GlStateManager.popAttrib();

                //render the iodust name
                double playerDist = Math.sqrt(Math.pow(mc.thePlayer.posX-te.getPos().getX()-0.5D,2)+ Math.pow(mc.thePlayer.posY-te.getPos().getY(),2)+ Math.pow(mc.thePlayer.posZ-te.getPos().getZ()-0.5D,2) );

                if(playerIsHoldingChalk&&mc.objectMouseOver.getBlockPos()!=null && mc.objectMouseOver.getBlockPos().distanceSq(te.getPos())<4.0D&&dustIO.getSignal()!=null&&playerDist<5.0D)
                {
                    //GlStateManager.pushAttrib();
                    GlStateManager.pushMatrix();

                    //double playerDist = Math.sqrt(Math.pow(mc.thePlayer.posX-te.getPos().getX(),2)+ Math.pow(mc.thePlayer.posY-te.getPos().getY(),2)+ Math.pow(mc.thePlayer.posZ-te.getPos().getZ(),2) );
                    float multiplier = (float)playerDist/120F;
                    //float scale = 0.45F*multiplier+0.004F;
                    float scale = 0.01F;
                    //tessellator.getBuffer().setTranslation(x,y,z);
                    // Translate to the location of our tile entity
                    GlStateManager.translate(x, y, z);

                    GL11.glEnable(GL11.GL_BLEND);
                    GlStateManager.enableAlpha();
                    //GlStateManager.color(0,255,0);

                    GlStateManager.translate(0.5, 0, 0.5);
                    GlStateManager.translate((float) (dustIO.x - 1) * (1.0F / 3), 0, (float) (dustIO.z - 1) * (1.0F / 3));
                    GlStateManager.translate(0,0.3,0);
                    GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                   // GL11.glRotatef(-90, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);

                    GL11.glScalef(-scale, -scale, scale);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDepthMask(false);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    int textWidth = 0;
                    String thisMessage = dustIO.getSignal().name;

                    int thisMessageWidth = mc.fontRendererObj.getStringWidth(thisMessage);
                    if (thisMessageWidth > textWidth)
                        textWidth = thisMessageWidth;

                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                    int stringMiddle = textWidth / 2;

                    tessellator.getBuffer().pos(-stringMiddle - 1, -1 + 0, 0.0D).color(0F,0F,0F,0.5F).endVertex();
                    tessellator.getBuffer().pos(-stringMiddle - 1, 8, 0.0D).color(0F,0F,0F,0.5F).endVertex();
                    tessellator.getBuffer().pos(stringMiddle + 1, 8, 0.0D).color(0F,0F,0F,0.5F).endVertex();
                    tessellator.getBuffer().pos(stringMiddle + 1, -1 + 0, 0.0D).color(0F,0F,0F,0.5F).endVertex();

                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);

                    mc.fontRendererObj.drawString(thisMessage, -textWidth / 2, 0, 0xFFFFFF);

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GlStateManager.popMatrix();
                    //GlStateManager.popAttrib();


                }

                dustIO.renderConnections(x, y, z);
            }
            dust.renderConnections(x, y, z);
            GlStateManager.popAttrib();
        }

        ItemStack stack = te.getItem();
        if (stack != null) {
            DefaultDustSymbol dust = te.getBiggestDust();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();
            // Translate to the center of the block and .9 points higher
            GlStateManager.translate(x, y, z);
            GlStateManager.translate(.5, .5, .5);
            if(dust!=null)
                GlStateManager.translate((float)(dust.x - 1)*(1.0F/3), 0, (float)(dust.z - 1)*(1.0F/3));
            GlStateManager.scale(.4f, .4f, .4f);
            long angle = (System.currentTimeMillis() / 25) % 360;
            GlStateManager.translate(0, 0.15 * Math.sin(Math.PI * angle / 90), 0);
            GlStateManager.rotate(-angle, 0, 1, 0);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }
}

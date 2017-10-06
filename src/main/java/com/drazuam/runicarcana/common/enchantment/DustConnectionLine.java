package com.drazuam.runicarcana.common.enchantment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Joel on 2/22/2017.
 */
public class DustConnectionLine implements Serializable{

    public DustIOSymbol parent;
    public DustIOSymbol child;
    public ModDust.ConnectionType type;
    public static final double width = 1.0D/6;
    float pX,pY,pZ;
    float cX,cY,cZ;
    double length;
    float angle;
    int dim;
    private double heading;

    public DustConnectionLine(DustIOSymbol newParent, DustIOSymbol newChild, ModDust.ConnectionType newType)
    {
        parent = newParent;
        child = newChild;
        type = newType;

        pX = parent.blockX+(parent.x*0.33F+0.33F);
        pY = parent.blockY;
        pZ = parent.blockZ+(parent.z*0.33F+0.33F);

        cX = child.blockX+(child.x*0.33F+0.33F);
        cY = child.blockY;
        cZ = child.blockZ+(child.z*0.33F+0.33F);

        length = Math.sqrt(Math.pow(pX-cX,2)+Math.pow(pY-cY,2)+Math.pow(pZ-cZ,2));

        dim = parent.getParent().getWorld().provider.getDimension();

        heading = Math.atan2(cZ-pZ,cX-pX)*180/Math.PI;


    }

    public void render( double x, double y, double z)
    {
        if(child==null)
        {
            parent.connectionLines.remove(this);
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        //if(dim!=Minecraft.getMinecraft().theWorld.provider.getDimension())return;
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.type.texture);
        Tessellator tessellator = Tessellator.getInstance();
        //tessellator.getBuffer().setTranslation(x,y,x);
        GlStateManager.translate(x,y,z);

        GL11.glEnable(GL11.GL_BLEND);
        GlStateManager.enableAlpha();

        //long angle = (System.currentTimeMillis() / 10) % 360;
        //GlStateManager.translate(0, 0.07 * Math.sin(Math.PI * angle / 180), 0);
        //GlStateManager.rotate(-angle, 1, 1, 1);

        GlStateManager.rotate(180, 1, 0, 0);


        RenderHelper.disableStandardItemLighting();

        GlStateManager.translate(0.33D*parent.x+0.1666,-0.08,-0.33D*parent.z-0.1666);
        GlStateManager.rotate(90+(float)heading, 0, 1, 0);


        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }
        //GlStateManager.translate(-(pX+cX)/2,-(pY+cY)/2+0.5,-(pZ+cZ)/2);

        Color newColor = new Color(this.parent.getSignal().type.color);

        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        int state = (int)(System.currentTimeMillis()/100)%4+1;
        tessellator.getBuffer().pos(width/2,0,0).tex(0.25D*state,length).color(newColor.getRed(),newColor.getGreen(),newColor.getBlue(),newColor.getAlpha()).endVertex();
        tessellator.getBuffer().pos(width/2,0,length).tex(0.25D*state,0).color(newColor.getRed(),newColor.getGreen(),newColor.getBlue(),newColor.getAlpha()).endVertex();
        tessellator.getBuffer().pos(-width/2,0,length).tex(0.25D*(state-1),0).color(newColor.getRed(),newColor.getGreen(),newColor.getBlue(),newColor.getAlpha()).endVertex();
        tessellator.getBuffer().pos(-width/2,0,0).tex(0.25D*(state-1),length).color(newColor.getRed(),newColor.getGreen(),newColor.getBlue(),newColor.getAlpha()).endVertex();
        tessellator.draw();

        GL11.glDisable(GL11.GL_BLEND);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }
}

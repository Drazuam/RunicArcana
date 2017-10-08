package com.drazuam.runicarcana.client.gui;

import com.drazuam.runicarcana.common.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * Created by Joel on 2/21/2017.
 */
public class SymbolButton extends GuiButton {

    private ResourceLocation texture;
    private int textureSize;
    private float[]colors = new float[3];

    public SymbolButton(int buttonId, int x, int y, int widthIn, int heightIn, ResourceLocation textureLocation, int size, Color color) {
        super(buttonId, x, y, widthIn, heightIn, "");
        texture = textureLocation;
        textureSize = size*32;
        color.getColorComponents(colors);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(this.texture);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            GlStateManager.pushMatrix();
            float scaleFactor = (float)0.1;
            {
                GlStateManager.scale(scaleFactor, scaleFactor, 1);

            }
            //GlStateManager.popMatrix();
            EntityPlayerSP playerEntity = Minecraft.getMinecraft().thePlayer;
            boolean selected = false;
            if(playerEntity.getHeldItem(EnumHand.MAIN_HAND)!=null&&playerEntity.getHeldItem(EnumHand.MAIN_HAND).getItem()== ModItems.defaultChalkItem)
            {
                ItemStack chalk = playerEntity.getHeldItem(EnumHand.MAIN_HAND);
                if(chalk.getTagCompound()!=null&&chalk.getTagCompound().getInteger("dustID")==this.id)
                    selected=true;
                if(chalk.getTagCompound()!=null&&chalk.getTagCompound().getInteger("catID")==(this.id))
                    selected=true;

            }
            else if(playerEntity.getHeldItem(EnumHand.OFF_HAND)!=null&&playerEntity.getHeldItem(EnumHand.OFF_HAND).getItem()== ModItems.defaultChalkItem) {
                ItemStack chalk = playerEntity.getHeldItem(EnumHand.OFF_HAND);
                if(chalk.getTagCompound()!=null&&chalk.getTagCompound().getInteger("dustID")==this.id)
                    selected=true;
                if(chalk.getTagCompound()!=null&&chalk.getTagCompound().getInteger("catID")==(this.id))
                    selected=true;
            }




            if(selected)
                GlStateManager.color(colors[0], colors[1], colors[2],1.0F);
            else
                GlStateManager.color(colors[0], colors[1], colors[2],0.5F);

            this.drawTexturedModalRect((float)this.xPosition/scaleFactor, (float)this.yPosition/scaleFactor, 0, 0, (int)(width/scaleFactor), (int)(width/scaleFactor));
            //this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
            GlStateManager.popMatrix();
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }


}

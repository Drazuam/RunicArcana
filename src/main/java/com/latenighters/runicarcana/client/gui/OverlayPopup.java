package com.latenighters.runicarcana.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class OverlayPopup extends Screen {

    private final int guiHeight = 80;
    private final int guiWidth  = 40;
    private final int headerSize = 5;
    private final int textLineSize = 20;
    private int numberOfTextItems = 3;

    private static final int X_OFFSET = 30;
    private static final int Y_OFFSET = -10;


    public OverlayPopup() {
        super(new TranslationTextComponent(MODID + ".popup_gui"));
    }


    public void render() {
        this.width = Minecraft.getInstance().getMainWindow().getScaledWidth();
        this.height = Minecraft.getInstance().getMainWindow().getScaledHeight();

        Minecraft.getInstance().getRenderManager().textureManager.bindTexture(GuiTextures.CHALK_POPUP);

        int xStart = (width-guiWidth)/2 + X_OFFSET;
        int yStart = ((height-guiHeight)/2 - Y_OFFSET);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.5F);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableAlphaTest();

        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        //render the top of the box
        Tessellator tessellator = new Tessellator();
        BufferBuilder builder = tessellator.getBuffer();

        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        builder.pos(xStart,yStart,0).tex(0,0).endVertex();
        builder.pos(xStart+guiWidth,yStart,0).tex(guiWidth,0).endVertex();
        builder.pos(xStart+guiWidth,yStart+guiHeight,0).tex(guiWidth,guiHeight).endVertex();
        builder.pos(xStart,yStart+guiHeight,0).tex(0,guiHeight).endVertex();

        tessellator.draw();


    }
}

package com.latenighters.runicarcana.client.gui;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.symbols.backend.*;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class OverlayPopup extends Screen {

    private final int guiHeight = 80;
    private final int guiWidth  = 40 + 20;
    private final int headerSize = 5;
    private final int textLineSize = 15;
    private int numberOfTextItems = 3;

    private static final int X_OFFSET = 30 + 10;
    private static final int Y_OFFSET = -10;

    public final AtomicReference<Tuple<String, DataType>> selectedFunction;
    public AtomicReference<IFunctionalObject> funcObject = new AtomicReference<>();
    private List<Tuple<String, DataType>> listToRender = new ArrayList<>();

    public OverlayPopup(AtomicReference<Tuple<String, DataType>> selectedFunction) {
        super(new TranslationTextComponent(MODID + ".popup_gui"));
        this.selectedFunction = selectedFunction;
    }


    public void render(float partialTicks, ItemStack chalk) {

        AtomicReference<IFunctionalObject> symbol = new AtomicReference<>();
        symbol.set(SymbolUtil.getLookedFunctionalObject());

        if(symbol.get()==null)
        {
            funcObject.set(null);
            selectedFunction.set(null);
        }
        else
        {
            this.width = Minecraft.getInstance().getMainWindow().getScaledWidth();
            this.height = Minecraft.getInstance().getMainWindow().getScaledHeight();

            numberOfTextItems = listToRender.size();

            double scaledGuiHeight = (guiHeight/Minecraft.getInstance().getMainWindow().getGuiScaleFactor());
            double scaledGuiWidth  = (guiWidth/Minecraft.getInstance().getMainWindow().getGuiScaleFactor());

            Minecraft.getInstance().getRenderManager().textureManager.bindTexture(GuiTextures.CHALK_POPUP);

            int xStart = (width-guiWidth)/2 + X_OFFSET;
            int yStart = ((height-guiHeight)/2 - Y_OFFSET);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableAlphaTest();

            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            //draw entire box
            //blit(xStart, yStart, 0,0,0,guiWidth,guiHeight,guiHeight,guiWidth);

            //draw header
            blit(xStart, yStart, 0,0,0,guiWidth,headerSize,guiHeight,guiWidth);

            //draw the inbetween
            blit(xStart, yStart+headerSize, 0,0,headerSize,guiWidth,textLineSize*numberOfTextItems,textLineSize*numberOfTextItems*2,guiWidth);

            //draw footer
            blit(xStart, yStart+textLineSize*numberOfTextItems+headerSize, 0,0,guiHeight-headerSize ,guiWidth,headerSize,guiHeight,guiWidth);

            //render the symbol functions

            //render the list of functions for the symbol.
            if(!(this.funcObject.get()==symbol.get())) {
                //if we currently have linking data in our chalk, then we need to render outputs
                listToRender.clear();
                if (chalk.getTag().contains("linking_from")) {
                    //render outputs
                    symbol.get().getOutputs().forEach(output -> {
                        listToRender.add(new Tuple<>(output.getName(), output.getOutputType()));
                    });
                } else {
                    //render inputs
                    listToRender.addAll(symbol.get().getInputs());
                }
                this.funcObject.set(symbol.get());
            }

            if(listToRender.size()<=0) return;

            //rely on the chalk item to keep this class updated with the correct selected function
            //if we don't have a selected function, choose the first.
            if(selectedFunction.get() == null)
                selectedFunction.set(listToRender.get(0));

            for(int i=0; i<listToRender.size(); i++)
            {
                if(listToRender.get(i) == selectedFunction.get())
                {
                    //render a nice little box for the selected function
//                    Minecraft.getInstance().getRenderManager().textureManager.bindTexture(GuiTextures.CHALK_SELECT);
//                    blit(xStart, yStart+textLineSize*i+headerSize,guiWidth, 1,0,0,textLineSize,textLineSize,guiWidth);
                    Minecraft.getInstance().fontRenderer.drawStringWithShadow(listToRender.get(i).getB().getShortName() + ":" +
                            listToRender.get(i).getA(),xStart+4,yStart+textLineSize*i+headerSize+1, TextFormatting.WHITE.getColor());
                }
                else
                {
                    Minecraft.getInstance().fontRenderer.drawStringWithShadow(listToRender.get(i).getB().getShortName() + ":" +
                            listToRender.get(i).getA(),xStart+4,yStart+textLineSize*i+headerSize+1, TextFormatting.GRAY.getColor());
                }


            }

        }


    }
}

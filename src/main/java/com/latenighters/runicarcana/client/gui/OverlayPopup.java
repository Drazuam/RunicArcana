package com.latenighters.runicarcana.client.gui;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.items.ChalkItem;
import com.latenighters.runicarcana.common.symbols.backend.*;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.latenighters.runicarcana.RunicArcana.MODID;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class OverlayPopup extends Screen {

    public static final OverlayPopup INSTANCE = new OverlayPopup();

    private final int guiHeight = 80;
    private final int guiWidth  = 40 + 20;
    private final int headerSize = 5;
    private final int textLineSize = 15;
    private int numberOfTextItems = 3;

    private static final int X_OFFSET = 30 + 10;
    private static final int Y_OFFSET = -10;

    public static AtomicReference<HashableTuple<String, DataType>> selectedFunction;
    public static AtomicReference<IFunctionalObject> funcObject = new AtomicReference<>();
    private static List<HashableTuple<String, DataType>> listToRender = new ArrayList<>();

    public OverlayPopup() {
        super(new TranslationTextComponent(MODID + ".popup_gui"));
        selectedFunction = new AtomicReference<>();
        ChalkItem.selectedFunction = selectedFunction;
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
                this.selectedFunction.set(null);
                updateRenderList(chalk,symbol.get());
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

    @SubscribeEvent
    public static void onScrollWheel(InputEvent.MouseScrollEvent event)
    {
        if((RunicArcana.proxy.getPlayer().getHeldItemMainhand().getItem() instanceof ChalkItem || RunicArcana.proxy.getPlayer().getHeldItemMainhand().getItem() instanceof ChalkItem)
                && RunicArcana.proxy.getPlayer().isSteppingCarefully() && funcObject.get()!=null){

            ItemStack chalk = RunicArcana.proxy.getPlayer().getHeldItemMainhand().getItem() instanceof ChalkItem ?
                    RunicArcana.proxy.getPlayer().getHeldItem(Hand.MAIN_HAND) : RunicArcana.proxy.getPlayer().getHeldItem(Hand.OFF_HAND);
            int indexMove = 0;
            if(event.getScrollDelta()>0)
                indexMove = -1;
            else
                indexMove = 1;

            if(funcObject.get()==null)return;
            if(selectedFunction.get()==null)return;

            if(chalk.getOrCreateTag().contains("linking_from"))
            {
                int prevIndex = 0;
                List<IFunctional> outputs = funcObject.get().getOutputs();
                for(int i=0; i<outputs.size(); i++){
                    if(outputs.get(i).getName().equals(selectedFunction.get().getA()) && outputs.get(i).getOutputType()==selectedFunction.get().getB())
                        prevIndex=i;
                }

                int newInd = (prevIndex + indexMove)%funcObject.get().getOutputs().size();
                if (newInd<0) newInd += funcObject.get().getOutputs().size();
                selectedFunction.set(new HashableTuple<>(outputs.get(newInd).getName(), outputs.get(newInd).getOutputType()));
            }
            else
            {
                int prevIndex = funcObject.get().getInputs().indexOf(selectedFunction.get());
                int newInd = (prevIndex + indexMove) % funcObject.get().getInputs().size();
                if (newInd<0) newInd += funcObject.get().getInputs().size();
                selectedFunction.set(funcObject.get().getInputs().get(newInd));
            }

            updateRenderList(chalk,funcObject.get());

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderEvent(RenderGameOverlayEvent.Post event)
    {
        if(event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS) return;
        if(RunicArcana.proxy.getPlayer().getHeldItemMainhand().getItem() instanceof ChalkItem)
            INSTANCE.render(event.getPartialTicks(), RunicArcana.proxy.getPlayer().getHeldItemMainhand());
        else if(RunicArcana.proxy.getPlayer().getHeldItemOffhand().getItem() instanceof ChalkItem)
            INSTANCE.render(event.getPartialTicks(), RunicArcana.proxy.getPlayer().getHeldItemOffhand());

    }

    public static void updateRenderList(ItemStack chalk, IFunctionalObject symbol) {
        listToRender.clear();
        if(symbol==null) return;
        if (chalk.getOrCreateTag().contains("linking_from")) {
            //render outputs
            symbol.getOutputs().forEach(output -> {
                if (selectedFunction.get() != null && selectedFunction.get().getA().equals(output.getName()) && selectedFunction.get().getB() == output.getOutputType())
                    listToRender.add(selectedFunction.get());
                else
                    listToRender.add(new HashableTuple<>(output.getName(), output.getOutputType()));

            });
        } else {
            //render inputs
            listToRender.addAll(symbol.getInputs());
        }
        funcObject.set(symbol);
    }
}

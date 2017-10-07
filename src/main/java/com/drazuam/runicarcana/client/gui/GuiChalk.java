package com.drazuam.runicarcana.client.gui;

import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.IDustSymbol;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.client.event.KeyEventHandler;
import com.drazuam.runicarcana.common.item.ModItems;
import com.drazuam.runicarcana.common.keybind.ModKeybind;
import com.drazuam.runicarcana.common.network.PacketHandler;
import com.drazuam.runicarcana.common.network.PacketSendDust;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joel on 2/21/2017.
 */
public class GuiChalk extends GuiScreen {


    private final ResourceLocation texture = new ResourceLocation(RunicArcana.MODID, "textures/gui/chalkGui.png");
    private final int guiHeight = 140;
    private final int guiWidth  = 200;
    private boolean shifting = false;



    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        int xStart = (width-guiWidth)/30;
        int yStart = (int)((height-guiHeight)*0.95);
        drawTexturedModalRect(xStart,yStart,0,0,guiWidth,guiHeight);

        super.drawScreen(mouseX, mouseY, partialTicks);
        for(GuiButton but: buttonList)
        {
            if(mouseX>but.xPosition&&mouseX<but.xPosition+but.width&&mouseY>but.yPosition&&mouseY<but.yPosition+but.height)
            {
                List<String> text = new ArrayList<String>();

                for(LinkedList<IDustSymbol> category : ModDust.dustRegistry)
                {
                    for(IDustSymbol dust : category)
                    {
                        if(dust.getDustID()==but.id)
                        {
                            text.add(dust.getDisplayName().getUnformattedText());
                            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                            {
                                //TODO: symbol descriptions
                            }
                        }
                    }
                }


                drawHoveringText(text,mouseX,mouseY);
            }
        }





    }


    @Override
    public void initGui(){
        buttonList.clear();
        addButtons();
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        for(LinkedList<IDustSymbol> category : ModDust.dustRegistry)
        {
            for(IDustSymbol dust : category)
            {
                if(button.id==dust.getDustID())
                {
                    //System.out.println("Selected "+dust.getDisplayName().getUnformattedText());
                    EntityPlayerSP playerEntity = Minecraft.getMinecraft().thePlayer;
                    if(playerEntity.getHeldItem(EnumHand.MAIN_HAND)!=null&&playerEntity.getHeldItem(EnumHand.MAIN_HAND).getItem()== ModItems.defaultChalkItem)
                    {
                        ItemStack chalk = playerEntity.getHeldItem(EnumHand.MAIN_HAND);
                        if(chalk.getTagCompound()==null){chalk.setTagCompound(new NBTTagCompound());}
                        chalk.getTagCompound().setInteger("dustID",button.id);
                        PacketHandler.INSTANCE.sendToServer(new PacketSendDust(button.id));

                    }
                    else if(playerEntity.getHeldItem(EnumHand.OFF_HAND)!=null&&playerEntity.getHeldItem(EnumHand.OFF_HAND).getItem()== ModItems.defaultChalkItem)
                    {
                        ItemStack chalk = playerEntity.getHeldItem(EnumHand.OFF_HAND);
                        if(chalk.getTagCompound()==null){chalk.setTagCompound(new NBTTagCompound());}
                        chalk.getTagCompound().setInteger("dustID",button.id);
                        PacketHandler.INSTANCE.sendToServer(new PacketSendDust(button.id));
                    }


                    return;
                }
            }
        }

        super.actionPerformed(button);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        KeyEventHandler.isChalkGuiOpen = false;
        super.onGuiClosed();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!GameSettings.isKeyDown(ModKeybind.keybindChalk))
        {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }




    public void addButtons()
    {
        int buttonWidth = (guiWidth/8);
        int buttonHeight = buttonWidth;
        int xStart  = (width-guiWidth)/30;
        int yStart  = (int)((height-guiHeight)*0.95);
        int xEnd    = (xStart+guiWidth)-buttonWidth;
        int yEnd    = (yStart+guiHeight);
        int xMargin = (guiWidth/20);
        int yMargin = (guiHeight/4);
        int xSpacing = (guiWidth/30);
        int ySpacing = (guiHeight/30);


        int x = xStart+xMargin;
        int y = yStart+yMargin;

        for(IDustSymbol dust : ModDust.dustRegistry.getFirst())
        {
        SymbolButton newButt = new SymbolButton(dust.getDustID(),x,y,buttonHeight,buttonWidth,dust.getResourceLocation(), dust.getSize());
            buttonList.add(newButt);
            x = x + xSpacing + buttonWidth;
            if(x>xEnd)
            {
                x = xStart+xMargin;
                y = y+ySpacing+buttonHeight;
            }
        }

    }



}



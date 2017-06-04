package com.drazuam.omnimancy.client.gui;

import com.drazuam.omnimancy.client.event.KeyEventHandler;
import com.drazuam.omnimancy.common.Omnimancy;
import com.drazuam.omnimancy.common.enchantment.DefaultDustSymbol;
import com.drazuam.omnimancy.common.enchantment.ModDust;
import com.drazuam.omnimancy.common.item.ModItems;
import com.drazuam.omnimancy.common.keybind.ModKeybind;
import com.drazuam.omnimancy.common.network.PacketHandler;
import com.drazuam.omnimancy.common.network.PacketSendDust;
import com.drazuam.omnimancy.common.network.PacketSendScrap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;
import scala.swing.event.KeyTyped;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joel on 2/21/2017.
 */
public class GuiPaperScrap extends GuiScreen {


    private final ResourceLocation texture = new ResourceLocation(Omnimancy.MODID, "textures/gui/paperScrapGui.png");
    private final int guiHeight = 50;
    private final int guiWidth  = 115;
    private final double scale = 1.5;
    private String text = "";
    private GuiButton doneBtn;


    public GuiPaperScrap(String newText) {
        super();
        this.text = newText;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        GlStateManager.scale(scale, scale, scale);

        int xStart = (width-(int)(guiWidth*scale))/2;
        int yStart = (int)((height-(int)(guiHeight*scale))*0.4);
        drawTexturedModalRect((int)(xStart/scale),(int)(yStart/scale),0,0,(guiWidth),(guiHeight));

        this.drawCenteredString(this.fontRendererObj, this.text, (int)(width/2/scale),(int)(height*0.4/scale), 0xFFFFFF);

        GlStateManager.scale(1/scale,1/scale,1/scale);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    public void initGui(){
        buttonList.clear();
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 30, this.height / 2+20,60,20, "Done"));
        super.initGui();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void onGuiClosed() {






        PacketHandler.INSTANCE.sendToServer(new PacketSendScrap(this.text));
        super.onGuiClosed();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 14 && !this.text.isEmpty())
        {
            this.text = this.text.substring(0, this.text.length() - 1);
        }

        if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && this.fontRendererObj.getStringWidth(this.text + typedChar) <= 90)
        {
            this.text = this.text + typedChar;
        }


        if (keyCode == 1 || keyCode== Keyboard.KEY_RETURN)
        {
            this.actionPerformed(this.doneBtn);
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }






}



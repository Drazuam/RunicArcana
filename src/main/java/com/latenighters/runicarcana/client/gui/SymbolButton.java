package com.latenighters.runicarcana.client.gui;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.items.ChalkItem;
import com.latenighters.runicarcana.common.symbols.backend.Symbol;
import com.latenighters.runicarcana.common.symbols.Symbols;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.RegistryManager;
import org.lwjgl.opengl.GL11;

public class SymbolButton extends Button {

    private final Symbol symbol;
    private final boolean is_category;

    public SymbolButton(Symbol symbol, int xIn, int yIn, int widthIn, int heightIn, IPressable onPress) {
        this(symbol, xIn, yIn, widthIn, heightIn, onPress, false);
    }

    public SymbolButton(Symbol symbol, int xIn, int yIn, int widthIn, int heightIn, IPressable onPress, boolean is_category ) {
        super(xIn, yIn, widthIn, heightIn, symbol.getName(), onPress);
        this.symbol = symbol;
        this.is_category = is_category;
    }

    @Override
    public void onPress() {
        super.onPress();
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {

        if(this.visible)
        {
            GlStateManager.bindTexture(Minecraft.getInstance().getRenderManager().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).getGlTextureId());
            TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(symbol.getTexture());

            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            PlayerEntity playerEntity = RunicArcana.proxy.getPlayer();
            ItemStack chalk;
            if(playerEntity.getHeldItem(Hand.MAIN_HAND)!=null&&playerEntity.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ChalkItem) {
                chalk = playerEntity.getHeldItem(Hand.MAIN_HAND);
            }
            else if(playerEntity.getHeldItem(Hand.MAIN_HAND)!=null&&playerEntity.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ChalkItem) {
                chalk = playerEntity.getHeldItem(Hand.MAIN_HAND);
            }
            else
            {
                Minecraft.getInstance().displayGuiScreen(null);
                return;
            }

            Symbol chalkSymbol = RegistryManager.ACTIVE.getRegistry(Symbol.class)
                    .getValue(new ResourceLocation(chalk.getOrCreateTag().contains("selected_symbol")
                            ? chalk.getOrCreateTag().getString("selected_symbol") : Symbols.DEBUG.getRegistryName().toString()));

            if(this.is_category)
            {
                if (symbol == chalkSymbol.getCategory().getDisplaySymbol())
                {
                    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                }else
                {
                    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.5F);
                }
            }
            else {
                if (symbol == chalkSymbol) {
                    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                }else
                {
                    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.5F);
                }
            }

            GlStateManager.pushMatrix();
            blit(this.x, this.y, 0, this.width, this.height, sprite);
            GlStateManager.popMatrix();
        }




        //super.renderButton(mouseX, mouseY, partialTicks);
    }

}

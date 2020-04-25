package com.latenighters.runicarcana.client.render;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.symbols.backend.capability.ISymbolHandler;
import com.latenighters.runicarcana.common.symbols.backend.DrawnSymbol;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.LazyOptional;
import org.lwjgl.opengl.GL11;

public class SymbolRenderer {

    //range to render symbols in chunks
    private static int symbol_render_range = 1;

    public static void renderSymbols(RenderWorldLastEvent evt)
    {


        PlayerEntity player = RunicArcana.proxy.getPlayer();
        if(player == null) return;
        Chunk homeChunk = player.world.getChunkAt(player.getPosition());

        Vec3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();

        MatrixStack matrix = evt.getMatrixStack();
//        matrix.push();


//        matrix.translate(-projectedView.x, -projectedView.y, -projectedView.z);
//        Matrix4f matrix4f = matrix.getLast().getMatrix();
//        RenderSystem.multMatrix(matrix4f);




        GlStateManager.pushMatrix();
        //Minecraft.getInstance().getRenderManager().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.bindTexture(Minecraft.getInstance().getRenderManager().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).getGlTextureId());


//        GlStateManager.translated(-projectedView.x, -projectedView.y, -projectedView.z);
        GlStateManager.rotatef(renderInfo.getPitch(), 1, 0, 0); // Fixes camera rotation.
        GlStateManager.rotatef(renderInfo.getYaw() + 180, 0, 1, 0); // Fixes camera rotation.
        GlStateManager.translated(-projectedView.x, -projectedView.y, -projectedView.z);

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlphaTest();
        GlStateManager.enableBlend();
//        GlStateManager.disableTexture();
        GlStateManager.enableLighting();

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//
//        RenderSystem.enableDepthTest();
//        RenderSystem.enableAlphaTest();
//        RenderSystem.enableLighting();


        LazyOptional<ISymbolHandler> symbolOp = homeChunk.getCapability(RunicArcana.SYMBOL_CAP);
        symbolOp.ifPresent(symbols -> {
            //TODO: move this to a chunk tick or something
            //symbols.clientSync(homeChunk.getPos());

            if(symbols.getSymbols().size()>0)
            {
                for(DrawnSymbol sym : symbols.getSymbols())
                {
                    GlStateManager.pushMatrix();
                    renderSymbol(sym, matrix);
                    GlStateManager.popMatrix();
                }
            }
        });

//        matrix.pop();
        GlStateManager.popMatrix();

    }

    public static void add(IVertexBuilder renderer, MatrixStack matrix, float x, float y, float z, float u, float v)
    {
        renderer.pos(matrix.getLast().getMatrix(),x,y,z)
                .color(1.0f, 1.0f, 1.0f, 1.0f)
                .tex(u,v)
                .lightmap(0,240)
                .normal(0, 1, 0)
                .endVertex();
    }

    public static void renderSymbol(DrawnSymbol symbol, MatrixStack matrix)
    {
        assert RunicArcana.proxy.getWorld() != null;


//        IRenderTypeBuffer buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
//        IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());
//        ResourceLocation textureToGrab = symbol.getTexture();
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(symbol.getTexture());

//
//        BlockPos pos = symbol.getDrawnOn();
//
//        switch (symbol.getBlockFace())
//        {
//            case UP:
//                add(builder,matrix,pos.getX(), pos.getY()+1.05F, pos.getZ(), sprite.getMinU(),sprite.getMinV());
//                add(builder,matrix,pos.getX(), pos.getY()+1.05F, pos.getZ()+1.05F, sprite.getMinU(),sprite.getMaxV());
//                add(builder,matrix,pos.getX()+1.05F, pos.getY()+1.05F, pos.getZ()+1.05F, sprite.getMaxU(),sprite.getMaxV());
//                add(builder,matrix,pos.getX()+1.05F, pos.getY()+1.05F, pos.getZ(), sprite.getMaxU(),sprite.getMinV());
//                break;
//            default:
//
//        }

//        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
//        ItemStack stack = new ItemStack(Items.DIAMOND);
//        IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(stack, tileEntity.getWorld(), null);
//        itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED, true, matrix, buffer, combinedLight, combinedOverlay, ibakedmodel);


        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
//        Minecraft.getInstance().getRenderManager().textureManager.bindTexture(symbol.getSymbol().getTexture());
        BlockPos pos = symbol.getDrawnOn();
        GlStateManager.translated(pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f);

        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        switch (symbol.getBlockFace())
        {
            case DOWN:
                GlStateManager.rotatef(180, 1, 0, 0);
                break;
            case EAST:
                GlStateManager.rotatef(-90, 0, 0, 1);
                break;
            case WEST:
                GlStateManager.rotatef(90, 0, 0, 1);
                break;
            case NORTH:
                GlStateManager.rotatef(-90, 1, 0, 0);
                break;
            case SOUTH:
                GlStateManager.rotatef(90, 1, 0, 0);
                break;
        }
        GlStateManager.rotatef(symbol.getWork()/10.0f,0,1,0);

        bufferBuilder.pos(-0.5, 0.51, -0.5).tex(sprite.getMinU(),sprite.getMinV()).normal(0, 1, 0).endVertex();
        bufferBuilder.pos(-0.5, 0.51,  0.5).tex(sprite.getMinU(),sprite.getMaxV()).normal(0, 1, 0).endVertex();
        bufferBuilder.pos( 0.5, 0.51,  0.5).tex(sprite.getMaxU(),sprite.getMaxV()).normal(0, 1, 0).endVertex();
        bufferBuilder.pos( 0.5, 0.51, -0.5).tex(sprite.getMaxU(),sprite.getMinV()).normal(0, 1, 0).endVertex();

        bufferBuilder.pos(-0.5, 0.49, -0.5).tex(sprite.getMinU(),sprite.getMinV()).normal(0, 1, 0).endVertex();
        bufferBuilder.pos( 0.5, 0.49, -0.5).tex(sprite.getMinU(),sprite.getMaxV()).normal(0, 1, 0).endVertex();
        bufferBuilder.pos( 0.5, 0.49,  0.5).tex(sprite.getMaxU(),sprite.getMaxV()).normal(0, 1, 0).endVertex();
        bufferBuilder.pos(-0.5, 0.49,  0.5).tex(sprite.getMaxU(),sprite.getMinV()).normal(0, 1, 0).endVertex();

        tessellator.draw();

//        GL11.glBegin(GL11.GL_QUADS);
//
//        BlockPos pos = symbol.getDrawnOn();
//
//        switch (symbol.getBlockFace())
//        {
//            case UP:
//
//                GL11.glVertex3d(pos.getX(), pos.getY() + 1, pos.getZ());
//                GL11.glVertex3d(pos.getX() + 1, pos.getY() + 1, pos.getZ());
//                GL11.glVertex3d(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
//                GL11.glVertex3d(pos.getX(), pos.getY() + 1, pos.getZ() + 1);
//                GL11.glNormal3d(0,-1,0);
//                GL11.glNormal3d(0,-1,0);
//                GL11.glNormal3d(0,-1,0);
//                GL11.glNormal3d(0,-1,0);
//
//                break;
//            case DOWN:
//
//                break;
//        }
//
//        GL11.glEnd();


    }
}

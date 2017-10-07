package com.drazuam.runicarcana.api.enchantment;

import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;


/**
 * Created by Joel on 2/19/2017.
 */
public interface IDustSymbol {

    public boolean checkOccupied(int checkX, int checkY);
    public IBakedModel getBakedModel();
    public boolean willAccept(ItemStack stack);
    public ITextComponent getDisplayName(String name);
    public IDustSymbol setXZFB(int newX, int newZ, int newF, int dim, BlockPos newBlockPos);
    public void renderConnections(double x, double y, double z);
    public short getDustID();
    public String getVariable();
    public DefaultDustSymbol setXZFB(int newX, int newZ, int newF, TileEntityChalkBase newParent);
    public Signal getSignal(int index);
    public ITextComponent getDisplayName();
    public ResourceLocation getResourceLocation();
    public int getSize();
    public String getModelLocation();
    public String getTexture();
    public String getDefaultName();

}

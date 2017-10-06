package com.drazuam.runicarcana.common.enchantment;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
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

}

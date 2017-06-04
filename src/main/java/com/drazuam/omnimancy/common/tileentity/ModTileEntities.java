package com.drazuam.omnimancy.common.tileentity;

import com.drazuam.omnimancy.common.Omnimancy;
import com.drazuam.omnimancy.client.enchantment.RendererDust;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Joel on 2/19/2017.
 */
public class ModTileEntities {


    public static void preInit()
    {
        registerTileEntities();

    }


    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityChalkBase.class, Omnimancy.MODID+":"+"TileEntityChalkBase");


    }
}

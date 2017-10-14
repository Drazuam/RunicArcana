package com.drazuam.runicarcana.common.tileentity;

import com.drazuam.runicarcana.common.RunicArcana;
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
        GameRegistry.registerTileEntity(TileEntityChalkBase.class, RunicArcana.MOD_ID +":"+"TileEntityChalkBase");


    }
}

package com.drazuam.omnimancy.client;

import com.drazuam.omnimancy.client.enchantment.RendererDust;
import com.drazuam.omnimancy.common.tileentity.TileEntityChalkBase;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Joel on 2/19/2017.
 */
public class ModRendering {

    public static void init()
    {
        registerSpecialRenderers();
    }

    public static void registerSpecialRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChalkBase.class, new RendererDust());
    }

}

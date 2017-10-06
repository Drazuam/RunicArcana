package com.drazuam.runicarcana.common.proxy;

import com.drazuam.runicarcana.client.ModRendering;
import com.drazuam.runicarcana.common.block.ModBlocks;
import com.drazuam.runicarcana.common.enchantment.ModDust;
import com.drazuam.runicarcana.common.event.ModEvents;
import com.drazuam.runicarcana.common.item.ModItems;
import com.drazuam.runicarcana.common.keybind.ModKeybind;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Joel on 2/18/2017.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ModEvents.initClient();
        ModKeybind.init();
        ModDust.registerDustsToGui();

    }

    @Override
    public void init(FMLInitializationEvent event) {

        ModItems.registerRenders();
        ModBlocks.registerRenders();
        ModRendering.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {



    }

}

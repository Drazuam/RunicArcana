package com.drazuam.omnimancy.common.proxy;

import com.drazuam.omnimancy.client.ModRendering;
import com.drazuam.omnimancy.common.block.ModBlocks;
import com.drazuam.omnimancy.common.enchantment.ModDust;
import com.drazuam.omnimancy.common.event.ModEvents;
import com.drazuam.omnimancy.common.item.ModItems;
import com.drazuam.omnimancy.common.keybind.ModKeybind;
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

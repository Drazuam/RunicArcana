package com.drazuam.omnimancy.common.event;

import com.drazuam.omnimancy.client.event.KeyEventHandler;
import com.drazuam.omnimancy.client.event.StitchEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Created by Joel on 2/21/2017.
 */
public class ModEvents {
    public static StitchEventHandler stitchEvent;
    public static KeyEventHandler keyEvent;
    public static ScriptActivationEventHandler rightClickEvent;

    public static void initClient()
    {
        stitchEvent = new StitchEventHandler();
        keyEvent    = new KeyEventHandler();

        RegisterEventsClient();
    }

    public static void initCommon()
    {
        rightClickEvent = new ScriptActivationEventHandler();
        RegisterEventsCommon();
    }

    public static void RegisterEventsCommon()
    {
        register(rightClickEvent);
    }


    public static void RegisterEventsClient()
    {
        register(stitchEvent);
        register(keyEvent);
    }

    public static void register(IEventHandler handle)
    {
        FMLCommonHandler.instance().bus().register(handle);
        MinecraftForge.EVENT_BUS.register(handle);

    }


}

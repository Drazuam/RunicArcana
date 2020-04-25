package com.latenighters.runicarcana;

import com.latenighters.runicarcana.client.event.ClientEventHandler;
import com.latenighters.runicarcana.client.event.KeyEventHandler;
import com.latenighters.runicarcana.common.items.armor.PrincipicArmorSubscriber;
import com.latenighters.runicarcana.common.symbols.backend.capability.ISymbolHandler;
import com.latenighters.runicarcana.common.symbols.backend.capability.SymbolHandler;
import com.latenighters.runicarcana.common.symbols.backend.capability.SymbolHandlerStorage;
import com.latenighters.runicarcana.common.symbols.backend.capability.SymbolSyncer;
import com.latenighters.runicarcana.common.event.CommonEventHandler;
import com.latenighters.runicarcana.common.setup.Registration;
import com.latenighters.runicarcana.common.symbols.backend.SymbolRegistration;
import com.latenighters.runicarcana.common.symbols.backend.SymbolRegistryHandler;
import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;
import com.latenighters.runicarcana.network.ClickableHandler;
import com.latenighters.runicarcana.network.NetworkSync;
import com.latenighters.runicarcana.proxy.ClientProxy;
import com.latenighters.runicarcana.proxy.IProxy;
import com.latenighters.runicarcana.proxy.ServerProxy;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

import static com.latenighters.runicarcana.common.symbols.backend.capability.SymbolSyncer.registerPackets;

@Mod("runicarcana")
public class RunicArcana
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "runicarcana";

    //Capability Registration
    @CapabilityInject(ISymbolHandler.class)
    public static Capability<ISymbolHandler> SYMBOL_CAP = null;

    public static IProxy proxy;

    public RunicArcana() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onSetupComplete);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doServerStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SymbolRegistryHandler::onCreateRegistryEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SymbolRegistration::registerSymbols);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
        MinecraftForge.EVENT_BUS.register(new SymbolSyncer());
        NetworkSync.registerPackets();
        ClickableHandler.registerPackets();
        Registration.init();
        //SymbolRegistration.init();

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        SymbolHandlerStorage storage = new SymbolHandlerStorage();
        SymbolHandler.SymbolHandlerFactory factory = new SymbolHandler.SymbolHandlerFactory();
        CapabilityManager.INSTANCE.register(ISymbolHandler.class, storage, factory);

        //TODO: register packets somewhere reasonable
        registerPackets();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        proxy = new ClientProxy();
        KeyEventHandler.registerKeyBindings();
    }

    private void doServerStuff(final FMLDedicatedServerSetupEvent event) {
         proxy = new ServerProxy();
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
//        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring").setSize(2));
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("trinket").setSize(2));

    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m->m.getMessageSupplier().get()).
//                collect(Collectors.toList()));
    }

    private void onSetupComplete(final FMLLoadCompleteEvent event)
    {
        PrincipicArmorSubscriber.onSetup(event);
        SymbolCategory.generateCategories();  //TODO remove this when static initialization is working
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
//        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
//            LOGGER.info("HELLO from Register Block");
        }
    }
}

package com.drazuam.runicarcana.common;

import com.drazuam.runicarcana.common.block.ModBlocks;
import com.drazuam.runicarcana.common.enchantment.ModEnchantment;
import com.drazuam.runicarcana.common.event.ModEvents;
import com.drazuam.runicarcana.common.item.ModItems;
import com.drazuam.runicarcana.common.network.PacketHandler;
import com.drazuam.runicarcana.common.recipes.ModRecipesVanilla;
import com.drazuam.runicarcana.common.tab.CreativeTabRunicArcana;
import com.drazuam.runicarcana.common.proxy.CommonProxy;
import com.drazuam.runicarcana.common.tileentity.ModTileEntities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = RunicArcana.MODID, version = RunicArcana.VERSION, name = RunicArcana.NAME)
public class RunicArcana
{
    public static final String MODID = "runicarcana";
    public static final String VERSION = "0.1";
    public static final String NAME = "Runic Arcana";

    @SidedProxy(clientSide = "com.drazuam.runicarcana.common.proxy.ClientProxy", serverSide = "com.drazuam.runicarcana.common.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static RunicArcana instance;

    public static CreativeTabRunicArcana creativeTabRunicArcana;



    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {


        creativeTabRunicArcana = new CreativeTabRunicArcana(CreativeTabs.getNextID(),"Runic Arcana");
        proxy.preInit(event);
        ModItems.preInit();
        ModBlocks.preInit();
        ModTileEntities.preInit();
        PacketHandler.registerMessages("chalkNBT");
        ModRecipesVanilla.registerRecipes();
        ModEnchantment.registerEnchantments();
        ModEvents.initCommon();

    }


    @EventHandler
    public void init(FMLInitializationEvent event)
    {

        proxy.init(event);

    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

        proxy.postInit(event);

    }

////    //@SideOnly(Side.CLIENT)
//    public void onTextureStitch(TextureStitchEvent.Pre event)
//    {
//        for(DustModelHandler.Textures tex: DustModelHandler.Textures.values())
//        {
//            System.out.println(event.getMap().registerSprite(new ResourceLocation(RunicArcana.MODID, tex.location)).toString());
//        }
//    }

}

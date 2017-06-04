package com.drazuam.omnimancy.common;

import com.drazuam.omnimancy.common.block.ModBlocks;
import com.drazuam.omnimancy.common.enchantment.ModEnchantment;
import com.drazuam.omnimancy.common.event.ModEvents;
import com.drazuam.omnimancy.common.item.ModItems;
import com.drazuam.omnimancy.common.network.PacketHandler;
import com.drazuam.omnimancy.common.recipes.ModRecipesVanilla;
import com.drazuam.omnimancy.common.tab.CreativeTabOmnimancy;
import com.drazuam.omnimancy.common.proxy.CommonProxy;
import com.drazuam.omnimancy.common.tileentity.ModTileEntities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Omnimancy.MODID, version = Omnimancy.VERSION, name = Omnimancy.NAME)
public class Omnimancy
{
    public static final String MODID = "omnimancy";
    public static final String VERSION = "proto-0.1";
    public static final String NAME = "Omnimancy";

    @SidedProxy(clientSide = "com.drazuam.omnimancy.common.proxy.ClientProxy", serverSide = "com.drazuam.omnimancy.common.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Omnimancy instance;

    public static CreativeTabOmnimancy creativeTabOmnimancy;



    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {


        creativeTabOmnimancy = new CreativeTabOmnimancy(CreativeTabs.getNextID(),"Omnimancy");
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
//            System.out.println(event.getMap().registerSprite(new ResourceLocation(Omnimancy.MODID, tex.location)).toString());
//        }
//    }

}

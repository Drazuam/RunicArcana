package com.latenighters.runicarcana.common.items;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.client.gui.OverlayPopup;
import com.latenighters.runicarcana.common.symbols.backend.*;
import com.latenighters.runicarcana.common.symbols.backend.capability.ISymbolHandler;
import com.latenighters.runicarcana.common.symbols.backend.capability.SymbolSyncer;
import com.latenighters.runicarcana.common.setup.ModSetup;
import com.latenighters.runicarcana.common.symbols.Symbols;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.RegistryManager;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class LensOfSoothItem extends Item {


    public LensOfSoothItem() {
        super(new Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        Chunk chunk = context.getWorld().getChunkAt(context.getPos());
        LazyOptional<ISymbolHandler> symbolOp = chunk.getCapability(RunicArcana.SYMBOL_CAP);
        symbolOp.ifPresent(symbols -> {

            if (!context.getWorld().isRemote) {
                IFunctionalObject symbol = SymbolUtil.getLookedFunctionalObject();

                if (symbol != null) {
                    HashableTuple<List<HashableTuple<String, Object>>, List<HashableTuple<String, Object>>> prevResolution = symbols.getPreviousResolution((DrawnSymbol) symbol, chunk);
                    List<HashableTuple<String, Object>> args = prevResolution.getA();
                    List<HashableTuple<String, Object>> outputs = prevResolution.getB();

                    StringBuilder sb = new StringBuilder("Inputs  |");
                    args.forEach(arg ->{
                        sb.append(String.format(" %s : %s,", arg.getA(),arg.getB()));
                    });
                    if (args.size() > 0) {
                        sb.setLength(sb.length() - 1);
                    }else{
                        sb.append(" None");
                    }
                    sb.append("\nOutputs |");
                    outputs.forEach(output ->{
                        sb.append(String.format(" %s: %s,", output.getA(),output.getB()));
                    });
                    if (outputs.size() > 0) {
                        sb.setLength(sb.length() - 1);
                    }else{
                        sb.append(" None");
                    }

                    context.getPlayer().sendStatusMessage(new TranslationTextComponent(sb.toString()), false);
                }
            }
        });
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    public void sendNBTToServer(IFunctionalObject symbol, ItemStack chalk, Chunk chunk)
    {

    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putString("selected_symbol",Symbols.DEBUG.getRegistryName().toString());
        super.onCreated(stack, worldIn, playerIn);
    }





}

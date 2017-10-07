package com.drazuam.runicarcana.common.event;

import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ModEnchantment;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.common.enchantment.Signals.CompiledSymbol;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Joel on 2/27/2017.
 */
public class ScriptActivationEventHandler implements IEventHandler {

    @SubscribeEvent
    public void rightClickEvent(PlayerInteractEvent.RightClickItem e)
    {
        EntityPlayer player = e.getEntityPlayer();
        ItemStack RunicItem = null;
        if(EnchantmentHelper.getEnchantmentLevel(ModEnchantment.runicenchantment, player.getHeldItem(EnumHand.MAIN_HAND) )>0)
            RunicItem = player.getHeldItem(EnumHand.MAIN_HAND);
        else if(EnchantmentHelper.getEnchantmentLevel(ModEnchantment.runicenchantment, player.getHeldItem(EnumHand.OFF_HAND) )>0)
            RunicItem = player.getHeldItem(EnumHand.OFF_HAND);
        else return;

        CompiledSymbol[] script = ModDust.getScriptFromItem(RunicItem);
        if(script==null)return;

        if(player.isSneaking())
            new ScriptExecutor(script, player, RunicItem, ScriptExecutor.StartPoint.SNEAK_RIGHT);
        else
            new ScriptExecutor(script, player, RunicItem, ScriptExecutor.StartPoint.RIGHT_CLICK);

    }

    @SubscribeEvent
    public void blockBreakEvent(BlockEvent.BreakEvent event)
    {
        EntityPlayer player = event.getPlayer();
        ItemStack OmniItem = null;
        if(EnchantmentHelper.getEnchantmentLevel(ModEnchantment.runicenchantment, player.getHeldItem(EnumHand.MAIN_HAND) )>0)
            OmniItem = player.getHeldItem(EnumHand.MAIN_HAND);
        else if(EnchantmentHelper.getEnchantmentLevel(ModEnchantment.runicenchantment, player.getHeldItem(EnumHand.OFF_HAND) )>0)
            OmniItem = player.getHeldItem(EnumHand.OFF_HAND);
        else return;

        CompiledSymbol[] script = ModDust.getScriptFromItem(OmniItem);
        if(script==null)return;

        new ScriptExecutor(script, player, OmniItem, ScriptExecutor.StartPoint.BLOCK_BREAK, event.getPos());
    }





}

package com.latenighters.runicarcana.common.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.ToolType;

import java.util.Collections;
import java.util.List;

public class AbstractToolItem extends ToolItem {

    static private final ArcanaToolStats defaultStats = new ArcanaToolStats(1f, 1f, 1f, 0f);
    static private final int harvestLevel = 3;
    private List<ToolType> toolTypes;

    public AbstractToolItem(List<ToolType> toolTypes) {
        super(defaultStats.getAttackDamage(), defaultStats.getAttackSpeed(), ArcanaMaterial.instance, Collections.emptySet(), new Properties());
        this.toolTypes = toolTypes;
    }

    public AbstractToolItem(ToolType toolType) {
        super(defaultStats.getAttackDamage(), defaultStats.getAttackSpeed(), ArcanaMaterial.instance, Collections.emptySet(), new Properties());
        this.toolTypes.add(toolType);
    }

    public static void checkNBT(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if(nbt==null)
            nbt = new CompoundNBT();
        if(!nbt.contains("Stats"))
            nbt.put("Stats", defaultStats.makeNBT());
        stack.setTag(nbt);
    }

    public static ArcanaToolStats getStats(ItemStack stack){
        checkNBT(stack);
        CompoundNBT nbt = (CompoundNBT) stack.getTag().get("Stats");
        return new ArcanaToolStats(nbt);
    }

    public static void setStats(ItemStack stack, ArcanaToolStats stats){
        checkNBT(stack);
        CompoundNBT nbt = stack.getTag();
        nbt.put("stats", stats.makeNBT());
        stack.setTag(nbt);
    }


    @Override
    public boolean canHarvestBlock(BlockState state) {
        ToolType harvestTool = state.getHarvestTool();
        if (toolTypes.contains(harvestTool)){
            if (harvestLevel >= state.getHarvestLevel()){
                return true;
            }
        }
        // Extra hardcoded shovel and pickaxe checks
        Block block = state.getBlock();
        if (toolTypes.contains(ToolType.SHOVEL) && (block == Blocks.SNOW || block == Blocks.SNOW_BLOCK)){
            return true;
        }

        Material material = state.getMaterial();
        return toolTypes.contains(ToolType.PICKAXE) && (material == Material.ROCK || material == Material.IRON || material == Material.ANVIL);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        boolean pick =
                toolTypes.contains(ToolType.PICKAXE) &&
                   material == Material.IRON
                || material == Material.ANVIL
                || material == Material.ROCK;
        boolean axe =
                toolTypes.contains(ToolType.AXE) &&
                   material == Material.WOOD
                || material == Material.PLANTS
                || material == Material.TALL_PLANTS
                || material == Material.BAMBOO;
        if (pick || axe || getToolTypes(stack).stream().anyMatch(state::isToolEffective)){ // Mek also uses effectiveBlocks.contains(state.getBlock()) but that's private so idk
            return getStats(stack).getDestroySpeed();
        } else {
            return 1;
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        // Encourages use of enhancement system.
        return false;
    }


}

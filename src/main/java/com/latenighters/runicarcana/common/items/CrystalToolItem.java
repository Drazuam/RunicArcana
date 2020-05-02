package com.latenighters.runicarcana.common.items;

import com.latenighters.runicarcana.common.arcana.ArcanaMachine;
import com.latenighters.runicarcana.common.setup.ModSetup;
import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CrystalToolItem extends Item {

    public CrystalToolItem() {
        super(new Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(stack.getOrCreateTag().contains("linking_from"))
        {
            BlockPos pos = NBTUtil.readBlockPos((CompoundNBT) stack.getTag().get("linking_from"));
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.linking_from").appendText(pos.toString()));

        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {

        if(context.getPlayer()==null)return super.onItemUse(context);

        TileEntity tile = context.getWorld().getTileEntity(context.getPos());
        if(tile instanceof ArcanaMachine) {

            if (context.getPlayer().isSteppingCarefully())
            {
                ((ArcanaMachine) tile).clearLinks();
                return ActionResultType.SUCCESS;
            }
            else {

                if (context.getItem().getOrCreateTag().contains("linking_from")) {
                    TileEntity tileFrom = context.getWorld().getTileEntity(NBTUtil.readBlockPos((CompoundNBT)
                            context.getItem().getTag().get("linking_from")));
                    if (tileFrom instanceof ArcanaMachine) {
                        ((ArcanaMachine) tileFrom).addExportLink(context.getItem().getTag().getInt("slot"),
                                context.getPos(), ((ArcanaMachine) tile).getChamberSlotFromHitVec(context.getHitVec()));
                    }

                    context.getItem().getTag().remove("linking_from");

                } else {
                    CompoundNBT nbt = NBTUtil.writeBlockPos(context.getPos());
                    nbt.putString("world", context.getWorld().dimension.toString());
                    nbt.putInt("slot", ((ArcanaMachine) tile).getChamberSlotFromHitVec(context.getHitVec()));

                    context.getItem().getTag().put("linking_from", nbt);
                }
                return ActionResultType.SUCCESS;
            }
        }

        return super.onItemUse(context);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        if(playerIn.isSteppingCarefully()){
            ItemStack chalk = playerIn.getHeldItem(handIn);
            chalk.getOrCreateTag().remove("linking_from");
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}

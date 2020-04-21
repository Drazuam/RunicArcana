package com.latenighters.runicarcana.common.items.trinkets;

import com.latenighters.runicarcana.capabilities.BasicCurioProvider;
import com.latenighters.runicarcana.common.items.IClickable;
import com.latenighters.runicarcana.common.setup.ModSetup;
import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class HearthstoneItem extends Item implements IClickable {
    public HearthstoneItem() {
        super(new Properties()
                .maxStackSize(1)
                .group(ModSetup.ITEM_GROUP)
        );
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        BlockPos pos = getStoredPos(stack);

        DimensionType storedDim = getStoredDim(stack);
        if (pos.getY() > 0 && storedDim != null){
            String dimString = DimensionType.getKey(storedDim).getPath();
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.hearthstone.set_block_pos", pos.getX(), pos.getY(), pos.getZ(), dimString));
        } else {
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.hearthstone.no_set_block_pos"));
        }

        tooltip.add(Util.loreStyle("lore.runicarcana.hearthstone"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new BasicCurioProvider(new ICurio() {

            @Override
            public void playEquipSound(LivingEntity livingEntity) {
                livingEntity.world
                        .playSound(null, livingEntity.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
                                SoundCategory.NEUTRAL, 1.0f, 1.0f);
            }

            @Override
            public DropRule getDropRule(LivingEntity livingEntity) {
                return ICurio.DropRule.ALWAYS_KEEP;
            }

            @Override
            public boolean canRightClickEquip() {
                return false;
            }
        }) {};
    }

    @Override
    public boolean onClick(PlayerEntity player, ItemStack itemStack, Container container, int slot) {
        teleportHome(player.world, player,itemStack);
        return true;
    }

    private static void checkNBT(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if(nbt==null)
            nbt = new CompoundNBT();
        if(!nbt.contains("StoredPos"))
            nbt.putIntArray("StoredPos", new int[]{0, -1, 0});
        if(!nbt.contains("StoredDim"))
            nbt.putString("StoredDim", "None");
        stack.setTag(nbt);
    }

    private static void setStoredPosAndDim(ItemStack stack, BlockPos pos, DimensionType dimension){
        // Maybe make this store player pitch and yaw as well?
        checkNBT(stack);
        CompoundNBT nbt = stack.getTag();

        assert nbt != null;
        nbt.putIntArray("StoredPos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
        ResourceLocation dimRL = DimensionType.getKey(dimension);
        if (dimRL != null){
            nbt.putString("StoredDim", dimRL.toString());
        }
        stack.setTag(nbt);

    }

    private static BlockPos getStoredPos(ItemStack stack) {
        checkNBT(stack);
        assert stack.getTag() != null;
        int[] posArray = stack.getTag().getIntArray("StoredPos");
        return new BlockPos(posArray[0], posArray[1], posArray[2]);
    }

    private static DimensionType getStoredDim(ItemStack stack) {
        checkNBT(stack);
        assert stack.getTag() != null;
        String dimString = stack.getTag().getString("StoredDim");
        if (dimString.equals("None"))
            return null;
        return DimensionType.byName(new ResourceLocation(dimString));
    }

    private void teleportHome(World world, LivingEntity livingEntity, ItemStack stack) {
        BlockPos pos = getStoredPos(stack);
        DimensionType dim = getStoredDim(stack);
        if (world.getDimension().getType() != dim){
            if (livingEntity instanceof PlayerEntity){
                ((PlayerEntity)livingEntity).sendStatusMessage(
                        new TranslationTextComponent("status.runicarcana.hearthstone.wrong_dimension"), true
                );
            }
        } else if (pos.getY() >= 0 && Util.checkHeadspace(world, pos.up())) {
            Util.setPositionAndRotationAndUpdate(livingEntity, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!playerIn.isSteppingCarefully()) {
            teleportHome(worldIn, playerIn, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getPlayer() != null && context.getPlayer().isSteppingCarefully()) {
            setStoredPosAndDim(context.getItem(), context.getPos(), context.getWorld().dimension.getType());
            if (context.getPlayer() != null){
                context.getPlayer().sendStatusMessage(new TranslationTextComponent(
                        "status.runicarcana.hearthstone.set_block_pos",
                        context.getPos().getX(),
                        context.getPos().getY(),
                        context.getPos().getZ()
                ), true);
            }
        } else {
            teleportHome(context.getWorld(), context.getPlayer(), context.getItem());
        }
        return super.onItemUse(context);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof PlayerEntity && !target.isSteppingCarefully()){
            ((PlayerEntity) target).sendStatusMessage(new TranslationTextComponent("status.runicarcana.hearthstone.player_not_sneaking"), true);
        } else {
            BlockPos pos = getStoredPos(stack);
            if (attacker.world.isRemote && attacker instanceof PlayerEntity){
                attacker.world.playSound((PlayerEntity)attacker, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 0.3f, 1f);
            }
            Util.setPositionAndRotationAndUpdate(target, pos.getX() + 0.5, (double) pos.getY(), pos.getZ() + 0.5);
            return true;
        }
        return false;
    }

}

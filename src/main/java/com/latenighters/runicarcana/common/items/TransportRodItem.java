package com.latenighters.runicarcana.common.items;

import com.latenighters.runicarcana.common.setup.ModSetup;
import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.latenighters.runicarcana.util.Util.checkHeadspace;

public class TransportRodItem extends Item {
    public static final float teleRange = 128.0f;
    public static final int maxBlocksSearched = 5;
    public static boolean didAltTeleport = false;

    public TransportRodItem() {
        super(new Properties()
                .maxStackSize(1)
                .group(ModSetup.ITEM_GROUP)
        );
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(Util.tooltipStyle("tooltip.principium.transport_rod.1"));
        tooltip.add(Util.tooltipStyle("tooltip.principium.transport_rod.2", (int) teleRange, maxBlocksSearched));
        tooltip.add(Util.tooltipStyle("tooltip.principium.transport_rod.3"));
        tooltip.add(Util.loreStyle("lore.principium.transport_rod"));
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!playerIn.isSteppingCarefully() && !didAltTeleport){
            Vec3d lookVec = playerIn.getLookVec();
            Vec3d start = new Vec3d(playerIn.getPosX(), playerIn.getPosY() + playerIn.getEyeHeight(), playerIn.getPosZ());
            Vec3d end = start.add(lookVec.x * teleRange, lookVec.y * teleRange, lookVec.z * teleRange);
            BlockRayTraceResult raytrace = worldIn.rayTraceBlocks(
                    new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, playerIn)
            );
            BlockPos pos = raytrace.getPos();

            for (int i=0; i < maxBlocksSearched; i++){
                // Check two blocks to insure no suffocation.
                BlockPos adjustedPos = new BlockPos(pos.getX(), pos.getY() + i, pos.getZ());
                if (checkHeadspace(worldIn, adjustedPos)) {
                    if (!worldIn.isRemote){
                        playerIn.fallDistance = 0f;
                        Util.setPositionAndRotationAndUpdate(
                            playerIn,
                            adjustedPos.getX() + 0.5,
                            (double) adjustedPos.getY(),
                            adjustedPos.getZ() + 0.5
                        );
                    } else {
                        worldIn.playSound(playerIn, adjustedPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 0.3f, 1f);
                        Util.spawnParticleGroup(worldIn, ParticleTypes.PORTAL, playerIn);
                    }
                    break;
                }
            }
        } else {
            didAltTeleport = false;
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        didAltTeleport = true;
        if (context.getPlayer() != null && context.getPlayer().isSteppingCarefully()) {
            BlockPos newPos;
            switch (context.getFace()) {
                case DOWN: newPos = new BlockPos(
                        context.getPos().getX(),
                        context.getPos().getY() + 1,
                        context.getPos().getZ()
                );
                break;
                case NORTH: newPos = new BlockPos(
                        context.getPos().getX(),
                        context.getPos().getY(),
                        context.getPos().getZ() + 1
                );
                break;
                case SOUTH: newPos = new BlockPos(
                        context.getPos().getX(),
                        context.getPos().getY(),
                        context.getPos().getZ() - 1
                );
                break;
                case WEST: newPos = new BlockPos(
                        context.getPos().getX() + 1,
                        context.getPos().getY(),
                        context.getPos().getZ()
                );
                break;
                case EAST: newPos = new BlockPos(
                        context.getPos().getX() - 1,
                        context.getPos().getY(),
                        context.getPos().getZ()
                );
                break;
                default: newPos = new BlockPos(
                        context.getPos().getX(),
                        context.getPos().getY() - 2, // -2 to allow for head room
                        context.getPos().getZ()
                );
                break;

            }
            if (context.getWorld().getBlockState(context.getPos()).getBlockHardness(context.getWorld(), context.getPos()) >= 0 && checkHeadspace(context.getWorld(), newPos)) {
                Util.setPositionAndRotationAndUpdate(context.getPlayer(), newPos.getX() + 0.5, (double) newPos.getY(), newPos.getZ() + 0.5);
                context.getWorld().playSound(context.getPlayer(), newPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 0.3f, 1f);
                return ActionResultType.SUCCESS;
            } else {
                didAltTeleport = false;
                return ActionResultType.FAIL;
            }
        } else if (context.getPlayer() != null){
            onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand());
        }
        return super.onItemUse(context);
    }


}

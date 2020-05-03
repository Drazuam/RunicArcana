package com.latenighters.runicarcana.common.items.armor;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.FireworkRocketEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class PrincipicBootsItem extends AbstractPrincipicArmor {

    private static final ItemStack rocketStack = new ItemStack(Items.FIREWORK_ROCKET, 1);
    private static final int boostCooldown = 30;


    public PrincipicBootsItem() {
        super(EquipmentSlotType.FEET);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(isEnabled(stack))
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_boots.effect_enabled"));
        else
            tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_boots.effect_disabled"));
        tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_boots"));
        tooltip.add(Util.tooltipStyle("tooltip.runicarcana.principic_boots.boost"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(Util.loreStyle("lore.runicarcana.principic_boots"));
    }



    public static class StepSyncMessage
    {
        float stepHeight;

        public StepSyncMessage(float stepHeight) {
            this.stepHeight = stepHeight;
        }

        public static void encode(final StepSyncMessage msg, final PacketBuffer buf)
        {
            buf.writeFloat(msg.stepHeight);
        }

        public static StepSyncMessage decode(final PacketBuffer buf)
        {
            return new StepSyncMessage(buf.readFloat());
        }

        public static void handle(final StepSyncMessage msg, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                context.enqueueWork(() -> {

                    RunicArcana.proxy.getPlayer().stepHeight = msg.stepHeight;

                    context.setPacketHandled(true);
                });
            }
        }
    }

    public static void checkTimerNBT(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if(nbt==null)
            nbt = new CompoundNBT();
        if(!nbt.contains("timer"))
            nbt.putLong("timer", 0L);
        stack.setTag(nbt);
    }

    public static long getTimer(ItemStack stack){
        checkNBT(stack);
        return stack.getTag().getLong("timer");
    }

    public static void setTimer(ItemStack stack, Long time){
        checkNBT(stack);
        CompoundNBT nbt = stack.getTag();
        nbt.putLong("timer", time);
        stack.setTag(nbt);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (player.isSteppingCarefully() && getTimer(stack) < world.getGameTime() && player.isElytraFlying()) {
            if (!world.isRemote){
                world.addEntity(new FireworkRocketEntity(world, rocketStack, player));
            }
            setTimer(stack, world.getGameTime() + boostCooldown);
        }
        if (!player.isElytraFlying() && player.onGround && getTimer(stack) != 0)
            setTimer(stack, 0L);
    }

}

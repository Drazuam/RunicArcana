package com.latenighters.runicarcana.common.items.armor;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.network.NetworkSync;
import com.latenighters.runicarcana.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class PrincipicBootsItem extends AbstractPrincipicArmor {

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
}

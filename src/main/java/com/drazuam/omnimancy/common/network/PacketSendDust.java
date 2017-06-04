package com.drazuam.omnimancy.common.network;

import com.drazuam.omnimancy.common.item.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Joel on 2/22/2017.
 */
public class PacketSendDust implements IMessage {
    private int dustID=-1;


    @Override
    public void fromBytes(ByteBuf buf) {
        dustID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dustID);
    }

    public PacketSendDust(){

    }

    public PacketSendDust(int newDustID) {

        dustID = newDustID;
    }

    public static class Handler implements IMessageHandler<PacketSendDust, IMessage> {
        @Override
        public IMessage onMessage(PacketSendDust message, MessageContext ctx) {
            // Always use a construct like this to actually handle your message. This ensures that
            // your 'handle' code is run on the main Minecraft thread. 'onMessage' itself
            // is called on the networking thread so it is not safe to do a lot of things
            // here.
            int breakP = 0;
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSendDust message, MessageContext ctx) {
            // This code is run on the server side. So you can do server-side calculations here
            EntityPlayerMP playerEntity = ctx.getServerHandler().playerEntity;
            if(playerEntity.getHeldItem(EnumHand.MAIN_HAND)!=null&&playerEntity.getHeldItem(EnumHand.MAIN_HAND).getItem()== ModItems.defaultChalkItem)
            {
                ItemStack chalk = playerEntity.getHeldItem(EnumHand.MAIN_HAND);
                if(chalk.getTagCompound()==null){chalk.setTagCompound(new NBTTagCompound());}
                chalk.getTagCompound().setInteger("dustID",message.dustID);

            }
            else if(playerEntity.getHeldItem(EnumHand.OFF_HAND)!=null&&playerEntity.getHeldItem(EnumHand.OFF_HAND).getItem()== ModItems.defaultChalkItem)
            {
                ItemStack chalk = playerEntity.getHeldItem(EnumHand.OFF_HAND);
                if(chalk.getTagCompound()==null){chalk.setTagCompound(new NBTTagCompound());}
                chalk.getTagCompound().setInteger("dustID",message.dustID);
            }
        }
    }
}

package com.drazuam.omnimancy.common.network;

import com.drazuam.omnimancy.common.item.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

/**
 * Created by Joel on 2/22/2017.
 */
public class PacketSendScrap implements IMessage {
    private String text="";


    @Override
    public void fromBytes(ByteBuf buf) {
        text =ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, text);

    }

    public PacketSendScrap(){

    }

    public PacketSendScrap(String newText) {

        text = newText;
    }

    public static class Handler implements IMessageHandler<PacketSendScrap, IMessage> {
        @Override
        public IMessage onMessage(PacketSendScrap message, MessageContext ctx) {
            // Always use a construct like this to actually handle your message. This ensures that
            // your 'handle' code is run on the main Minecraft thread. 'onMessage' itself
            // is called on the networking thread so it is not safe to do a lot of things
            // here.
            int breakP = 0;
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSendScrap message, MessageContext ctx) {
            // This code is run on the server side. So you can do server-side calculations here
            EntityPlayerMP playerEntity = ctx.getServerHandler().playerEntity;
            if(playerEntity.getHeldItem(EnumHand.MAIN_HAND)!=null&&playerEntity.getHeldItem(EnumHand.MAIN_HAND).getItem()== ModItems.paperScrapItem)
            {
                ItemStack paper = playerEntity.getHeldItem(EnumHand.MAIN_HAND);
                if(paper.stackSize==1) {
                    if (paper.getTagCompound() == null) {
                        paper.setTagCompound(new NBTTagCompound());
                    }
                    if(message.text=="")
                        paper.setTagCompound(null);
                    else
                        paper.getTagCompound().setString("text", message.text);
                }
                else if(message.text!=""){
                    paper.stackSize--;
                    ItemStack newPaper = new ItemStack(ModItems.paperScrapItem);
                    if (newPaper.getTagCompound() == null) {
                        newPaper.setTagCompound(new NBTTagCompound());
                    }
                    newPaper.getTagCompound().setString("text", message.text);
                    if (!playerEntity.inventory.addItemStackToInventory(newPaper))
                    {
                        playerEntity.dropItem(newPaper, false);
                    }
                }
            }
            else if(playerEntity.getHeldItem(EnumHand.OFF_HAND)!=null&&playerEntity.getHeldItem(EnumHand.OFF_HAND).getItem()== ModItems.paperScrapItem)
            {
                ItemStack paper = playerEntity.getHeldItem(EnumHand.OFF_HAND);
                if(paper.stackSize==1) {
                    if (paper.getTagCompound() == null) {
                        paper.setTagCompound(new NBTTagCompound());
                    }
                    if(message.text=="")
                        paper.setTagCompound(null);
                    else
                        paper.getTagCompound().setString("text", message.text);
                }
                else if(message.text!=""){
                    paper.stackSize--;
                    ItemStack newPaper = new ItemStack(ModItems.paperScrapItem);
                    if (newPaper.getTagCompound() == null) {
                        newPaper.setTagCompound(new NBTTagCompound());
                    }
                    newPaper.getTagCompound().setString("text", message.text);
                    if (!playerEntity.inventory.addItemStackToInventory(newPaper))
                    {
                        playerEntity.dropItem(newPaper, false);
                    }
                }
            }
        }
    }
}

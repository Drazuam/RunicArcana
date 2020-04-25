package com.latenighters.runicarcana.client.event;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.items.IClickable;
import com.latenighters.runicarcana.network.ClickableHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.latenighters.runicarcana.client.render.SymbolRenderer.renderSymbols;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onRender(RenderWorldLastEvent evt)
    {
        renderSymbols(evt);
    }

    @SubscribeEvent
    public static void onMouseEvent(GuiScreenEvent.MouseClickedEvent event)
    {
        if (event.getGui() == null || !(event.getGui() instanceof ContainerScreen)) {
            return;
        }

        if (((ContainerScreen) event.getGui()).getContainer() instanceof Container) {
            ContainerScreen<Container> gui = ((ContainerScreen<Container>) event.getGui());

            boolean rightClickDown = event.getButton() == 1;

            //TODO: error handling
            try {
                if (rightClickDown && gui.getSlotUnderMouse() != null) {
                    Slot underMouse = gui.getSlotUnderMouse();
                    int slot = 0;
                    Container container;
                    if (gui.getContainer() instanceof CreativeScreen.CreativeContainer) {
                        slot = underMouse.getSlotIndex();
                        PlayerInventory inv = (PlayerInventory)underMouse.inventory;
                        ItemStack item = inv.getStackInSlot(slot);
                        if (item.getItem() instanceof IClickable)
                        {
                            ClickableHandler.INSTANCE.sendToServer(new ClickableHandler.ClickActionMessage(slot, true));
                            RunicArcana.proxy.getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
                            event.setCanceled(true);
                        }

                    }
                    else {
                        slot = underMouse.slotNumber;
                        container = gui.getContainer();
                        if (container != null && slot < container.inventorySlots.size() &&
                                container.getSlot(slot) != null && !container.getSlot(slot).getStack().isEmpty()) {
                            ItemStack item = gui.getContainer().getSlot(slot).getStack();
                            if (item.getItem() instanceof IClickable) {
                                //example: is a charm or something
                                ClickableHandler.INSTANCE.sendToServer(new ClickableHandler.ClickActionMessage(slot, true));
                                RunicArcana.proxy.getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }
        }

    }

}

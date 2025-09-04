package com.hepdd.ae2emicraftingforge.client.helper;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.WidgetContainer;
import appeng.client.gui.widgets.AETextField;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.InventoryActionPacket;
import appeng.helpers.InventoryAction;
import appeng.menu.slot.FakeSlot;
import com.almostreliable.merequester.client.RequestSlot;
import com.almostreliable.merequester.platform.Platform;
import com.google.common.primitives.Ints;
import com.hepdd.ae2emicraftingforge.Ae2EmiCraftingMod;
import com.hepdd.ae2emicraftingforge.client.helper.interfaces.DropTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DropTargets {

    private DropTargets() {}

    public static List<DropTarget> getTargets(AEBaseScreen<?> aeScreen) {
        List<DropTarget> targets = new ArrayList<>();

        for (var slot : aeScreen.getMenu().slots) {
            if (slot.isActive() && slot instanceof FakeSlot fakeSlot) {
                var area = new Rect2i(aeScreen.getGuiLeft() + slot.x, aeScreen.getGuiTop() + slot.y, 16, 16);
                targets.add(new FakeSlotDropTarget(area, fakeSlot));
            }
        }

        for (var widget : reflectGetWidgets(aeScreen).values()) {
            if (widget instanceof AETextField search) {
                var area = new Rect2i(search.getX(), search.getY(),
                        search.getWidth(), search.getHeight());
                targets.add(new SearchBarDropTarget(area, search));
            }
        }

        return targets;
    }

    private record FakeSlotDropTarget(Rect2i area, FakeSlot slot) implements DropTarget {

        @Override
        public boolean canDrop(GenericStack stack) {
            return slot.canSetFilterTo(wrapFilterAsItem(stack));
        }

        @Override
        public boolean drop(GenericStack stack) {
            var itemStack = wrapFilterAsItem(stack);

            if (slot.canSetFilterTo(itemStack)) {
                if (slot instanceof RequestSlot requestSlot) {
                    Platform.sendDragAndDrop(requestSlot.getRequesterReference().getRequesterId(), requestSlot.getSlot(), itemStack);
                } else {
                    NetworkHandler.instance().sendToServer(new InventoryActionPacket(InventoryAction.SET_FILTER,
                            slot.index, itemStack));
                }
                return true;
            }

            return false;
        }

        private static ItemStack wrapFilterAsItem(GenericStack genericStack) {
            if (genericStack.what() instanceof AEItemKey itemKey) {
                return itemKey.toStack(Ints.saturatedCast(Math.max(1, genericStack.amount())));
            } else {
                return GenericStack.wrapInItemStack(genericStack.what(), Math.max(1, genericStack.amount()));
            }
        }
    }

    private record SearchBarDropTarget(Rect2i area, AETextField search) implements DropTarget {

        @Override
        public boolean canDrop(GenericStack stack) {
            return true;
        }

        @Override
        public boolean drop(GenericStack stack) {
            search.setValue(stack.what().getDisplayName().getString());
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, AbstractWidget> reflectGetWidgets(AEBaseScreen<?> screen) {
        try {
            var fWidgets = AEBaseScreen.class.getDeclaredField("widgets");
            fWidgets.setAccessible(true);
            WidgetContainer wc = (WidgetContainer) fWidgets.get(screen);
            var fWidgets0 = WidgetContainer.class.getDeclaredField("widgets");
            fWidgets0.setAccessible(true);
            return (Map<String, AbstractWidget>) fWidgets0.get(wc);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Ae2EmiCraftingMod.LOGGER.error("Failed to reflectively access AEBaseScreen widgets", e);
            return Map.of();
        }
    }
}

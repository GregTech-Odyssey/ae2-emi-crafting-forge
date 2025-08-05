package com.hepdd.ae2emicraftingforge.client.helper.rendering;

import net.minecraft.client.gui.GuiGraphics;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.Widget;

import java.util.List;
import java.util.Set;

import static appeng.integration.modules.jeirei.TransferHelper.BLUE_SLOT_HIGHLIGHT_COLOR;
import static appeng.integration.modules.jeirei.TransferHelper.RED_SLOT_HIGHLIGHT_COLOR;

public class Rendering {

    public static void renderMissingAndCraftableSlotOverlays(List<Widget> widgets, GuiGraphics guiGraphics,
                                                             Set<Integer> missingSlots, Set<Integer> craftableSlots) {
        int i = 0;
        for (var widget : widgets) {
            if (widget instanceof SlotWidget slot && isInputSlot(slot)) {
                boolean missing = missingSlots.contains(i);
                boolean craftable = craftableSlots.contains(i);
                i++;
                if (missing || craftable) {
                    var poseStack = guiGraphics.pose();
                    poseStack.pushPose();
                    poseStack.translate(0, 0, 400);
                    var innerBounds = getInnerBounds(slot);
                    guiGraphics.fill(innerBounds.x(), innerBounds.y(), innerBounds.right(),
                            innerBounds.bottom(), missing ? RED_SLOT_HIGHLIGHT_COLOR : BLUE_SLOT_HIGHLIGHT_COLOR);
                    poseStack.popPose();
                }
            }
        }
    }

    public static boolean isInputSlot(SlotWidget slot) {
        return slot.getRecipe() == null;
    }

    public static Bounds getInnerBounds(SlotWidget slot) {
        var bounds = slot.getBounds();
        return new Bounds(
                bounds.x() + 1,
                bounds.y() + 1,
                bounds.width() - 2,
                bounds.height() - 2);
    }
}

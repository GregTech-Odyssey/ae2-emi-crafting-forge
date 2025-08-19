package com.hepdd.ae2emicraftingforge.client.helper.rendering;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import appeng.api.stacks.AEKey;
import appeng.integration.modules.jeirei.TransferHelper;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.items.CraftingTermMenu;
import com.hepdd.ae2emicraftingforge.client.helper.mapper.EmiStackHelper;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.Widget;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static appeng.integration.modules.jeirei.TransferHelper.BLUE_SLOT_HIGHLIGHT_COLOR;
import static com.hepdd.ae2emicraftingforge.client.helper.rendering.Rendering.*;

public abstract class Result {

    /**
     * @return null doesn't override the default tooltip.
     */
    @Nullable
    public List<Component> getTooltip(EmiRecipe recipe, EmiCraftContext<?> context) {
        return null;
    }

    public abstract boolean canCraft();

    public void render(EmiRecipe recipe, EmiCraftContext<? extends AEBaseMenu> context, List<Widget> widgets,
                       GuiGraphics draw) {}

    public static final class Success extends Result {

        @Override
        public boolean canCraft() {
            return true;
        }
    }

    /**
     * There are missing ingredients, but at least one is present.
     */
    public static final class PartiallyCraftable extends Result {

        private final CraftingTermMenu.MissingIngredientSlots missingSlots;

        public PartiallyCraftable(CraftingTermMenu.MissingIngredientSlots missingSlots) {
            this.missingSlots = missingSlots;
        }

        @Override
        public boolean canCraft() {
            return true;
        }

        @Override
        public List<Component> getTooltip(EmiRecipe recipe, EmiCraftContext<?> context) {
            // EMI caches this tooltip, we cannot dynamically react to control being held here
            return TransferHelper.createCraftingTooltip(missingSlots, false);
        }

        @Override
        public void render(EmiRecipe recipe, EmiCraftContext<? extends AEBaseMenu> context, List<Widget> widgets,
                           GuiGraphics guiGraphics) {
            renderMissingAndCraftableSlotOverlays(widgets, guiGraphics, missingSlots.missingSlots(),
                    missingSlots.craftableSlots());
        }
    }

    /**
     * Indicates that some of the slots can already be crafted by the auto-crafting system.
     */
    public static final class EncodeWithCraftables extends Result {

        private final Set<AEKey> craftableKeys;

        /**
         * @param craftableKeys All keys that the current system can auto-craft.
         */
        public EncodeWithCraftables(Set<AEKey> craftableKeys) {
            this.craftableKeys = craftableKeys;
        }

        @Override
        public boolean canCraft() {
            return true;
        }

        @Override
        public List<Component> getTooltip(EmiRecipe emiRecipe, EmiCraftContext<?> context) {
            List<Component> gatheredTooltips = new ArrayList<>();
            var anyCatalyst = !emiRecipe.getCatalysts().isEmpty();
            if (anyCatalyst) {
                gatheredTooltips.add(Component.translatable("gtocore.ae.appeng.me2in1.emi.catalyst").withStyle(ChatFormatting.GREEN));
                gatheredTooltips.add(Component.translatable("gtocore.ae.appeng.me2in1.emi.catalyst.virtual").withStyle(ChatFormatting.DARK_GREEN));
            }
            var anyCraftable = emiRecipe.getInputs().stream()
                    .anyMatch(ing -> isCraftable(craftableKeys, ing));
            if (anyCraftable) {
                gatheredTooltips.addAll(TransferHelper.createEncodingTooltip(true));
            }
            if (anyCatalyst || anyCraftable) {
                return gatheredTooltips;
            }
            return null;
        }

        @Override
        public void render(EmiRecipe recipe, EmiCraftContext<? extends AEBaseMenu> context, List<Widget> widgets,
                           GuiGraphics guiGraphics) {
            for (var widget : widgets) {
                if (widget instanceof SlotWidget slot && isInputSlot(slot)) {
                    if (isCraftable(craftableKeys, slot.getStack())) {
                        var poseStack = guiGraphics.pose();
                        poseStack.pushPose();
                        poseStack.translate(0, 0, 400);
                        var bounds = getInnerBounds(slot);
                        guiGraphics.fill(bounds.x(),
                                bounds.y(),
                                bounds.right(),
                                bounds.bottom(),
                                BLUE_SLOT_HIGHLIGHT_COLOR);
                        poseStack.popPose();
                    }
                }
            }
        }

        private static boolean isCraftable(Set<AEKey> craftableKeys, EmiIngredient ingredient) {
            return ingredient.getEmiStacks().stream().anyMatch(emiIngredient -> {
                var stack = EmiStackHelper.toGenericStack(emiIngredient);
                return stack != null && craftableKeys.contains(stack.what());
            });
        }
    }

    static final class NotApplicable extends Result {

        @Override
        public boolean canCraft() {
            return false;
        }
    }

    static final class Error extends Result {

        @Getter
        private final Component message;
        private final Set<Integer> missingSlots;

        public Error(Component message, Set<Integer> missingSlots) {
            this.message = message;
            this.missingSlots = missingSlots;
        }

        @Override
        public boolean canCraft() {
            return false;
        }

        @Override
        public void render(EmiRecipe recipe, EmiCraftContext<? extends AEBaseMenu> context, List<Widget> widgets,
                           GuiGraphics guiGraphics) {
            renderMissingAndCraftableSlotOverlays(widgets, guiGraphics, missingSlots, Set.of());
        }

        @Override
        public List<Component> getTooltip(EmiRecipe emiRecipe, EmiCraftContext<?> context) {
            return List.of(getMessage());
        }
    }

    public static NotApplicable createNotApplicable() {
        return new NotApplicable();
    }

    public static Success createSuccessful() {
        return new Success();
    }

    public static Error createFailed(Component text) {
        return new Error(text, Set.of());
    }

    public static Error createFailed(Component text, Set<Integer> missingSlots) {
        return new Error(text, missingSlots);
    }
}

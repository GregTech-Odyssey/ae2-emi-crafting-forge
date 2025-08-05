package com.hepdd.ae2emicraftingforge.client.handler;

import net.minecraft.world.item.crafting.Recipe;

import appeng.core.localization.ItemModText;
import appeng.integration.modules.jeirei.CraftingHelper;
import appeng.menu.me.items.CraftingTermMenu;
import com.hepdd.ae2emicraftingforge.client.handler.generic.Ae2BaseRecipeHandler;
import com.hepdd.ae2emicraftingforge.client.helper.rendering.Result;
import dev.emi.emi.api.recipe.EmiRecipe;

import static com.hepdd.ae2emicraftingforge.client.helper.RecipeUtils.*;
import static com.hepdd.ae2emicraftingforge.client.recipes.generator.RecipeGenerator.createFakeRecipe;
import static net.minecraft.client.gui.screens.Screen.hasControlDown;

public class Ae2CraftingHandler<T extends CraftingTermMenu> extends Ae2BaseRecipeHandler<T> {

    public Ae2CraftingHandler(Class<T> containerClass) {
        super(containerClass);
    }

    @Override
    protected Result transferRecipe(T menu, Recipe<?> recipe, EmiRecipe emiRecipe, boolean doTransfer) {
        boolean craftingRecipe = isCraftingRecipe(recipe, emiRecipe);
        if (!craftingRecipe) {
            return Result.createNotApplicable();
        }

        if (!fitsIn3x3Grid(recipe)) {
            return Result.createFailed(ItemModText.RECIPE_TOO_LARGE.text());
        }

        if (recipe == null) {
            recipe = createFakeRecipe(emiRecipe);
        }

        // Find missing ingredient
        var slotToIngredientMap = getGuiSlotToIngredientMap(recipe);
        var missingSlots = menu.findMissingIngredients(getGuiSlotToIngredientMap(recipe));

        if (missingSlots.missingSlots().size() == slotToIngredientMap.size()) {
            // All missing, can't do much...
            return Result.createFailed(ItemModText.NO_ITEMS.text(), missingSlots.missingSlots());
        }

        if (!doTransfer) {
            if (missingSlots.anyMissing() || missingSlots.anyCraftable()) {
                // Highlight the slots with missing ingredients
                return new Result.PartiallyCraftable(missingSlots);
            }
        } else {
            // Thank you RS for pioneering this amazing feature! :)
            boolean craftMissing = hasControlDown();
            CraftingHelper.performTransfer(menu, recipe, craftMissing);
        }

        // No error
        return Result.createSuccessful();
    }
}

package com.hepdd.ae2emicraftingforge.client.helper;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

import appeng.integration.modules.jeirei.EncodingHelper;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;

import java.util.HashMap;
import java.util.Map;

import static com.hepdd.ae2emicraftingforge.client.handler.generic.Ae2BaseRecipeHandler.CRAFTING_GRID_HEIGHT;
import static com.hepdd.ae2emicraftingforge.client.handler.generic.Ae2BaseRecipeHandler.CRAFTING_GRID_WIDTH;

public class RecipeUtils {

    public static boolean isCraftingRecipe(Recipe<?> recipe, EmiRecipe emiRecipe) {
        return EncodingHelper.isSupportedCraftingRecipe(recipe) || emiRecipe.getCategory().equals(VanillaEmiRecipeCategories.CRAFTING);
    }

    public static boolean fitsIn3x3Grid(Recipe<?> recipe) {
        if (recipe != null) {
            return recipe.canCraftInDimensions(CRAFTING_GRID_WIDTH, CRAFTING_GRID_HEIGHT);
        } else {
            return true;
        }
    }

    public static Map<Integer, Ingredient> getGuiSlotToIngredientMap(Recipe<?> recipe) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int width;
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            width = shapedRecipe.getWidth();
        } else {
            width = 3;
        }

        HashMap<Integer, Ingredient> result = new HashMap<>(ingredients.size());

        for (int i = 0; i < ingredients.size(); ++i) {
            int guiSlot = i / width * 3 + i % width;
            Ingredient ingredient = ingredients.get(i);
            if (!ingredient.isEmpty()) {
                result.put(guiSlot, ingredient);
            }
        }

        return result;
    }
}

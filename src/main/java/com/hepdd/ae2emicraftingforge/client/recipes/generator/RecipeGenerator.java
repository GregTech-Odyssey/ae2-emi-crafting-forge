package com.hepdd.ae2emicraftingforge.client.recipes.generator;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiStack;

import static com.hepdd.ae2emicraftingforge.client.handler.generic.Ae2BaseRecipeHandler.CRAFTING_GRID_HEIGHT;
import static com.hepdd.ae2emicraftingforge.client.handler.generic.Ae2BaseRecipeHandler.CRAFTING_GRID_WIDTH;

public class RecipeGenerator {

    public static Recipe<?> createFakeRecipe(EmiRecipe display) {
        var ingredients = NonNullList.withSize(CRAFTING_GRID_WIDTH * CRAFTING_GRID_HEIGHT,
                Ingredient.EMPTY);

        for (int i = 0; i < Math.min(display.getInputs().size(), ingredients.size()); i++) {
            var ingredient = Ingredient.of(display.getInputs().get(i).getEmiStacks().stream()
                    .map(EmiStack::getItemStack)
                    .filter(is -> !is.isEmpty()));
            ingredients.set(i, ingredient);
        }

        return new ShapedRecipe(display.getId(), "", CraftingBookCategory.MISC, CRAFTING_GRID_WIDTH, CRAFTING_GRID_HEIGHT, ingredients, ItemStack.EMPTY);
    }
}

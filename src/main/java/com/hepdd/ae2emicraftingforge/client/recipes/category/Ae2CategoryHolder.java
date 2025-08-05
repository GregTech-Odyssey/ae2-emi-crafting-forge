package com.hepdd.ae2emicraftingforge.client.recipes.category;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import appeng.core.AppEng;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.core.localization.LocalizationEnum;
import appeng.recipes.entropy.EntropyRecipe;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import com.hepdd.ae2emicraftingforge.client.recipes.locale.TranslationKeys;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;

import java.util.List;
import java.util.function.Function;

public class Ae2CategoryHolder {

    public static Ae2RecipeCategory WORLD_INTERACTION = into(
            TransformRecipe.TYPE_ID,
            EmiStack.of(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED),
            TranslationKeys.CATEGORY_TRANSFORM);
    public static Ae2RecipeCategory INSCRIBER = into(
            InscriberRecipe.TYPE_ID,
            EmiStack.of(AEBlocks.INSCRIBER),
            TranslationKeys.CATEGORY_INSCRIBER);
    public static Ae2RecipeCategory CHARGER = into(
            ChargerRecipe.TYPE_ID,
            EmiStack.of(AEBlocks.CHARGER),
            TranslationKeys.CATEGORY_CHARGER);
    public static Ae2RecipeCategory ATTUNEMENT = unsafeInto(
            "attunement",
            EmiStack.of(AEParts.ME_P2P_TUNNEL),
            TranslationKeys.CATEGORY_ATTUNEMENT);
    public static Ae2RecipeCategory CONDENSER = unsafeInto(
            "condenser",
            EmiStack.of(AEBlocks.CONDENSER),
            TranslationKeys.CATEGORY_CONDENSER);

    public static Ae2RecipeCategory ENTROPY = into(
            EntropyRecipe.TYPE_ID,
            EmiStack.of(AEItems.ENTROPY_MANIPULATOR),
            TranslationKeys.CATEGORY_ENTROPY_MANIPULATOR);

    private static Ae2RecipeCategory into(ResourceLocation identifier, EmiRenderable icon, LocalizationEnum name) {
        return new Ae2RecipeCategory(identifier, icon, name);
    }

    private static Ae2RecipeCategory unsafeInto(String key, EmiRenderable icon, LocalizationEnum name) {
        return into(ResourceLocation.tryBuild(AppEng.MOD_ID, key), icon, name);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> void addAll(EmiRegistry registry, RecipeType type, Function<T, EmiRecipe> constructor) {
        for (T recipe : (List<T>) registry.getRecipeManager().getAllRecipesFor(type)) {
            registry.addRecipe(constructor.apply(recipe));
        }
    }
}

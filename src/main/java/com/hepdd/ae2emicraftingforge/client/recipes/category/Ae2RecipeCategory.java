package com.hepdd.ae2emicraftingforge.client.recipes.category;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import appeng.core.localization.LocalizationEnum;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;

public class Ae2RecipeCategory extends EmiRecipeCategory {

    private final MutableComponent name;

    public Ae2RecipeCategory(ResourceLocation id, EmiRenderable icon, LocalizationEnum locale) {
        super(id, icon);
        this.name = locale.text();
    }

    @Override
    public MutableComponent getName() {
        return name;
    }
}

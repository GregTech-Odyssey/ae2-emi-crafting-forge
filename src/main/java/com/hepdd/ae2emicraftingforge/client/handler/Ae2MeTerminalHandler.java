package com.hepdd.ae2emicraftingforge.client.handler;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;

import appeng.menu.me.common.MEStorageMenu;
import com.hepdd.ae2emicraftingforge.client.handler.generic.Ae2BaseRecipeHandler;
import com.hepdd.ae2emicraftingforge.client.helper.rendering.Result;
import dev.emi.emi.api.recipe.EmiRecipe;
import org.jetbrains.annotations.Nullable;

public class Ae2MeTerminalHandler<T extends MEStorageMenu> extends Ae2BaseRecipeHandler<T> {

    public Ae2MeTerminalHandler(Class<T> containerClass) {
        super(containerClass);
    }

    @Override
    protected Result transferRecipe(T menu, @Nullable Recipe<?> recipe, EmiRecipe emiRecipe, boolean doTransfer) {
        return Result.createFailed(Component.translatable("ae2-emi-crafting.error.crafting.storage_terminal"));
    }
}

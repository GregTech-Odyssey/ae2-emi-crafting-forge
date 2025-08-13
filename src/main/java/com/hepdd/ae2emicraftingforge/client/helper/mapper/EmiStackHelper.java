package com.hepdd.ae2emicraftingforge.client.helper.mapper;

import appeng.api.stacks.GenericStack;
import com.hepdd.ae2emicraftingforge.Ae2EmiCraftingMod;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class EmiStackHelper {

    private EmiStackHelper() {}

    @Nullable
    public static GenericStack toGenericStack(EmiStack emiStack) {
        if (emiStack == EmiStack.EMPTY) {
            return null;
        }

        for (var converter : EmiStackConverters.getConverters()) {
            var stack = converter.toGenericStack(emiStack);
            if (stack != null) {
                return stack;
            }
        }

        if (emiStack != null) {
            Ae2EmiCraftingMod.LOGGER.error("================ Missing Converter Error =================");
            Ae2EmiCraftingMod.LOGGER.error("Couldn't find a  GenericStack converter for EmiStack: " + emiStack.getItemStack());
            Ae2EmiCraftingMod.LOGGER.error("Please report this to the developers");
            Ae2EmiCraftingMod.LOGGER.error("https://github.com/blocovermelho/ae2-emi-crafting");
            Ae2EmiCraftingMod.LOGGER.error("================ Missing Converter Error =================");
        }

        return null;
    }

    @Nullable
    public static EmiStack toEmiStack(GenericStack stack) {
        for (var converter : EmiStackConverters.getConverters()) {
            var emiStack = converter.toEmiStack(stack);
            if (emiStack != null) {
                return emiStack;
            }
        }

        if (stack.what() != null) {
            Ae2EmiCraftingMod.LOGGER.error("================ Missing Converter Error =================");
            Ae2EmiCraftingMod.LOGGER.error(":k AeKey is " + stack.what().getClass());
            Ae2EmiCraftingMod.LOGGER.error("Couldn't find a EmiStack converter for AeKey: " + stack.what().toTagGeneric().toString());
            Ae2EmiCraftingMod.LOGGER.error("Please report this to the developers");
            Ae2EmiCraftingMod.LOGGER.error("https://github.com/blocovermelho/ae2-emi-crafting");
            Ae2EmiCraftingMod.LOGGER.error("================ Missing Converter Error =================");
        }

        return null;
    }

    public static List<List<GenericStack>> ofInputs(EmiRecipe emiRecipe) {
        return emiRecipe.getInputs().stream().map(EmiStackHelper::of).toList();
    }

    public static List<GenericStack> ofOutputs(EmiRecipe emiRecipe) {
        return emiRecipe.getOutputs().stream()
                .map(EmiStackHelper::toGenericStack)
                .filter(Objects::nonNull)
                .toList();
    }

    private static List<GenericStack> of(EmiIngredient emiIngredient) {
        if (emiIngredient.isEmpty()) {
            return Collections.emptyList();
        }

        return emiIngredient.getEmiStacks()
                .stream()
                .map(EmiStackHelper::toGenericStack)
                .filter(Objects::nonNull)
                .toList();
    }
}

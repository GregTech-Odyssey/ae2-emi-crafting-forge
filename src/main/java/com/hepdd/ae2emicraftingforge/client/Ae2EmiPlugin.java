package com.hepdd.ae2emicraftingforge.client;

import appeng.api.config.CondenserOutput;
import appeng.core.AEConfig;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.recipes.entropy.EntropyRecipe;
import appeng.recipes.handlers.ChargerRecipe;
import appeng.recipes.handlers.InscriberRecipe;
import appeng.recipes.transform.TransformRecipe;
import com.hepdd.ae2emicraftingforge.client.handler.Ae2CraftingHandler;
import com.hepdd.ae2emicraftingforge.client.handler.Ae2MeTerminalHandler;
import com.hepdd.ae2emicraftingforge.client.handler.Ae2PatternTerminalHandler;
import com.hepdd.ae2emicraftingforge.client.handler.generic.Ae2BaseDragHandler;
import com.hepdd.ae2emicraftingforge.client.handler.generic.Ae2BaseScreenExclusionZones;
import com.hepdd.ae2emicraftingforge.client.handler.generic.Ae2BaseStackProvider;
import com.hepdd.ae2emicraftingforge.client.recipes.Ae2ChargerRecipe;
import com.hepdd.ae2emicraftingforge.client.recipes.Ae2CondenserRecipe;
import com.hepdd.ae2emicraftingforge.client.recipes.Ae2EntropyManipulatorRecipe;
import com.hepdd.ae2emicraftingforge.client.recipes.Ae2InscriberRecipe;
import com.hepdd.ae2emicraftingforge.client.recipes.Ae2RecipeHolder;
import com.hepdd.ae2emicraftingforge.client.recipes.Ae2TransformRecipe;
import com.hepdd.ae2emicraftingforge.client.recipes.category.Ae2CategoryHolder;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;

@EmiEntrypoint
public class Ae2EmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addGenericExclusionArea(new Ae2BaseScreenExclusionZones());
        registry.addGenericStackProvider(new Ae2BaseStackProvider());
        registry.addGenericDragDropHandler(new Ae2BaseDragHandler());

        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEParts.CRAFTING_TERMINAL.stack()));
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(AEItems.WIRELESS_CRAFTING_TERMINAL.stack()));

        registry.addRecipeHandler(CraftingTermMenu.TYPE, new Ae2CraftingHandler<>(CraftingTermMenu.class));
        registry.addRecipeHandler(PatternEncodingTermMenu.TYPE, new Ae2PatternTerminalHandler<>(PatternEncodingTermMenu.class));
        // Workaround: Seeing Items from ME Terminal on Synthetic Favourites without using the GenericStackProvider.
        // Reasoning: For whatever reason that is broken on fluids, even though it shouldn't.
        registry.addRecipeHandler(MEStorageMenu.TYPE, new Ae2MeTerminalHandler<>(MEStorageMenu.class));

        registry.addCategory(Ae2CategoryHolder.WORLD_INTERACTION);
        Ae2CategoryHolder.addAll(registry, TransformRecipe.TYPE, Ae2TransformRecipe::new);

        registry.addCategory(Ae2CategoryHolder.INSCRIBER);
        registry.addWorkstation(Ae2CategoryHolder.INSCRIBER, EmiStack.of(AEBlocks.INSCRIBER));
        Ae2CategoryHolder.addAll(registry, InscriberRecipe.TYPE, Ae2InscriberRecipe::new);

        registry.addCategory(Ae2CategoryHolder.CHARGER);
        registry.addWorkstation(Ae2CategoryHolder.CHARGER, EmiStack.of(AEBlocks.CHARGER));
        registry.addWorkstation(Ae2CategoryHolder.CHARGER, EmiStack.of(AEBlocks.CRANK));
        Ae2CategoryHolder.addAll(registry, ChargerRecipe.TYPE, Ae2ChargerRecipe::new);

        registry.addCategory(Ae2CategoryHolder.CONDENSER);
        registry.addWorkstation(Ae2CategoryHolder.CONDENSER, EmiStack.of(AEBlocks.CONDENSER));
        registry.addRecipe(new Ae2CondenserRecipe(CondenserOutput.MATTER_BALLS));
        registry.addRecipe(new Ae2CondenserRecipe(CondenserOutput.SINGULARITY));

        registry.addCategory(Ae2CategoryHolder.ENTROPY);
        registry.addWorkstation(Ae2CategoryHolder.ENTROPY, EmiStack.of(AEItems.ENTROPY_MANIPULATOR));
        Ae2CategoryHolder.addAll(registry, EntropyRecipe.TYPE, Ae2EntropyManipulatorRecipe::new);

        registry.addCategory(Ae2CategoryHolder.ATTUNEMENT);
        registry.addDeferredRecipes(Ae2RecipeHolder::registerP2PAttunements);

        if (AEConfig.instance().isEnableFacadeRecipesInJEI()) {
            registry.addDeferredRecipes(Ae2RecipeHolder::registerFacades);
        }

        Ae2RecipeHolder.registerDescriptions(registry);
    }
}

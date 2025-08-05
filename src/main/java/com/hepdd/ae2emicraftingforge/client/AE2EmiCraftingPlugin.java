package com.hepdd.ae2emicraftingforge.client;

import com.hepdd.ae2emicraftingforge.Ae2EmiCraftingMod;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

@EmiEntrypoint
public class AE2EmiCraftingPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        Ae2EmiCraftingMod.LOGGER.info("AE2 Emi Crafting Support!");

        if (Ae2EmiCraftingMod.isModLoaded("ae2")) {
            new Ae2EmiPlugin().register(registry);
            Ae2EmiCraftingMod.LOGGER.info("AE2 done.");
        }

        if (Ae2EmiCraftingMod.isModLoaded("ae2wtlib")) {
            new Ae2WtEmiPlugin().register(registry);
            Ae2EmiCraftingMod.LOGGER.info("AE2WTLIB done.");
        }
    }
}

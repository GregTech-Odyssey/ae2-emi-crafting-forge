package com.hepdd.ae2emicraftingforge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Ae2EmiCraftingMod.MOD_ID)
public class Ae2EmiCraftingMod {

    public static final String MOD_ID = "ae2emicraftingforge";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Ae2EmiCraftingMod() {}

    /**
     * Checks if a mod is loaded
     *
     * @param modId The mod ID to check
     * @return True if the mod is loaded, false otherwise
     */
    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}

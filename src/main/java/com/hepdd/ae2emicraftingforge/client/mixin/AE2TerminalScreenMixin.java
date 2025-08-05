package com.hepdd.ae2emicraftingforge.client.mixin;

import net.minecraft.network.chat.Component;

import appeng.client.gui.me.common.TerminalSettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TerminalSettingsScreen.class)
public class AE2TerminalScreenMixin {
    // Thanks a lot vriska minecraft for the mixins they were tastey
    // Source:
    // https://gitlab.com/vriska/emplied/-/blob/main/src/main/java/dev/vriska/emplied/mixin/TerminalSettingsScreenMixin.java

    @ModifyVariable(method = "<init>(Lappeng/client/gui/me/common/MEStorageScreen;)V", at = @At(value = "STORE"), name = "hasExternalSearch", remap = false)
    private boolean ae2emicrafting$set_external_search(boolean hasExternalSearch) {
        return true;
    }

    @ModifyVariable(method = "<init>(Lappeng/client/gui/me/common/MEStorageScreen;)V", at = @At("STORE"), name = "externalSearchMod", remap = false)
    private Component ae2emicrafting$set_mod_name(Component externalSearchMod) {
        return Component.literal("EMI");
    }
}

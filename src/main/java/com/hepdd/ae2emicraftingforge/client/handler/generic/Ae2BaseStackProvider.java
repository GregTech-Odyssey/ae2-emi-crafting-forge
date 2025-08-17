package com.hepdd.ae2emicraftingforge.client.handler.generic;

import net.minecraft.client.gui.screens.Screen;

import appeng.client.gui.AEBaseScreen;
import com.hepdd.ae2emicraftingforge.client.helper.mapper.EmiStackHelper;
import dev.emi.emi.api.EmiStackProvider;
import dev.emi.emi.api.stack.EmiStackInteraction;

public class Ae2BaseStackProvider implements EmiStackProvider<Screen> {

    @Override
    public EmiStackInteraction getStackAt(Screen screen, int x, int y) {
        if (screen instanceof AEBaseScreen<?> aeScreen) {
            var stack = aeScreen.getStackUnderMouse(x, y);
            if (stack != null) {
                var emiStack = EmiStackHelper.toEmiStack(stack.stack());
                if (emiStack != null) {
                    if (emiStack.getAmount() == 0) {
                        emiStack.setAmount(1);
                    }
                    return new EmiStackInteraction(emiStack);
                }
            }
        }
        return EmiStackInteraction.EMPTY;
    }
}

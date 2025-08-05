package com.hepdd.ae2emicraftingforge.client.handler.generic;

import net.minecraft.client.gui.screens.Screen;

import appeng.client.gui.AEBaseScreen;
import dev.emi.emi.api.EmiExclusionArea;
import dev.emi.emi.api.widget.Bounds;

import java.util.function.Consumer;

public class Ae2BaseScreenExclusionZones implements EmiExclusionArea<Screen> {

    @Override
    public void addExclusionArea(Screen screen, Consumer<Bounds> consumer) {
        if (!(screen instanceof AEBaseScreen<?> aeScreen)) {
            return;
        }

        for (var zone : aeScreen.getExclusionZones()) {
            consumer.accept(new Bounds(zone.getX(), zone.getY(), zone.getWidth(), zone.getHeight()));
        }
    }
}

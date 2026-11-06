package com.magicworld;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public final class MagicWorldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(MagicWorldMod.CLASS_SELECTION_HANDLER, ClassSelectionScreen::new);
    }
}

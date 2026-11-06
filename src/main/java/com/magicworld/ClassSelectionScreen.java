package com.magicworld;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public final class ClassSelectionScreen extends HandledScreen<MagicWorldScreenHandler> {
    private static final int WIDTH = 360;
    private static final int HEIGHT = 220;

    public ClassSelectionScreen(MagicWorldScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = WIDTH;
        this.backgroundHeight = HEIGHT;
    }

    @Override
    protected void init() {
        super.init();
        int left = (width - WIDTH) / 2;
        int top = (height - HEIGHT) / 2;
        for (int i = 0; i < MagicWorldMod.CLASS_IDS.length; i++) {
            int column = i % 2;
            int row = i / 2;
            int x = left + 12 + column * 174;
            int y = top + 30 + row * 22;
            final int classIndex = i;
            addDrawableChild(ButtonWidget.builder(Text.literal(MagicWorldMod.CLASS_NAMES[i]), button -> {
                if (client != null && client.interactionManager != null) {
                    client.interactionManager.clickButton(handler.syncId, classIndex);
                }
            }).dimensions(x, y, 166, 20).build());
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int left = (width - backgroundWidth) / 2;
        int top = (height - backgroundHeight) / 2;
        context.fill(left, top, left + backgroundWidth, top + backgroundHeight, 0xF0101824);
        context.drawBorder(left, top, backgroundWidth, backgroundHeight, 0xFFB99A5B);
        context.drawText(textRenderer, Text.literal("Divine Blessing - choose your class"), left + 12, top + 10, 0xFFE8D7A3, false);
    }
}

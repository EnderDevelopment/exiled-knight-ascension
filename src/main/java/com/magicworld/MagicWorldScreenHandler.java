package com.magicworld;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public final class MagicWorldScreenHandler extends ScreenHandler {
    public static final ScreenHandlerType<MagicWorldScreenHandler> TYPE = MagicWorldMod.CLASS_SELECTION_HANDLER;

    public MagicWorldScreenHandler(int syncId, PlayerInventory inventory) {
        super(TYPE, syncId);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (!player.getWorld().isClient && player instanceof net.minecraft.server.network.ServerPlayerEntity serverPlayer) {
            MagicWorldMod.selectClassFromScreen(serverPlayer, id);
        }
        return true;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}

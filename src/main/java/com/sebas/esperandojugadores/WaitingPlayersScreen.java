package com.sebas.esperandojugadores;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WaitingPlayersScreen extends Screen {
    private static final int RED = 0xFF9F3038, RED_DARK = 0xFF4B2028;
    private static final int PURPLE_BAR = 0xFF9870C9, PANEL = 0xEE17141B, TEXT = 0xFFF1EAF4;
    public WaitingPlayersScreen() { super(Component.literal("Esperando jugadores")); }
    @Override protected void init() {
        addRenderableWidget(Button.builder(Component.literal("Cerrar"),
            button -> Minecraft.getInstance().setScreen(null))
            .bounds(width / 2 - 50, height / 2 + 58, 100, 20).build());
    }
    @Override public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        int w = Math.min(300, width - 24), h = 150, x = (width - w) / 2, y = (height - h) / 2;
        fill(poseStack, x - 3, y - 3, x + w + 3, y + h + 3, RED);
        fill(poseStack, x, y, x + w, y + h, PANEL);
        fill(poseStack, x, y, x + 14, y + 3, RED);
        fill(poseStack, x, y, x + 3, y + 14, RED);
        drawCenteredString(poseStack, font, "Esperando jugadores...", width / 2, y + 22, TEXT);
        int online = ClientEvents.getKnownPlayerCount(Minecraft.getInstance());
        int target = ClientConfig.TARGET_PLAYERS.get();
        drawCenteredString(poseStack, font, online + "/" + target + " jugadores", width / 2, y + 48, TEXT);
        int bw = w - 48, bx = width / 2 - bw / 2, by = y + 75;
        fill(poseStack, bx, by, bx + bw, by + 10, RED_DARK);
        int filled = Math.min(bw, Math.round(bw * (Math.min(online, target) / (float) target)));
        if (filled > 4) fill(poseStack, bx + 2, by + 2, bx + filled - 2, by + 8, PURPLE_BAR);
        drawCenteredString(poseStack, font, "F6: ocultar / mostrar", width / 2, y + 99, 0xFFBFB5C8);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }
    @Override public boolean isPauseScreen() { return false; }
}

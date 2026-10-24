package com.sebas.esperandojugadores;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Full-screen waiting lobby inspired by the server-title reference image. */
public class WaitingPlayersScreen extends Screen {
    private static final int WHITE = 0xFFF4F0E8;
    private static final int MUTED = 0xFFD6C8C4;
    private static final int GOLD = 0xFFFFD66B;
    private static final int GOLD_DARK = 0xFF75421C;
    private static final int RED = 0xFFB51F2C;
    private static final int PANEL = 0xD9161018;

    public WaitingPlayersScreen() {
        super(Component.literal("Esperando jugadores"));
    }

    @Override
    protected void init() {
        int buttonY = height - 76;
        int gap = 18;
        int buttonWidth = Math.min(260, (width - 3 * gap) / 2);
        int leftX = width / 2 - buttonWidth - gap / 2;
        int rightX = width / 2 + gap / 2;

        addRenderableWidget(new Button(leftX, buttonY, buttonWidth, 34,
                Component.literal("⇥  Salir del servidor"),
                button -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.getConnection() != null) {
                        mc.getConnection().getConnection().disconnect(Component.literal("Saliste del servidor"));
                    }
                    mc.setScreen(null);
                }));

        addRenderableWidget(new Button(rightX, buttonY, buttonWidth, 34,
                Component.literal("◉  Ocultar (F6)"),
                button -> Minecraft.getInstance().setScreen(null)));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        // Full-screen red sunset-inspired backdrop made from layered gradients and blocky clouds.
        fillGradient(poseStack, 0, 0, width, height, 0xFF17131E, 0xFF8C261F);
        fillGradient(poseStack, 0, height / 3, width, height, 0x003D202C, 0xFFB33A20);

        int cloudY = Math.max(36, height / 7);
        for (int i = 0; i < 8; i++) {
            int cx = (i * (width / 7)) - 30;
            int cy = cloudY + (i % 3) * 22;
            int shade = (i % 2 == 0) ? 0x263B303F : 0x304A3038;
            fill(poseStack, cx, cy, cx + 110, cy + 14, shade);
            fill(poseStack, cx + 24, cy - 9, cx + 82, cy + 14, shade);
            fill(poseStack, cx + 72, cy + 6, cx + 135, cy + 20, shade);
        }

        // Square sunset on the right, echoing the pixel-art reference.
        int sun = Math.min(82, Math.max(42, height / 7));
        int sx = width * 3 / 4;
        int sy = height / 2 - sun / 2;
        fill(poseStack, sx - 10, sy - 10, sx + sun + 10, sy + sun + 10, 0x30FFD95B);
        fill(poseStack, sx, sy, sx + sun, sy + sun, 0xFFFFD65A);
        fill(poseStack, sx + 10, sy + 10, sx + sun - 10, sy + sun - 10, 0xFFFFF0A5);

        // Darken the entire backdrop for readability.
        fill(poseStack, 0, 0, width, height, 0x38200D18);

        // Server title at the top.
        int titleY = Math.max(22, height / 16);
        drawCenteredString(poseStack, font, "ONE BLOCK HORROR", width / 2 + 2, titleY + 2, 0xFF26130C);
        drawCenteredString(poseStack, font, "ONE BLOCK HORROR", width / 2, titleY, GOLD);
        drawCenteredString(poseStack, font, "SERVER", width / 2 + 2, titleY + 26, 0xFF26130C);
        drawCenteredString(poseStack, font, "SERVER", width / 2, titleY + 24, GOLD);
        int labelW = 116;
        fill(poseStack, width / 2 - labelW / 2, titleY + 43, width / 2 + labelW / 2, titleY + 61, RED);
        drawCenteredString(poseStack, font, "SEBASTIAN", width / 2, titleY + 48, WHITE);

        // Large central translucent panel.
        int panelW = Math.min(590, width - 36);
        int panelH = 204;
        int panelX = (width - panelW) / 2;
        int panelY = Math.max(titleY + 80, height / 2 - panelH / 2 - 12);
        panelY = Math.min(panelY, height - panelH - 92);
        fill(poseStack, panelX - 2, panelY - 2, panelX + panelW + 2, panelY + panelH + 2, 0xA8D2A18C);
        fill(poseStack, panelX, panelY, panelX + panelW, panelY + panelH, PANEL);
        fill(poseStack, panelX, panelY, panelX + panelW, panelY + 3, 0xFFBBA49B);

        drawCenteredString(poseStack, font, "ESPERANDO JUGADORES...", width / 2, panelY + 23, WHITE);
        fill(poseStack, panelX + 24, panelY + 55, panelX + panelW - 24, panelY + 57, 0xFF77615D);

        Minecraft mc = Minecraft.getInstance();
        int online = ClientEvents.getKnownPlayerCount(mc);
        int target = Math.max(1, ClientConfig.TARGET_PLAYERS.get());
        drawCenteredString(poseStack, font, "Jugadores: " + online + "/" + target,
                width / 2, panelY + 76, 0xFF7BFF85);
        drawCenteredString(poseStack, font, online >= target ? "¡Todos los jugadores han llegado!" : "¡Aun faltan mas jugadores!",
                width / 2, panelY + 108, MUTED);
        drawCenteredString(poseStack, font, "Mantente conectado...", width / 2, panelY + 128, MUTED);

        int barW = panelW - 72;
        int barX = width / 2 - barW / 2;
        int barY = panelY + 158;
        fill(poseStack, barX, barY, barX + barW, barY + 12, 0xFF3C2026);
        int progress = Math.round((barW - 4) * (Math.min(online, target) / (float) target));
        if (progress > 0) fill(poseStack, barX + 2, barY + 2, barX + 2 + progress, barY + 10, GOLD_DARK);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

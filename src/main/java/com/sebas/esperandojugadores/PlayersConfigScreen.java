package com.sebas.esperandojugadores;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PlayersConfigScreen extends Screen {
    private final Screen parent;
    private int targetPlayers;

    public PlayersConfigScreen(Screen parent) {
        super(Component.literal("Configuración de EsperandoJugadores"));
        this.parent = parent;
        this.targetPlayers = ClientConfig.TARGET_PLAYERS.get();
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        this.addRenderableWidget(new Button(center - 100, this.height / 2 - 18, 35, 20,
                Component.literal("-"), button -> {
                    targetPlayers = Math.max(1, targetPlayers - 1);
                }));
        this.addRenderableWidget(new Button(center + 65, this.height / 2 - 18, 35, 20,
                Component.literal("+"), button -> {
                    targetPlayers = Math.min(100, targetPlayers + 1);
                }));
        this.addRenderableWidget(new Button(center - 100, this.height / 2 + 22, 200, 20,
                Component.literal("Guardar"), button -> {
                    ClientConfig.TARGET_PLAYERS.set(targetPlayers);
                    ClientConfig.SPEC.save();
                    this.minecraft.setScreen(parent);
                }));
        this.addRenderableWidget(new Button(center - 100, this.height / 2 + 48, 200, 20,
                Component.literal("Cancelar"), button -> this.minecraft.setScreen(parent)));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, this.height / 2 - 70, 0xFFFFFFFF);
        drawCenteredString(poseStack, this.font, "Jugadores necesarios para cerrar la pantalla", this.width / 2,
                this.height / 2 - 43, 0xFFD8D0E0);
        drawCenteredString(poseStack, this.font, Integer.toString(targetPlayers), this.width / 2,
                this.height / 2 - 13, 0xFFFFFFFF);
        drawCenteredString(poseStack, this.font, "Elige un valor entre 1 y 100", this.width / 2,
                this.height / 2 + 8, 0xFFBFB5C8);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }
}

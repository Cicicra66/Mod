package com.sebas.esperandojugadores;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = EsperandoJugadores.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientEvents {
    public static final KeyMapping TOGGLE_SCREEN = new KeyMapping(
            "key.esperandojugadores.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F6,
            "key.categories.esperandojugadores"
    );

    private static boolean waitingScreenOpenedForConnection = false;

    private ClientEvents() {}

    @Mod.EventBusSubscriber(modid = EsperandoJugadores.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModEvents {
        private ModEvents() {}

        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(TOGGLE_SCREEN);
        }
    }

    // Open the screen directly when the client finishes connecting to a server.
    @SubscribeEvent
    public static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        Minecraft mc = Minecraft.getInstance();
        waitingScreenOpenedForConnection = true;
        mc.setScreen(new WaitingPlayersScreen());
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        waitingScreenOpenedForConnection = false;
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof WaitingPlayersScreen) {
            mc.setScreen(null);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();

        // Reset the flag after leaving a world or server.
        if (mc.player == null || mc.level == null || mc.getConnection() == null) {
            waitingScreenOpenedForConnection = false;
            return;
        }

        // Fallback in case the login event happened before the world was ready.
        if (!waitingScreenOpenedForConnection) {
            waitingScreenOpenedForConnection = true;
            mc.setScreen(new WaitingPlayersScreen());
        }

        // F6 hides the screen or brings it back.
        while (TOGGLE_SCREEN.consumeClick()) {
            mc.setScreen(mc.screen instanceof WaitingPlayersScreen ? null : new WaitingPlayersScreen());
        }

        // Close the waiting screen once the configured player target is reached.
        if (mc.screen instanceof WaitingPlayersScreen
                && getKnownPlayerCount(mc) >= ClientConfig.TARGET_PLAYERS.get()) {
            mc.setScreen(null);
        }
    }

    public static int getKnownPlayerCount(Minecraft mc) {
        if (mc.getConnection() == null) return 1;
        Collection<PlayerInfo> players = mc.getConnection().getOnlinePlayers();
        return Math.max(1, players.size());
    }
}

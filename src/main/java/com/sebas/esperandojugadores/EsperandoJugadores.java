package com.sebas.esperandojugadores;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(EsperandoJugadores.MOD_ID)
public class EsperandoJugadores {
    public static final String MOD_ID = "esperandojugadores";

    public EsperandoJugadores() {
        ModLoadingContext context = ModLoadingContext.get();
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> new PlayersConfigScreen(parent)
                )
        );
    }
}

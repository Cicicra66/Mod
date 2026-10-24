package com.sebas.esperandojugadores;
import net.minecraftforge.common.ForgeConfigSpec;
public final class ClientConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.IntValue TARGET_PLAYERS;
    static {
        BUILDER.push("esperando_jugadores");
        TARGET_PLAYERS = BUILDER.comment("Cantidad de jugadores para cerrar la pantalla (1 a 100).")
                .defineInRange("targetPlayers", 6, 1, 100);
        BUILDER.pop();
    }
    public static final ForgeConfigSpec SPEC = BUILDER.build();
    private ClientConfig() {}
}

package net.mrmisc.essenceofthewild.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EOTWConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec.IntValue CHEESE_MAKER_PROCESS_SECONDS;
    public static final ForgeConfigSpec.IntValue NEST_HATCH_MINUTES;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("cheese_maker");
        CHEESE_MAKER_PROCESS_SECONDS = builder
                .comment("How many seconds the cheese maker takes to turn one sheep milk bucket into sheep cheese.")
                .defineInRange("process_seconds", 300, 1, 86400);
        builder.pop();

        builder.push("nest");
        NEST_HATCH_MINUTES = builder
                .comment("How many minutes nest eggs take to hatch.")
                .defineInRange("hatch_minutes", 8, 1, 1440);
        builder.pop();

        COMMON_SPEC = builder.build();
    }

    public static int cheeseMakerProcessTicks() {
        return Math.max(1, CHEESE_MAKER_PROCESS_SECONDS.get() * 20);
    }

    public static int nestHatchTicks() {
        return Math.max(1, NEST_HATCH_MINUTES.get() * 60 * 20);
    }
}

package net.mrmisc.essenceofthewild.entity.custom.rabbit;

import java.util.function.BooleanSupplier;
import net.mrmisc.essenceofthewild.entity.util.VariantSet;
import net.mrmisc.essenceofthewild.entity.util.Habitat;
import net.mrmisc.essenceofthewild.entity.util.BiomeQuery;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;

import java.util.List;

public class RabbitVariants {
    public static final MobVariant BASIC =
            new MobVariant("basic",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/rabbit/rabbit.png"),
                    false, false);
    public static final MobVariant BASIC_WHITE =
            new MobVariant("basic_grey",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/rabbit/white_rabbit.png"),
                    false, false);
    public static final MobVariant COLD =
            new MobVariant("cold",
                    ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID,
                            "textures/entity/rabbit/snow_rabbit.png"),
                    true, false);

    public static final List<MobVariant> ALL = List.of(BASIC, BASIC_WHITE, COLD);

    public static final VariantSet<MobVariant> SET = VariantSet.of(MobVariant::id, ALL);

    public static MobVariant pick(BiomeQuery at, BooleanSupplier coin) {
        if (at.is(Habitat.COLD)) {
            return COLD;
        }
        return coin.getAsBoolean() ? BASIC : BASIC_WHITE;
    }

}

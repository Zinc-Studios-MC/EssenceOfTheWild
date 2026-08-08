package net.mrmisc.essenceofthewild.entity.custom.rat;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class RatVariants {
    public static final RatVariant BASIC = variant("basic", "rat.png", "angry_rat.png");
    public static final RatVariant GRAY = variant("gray", "gray_rat.png", "angry_gray_rat.png");
    public static final RatVariant WHITE = variant("white", "white_rat.png", "angry_white_rat.png");
    // name tag easter egg, never spawns on its own, and it just uses the normal angry texture
    public static final RatVariant FISHGUY = variant("fishguy", "fishguy_rat.png", "angry_rat.png");

    public static final List<RatVariant> ALL = List.of(BASIC, GRAY, WHITE, FISHGUY);

    // the ones that actually spawn in the world, basic gray or white
    public static RatVariant randomNatural(RandomSource random) {
        return switch (random.nextInt(3)) {
            case 0 -> BASIC;
            case 1 -> GRAY;
            default -> WHITE;
        };
    }

    private static RatVariant variant(String id, String texture, String angryTexture) {
        return new RatVariant(
                id,
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/rat/" + texture),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/rat/" + angryTexture)
        );
    }
}

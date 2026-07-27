package net.mrmisc.essenceofthewild.entity.custom.rat;

import net.minecraft.resources.ResourceLocation;

/**
 * A rat colour variant. {@code location} is the calm texture, {@code angryLocation}
 * the texture shown while the rat is angry/attacking.
 */
public record RatVariant(String id, ResourceLocation location, ResourceLocation angryLocation) {
}

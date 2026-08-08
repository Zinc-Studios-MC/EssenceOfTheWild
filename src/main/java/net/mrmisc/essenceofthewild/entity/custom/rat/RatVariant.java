package net.mrmisc.essenceofthewild.entity.custom.rat;

import net.minecraft.resources.ResourceLocation;

// one rat colour, location is the normal texture and angryLocation is the one used when its angry
public record RatVariant(String id, ResourceLocation location, ResourceLocation angryLocation) {
}

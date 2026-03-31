package net.mrmisc.essenceofthewild.entity.util;

import net.minecraft.resources.ResourceLocation;

public record MobVariant(String id, ResourceLocation location, boolean cold, boolean warm) {
}

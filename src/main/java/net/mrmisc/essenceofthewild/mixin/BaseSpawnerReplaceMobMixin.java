package net.mrmisc.essenceofthewild.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseSpawner.class)
public class BaseSpawnerReplaceMobMixin {

    @Inject(method = "load", at = @At("HEAD"))
    private void essenceofthewild$replaceSpawnerMobs(@Nullable Level level, BlockPos pos, CompoundTag tag,
                                                     CallbackInfo ci) {
        essenceofthewild$rewrite(tag.getCompound("SpawnData"));

        ListTag potentials = tag.getList("SpawnPotentials", Tag.TAG_COMPOUND);
        for (int i = 0; i < potentials.size(); i++) {
            essenceofthewild$rewrite(potentials.getCompound(i).getCompound("data"));
        }
    }

    @Unique
    private static void essenceofthewild$rewrite(CompoundTag spawnData) {
        CompoundTag entity = spawnData.getCompound("entity");
        String id = entity.getString("id");

        if (EntityType.getKey(EntityType.SPIDER).toString().equals(id)) {
            entity.putString("id", EOTWEntities.SPIDER.getId().toString());
        } else if (EntityType.getKey(EntityType.CAVE_SPIDER).toString().equals(id)) {
            entity.putString("id", EOTWEntities.CAVE_SPIDER.getId().toString());
        }
    }
}

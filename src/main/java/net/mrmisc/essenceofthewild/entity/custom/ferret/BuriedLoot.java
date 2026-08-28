package net.mrmisc.essenceofthewild.entity.custom.ferret;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public final class BuriedLoot {

    public static ItemStack rollAt(ServerLevel level, BlockPos pos, Vec3 origin, @Nullable Player owner) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof BrushableBlockEntity brushable)) {
            return ItemStack.EMPTY;
        }

        ResourceLocation id = lootTableOf(brushable);
        if (id == null) {
            return ItemStack.EMPTY;
        }

        LootParams.Builder params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, origin);
        if (owner != null) {
            params.withLuck(owner.getLuck()).withParameter(LootContextParams.THIS_ENTITY, owner);
        }

        LootTable table = level.getServer().getLootData().getLootTable(id);
        ObjectArrayList<ItemStack> items = table.getRandomItems(params.create(LootContextParamSets.CHEST));
        return items.isEmpty() ? ItemStack.EMPTY : items.get(0);
    }

    @Nullable
    private static ResourceLocation lootTableOf(BrushableBlockEntity brushable) {
        ItemStack stack = new ItemStack(Items.AIR);
        brushable.saveToItem(stack);
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return null;
        }
        String id = tag.getCompound("BlockEntityTag").getString("LootTable");
        return id.isEmpty() ? null : ResourceLocation.tryParse(id);
    }

    private BuriedLoot() {
    }
}

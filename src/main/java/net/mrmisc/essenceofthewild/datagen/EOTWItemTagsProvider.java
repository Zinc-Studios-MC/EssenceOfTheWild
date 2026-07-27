package net.mrmisc.essenceofthewild.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EOTWItemTagsProvider extends ItemTagsProvider {
    public EOTWItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, EssenceOfTheWildMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.ARROWS).add(EOTWItems.UNDERWATER_ARROW.get());
        this.tag(ItemTags.SIGNS).add(EOTWItems.MANGO_SIGN.get());
        this.tag(ItemTags.HANGING_SIGNS).add(EOTWItems.MANGO_HANGING_SIGN.get());
    }
}

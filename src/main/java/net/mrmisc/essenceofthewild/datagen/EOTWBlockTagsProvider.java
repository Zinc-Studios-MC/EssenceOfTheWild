package net.mrmisc.essenceofthewild.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EOTWBlockTagsProvider extends BlockTagsProvider {
    public EOTWBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, EssenceOfTheWildMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(EOTWBlocks.MANGO_LOG.get())
                .add(EOTWBlocks.STRIPPED_MANGO_LOG.get())
                .add(EOTWBlocks.MANGO_WOOD.get())
                .add(EOTWBlocks.STRIPPED_MANGO_WOOD.get());
    }
}

package net.mrmisc.essenceofthewild.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;

public class EOTWBlockModelProvider extends BlockStateProvider {
    public EOTWBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EssenceOfTheWildMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        leavesBlock(EOTWBlocks.MANGO_LEAVES);
        leavesBlock(EOTWBlocks.VANILLA_LEAVES);

        blockItem(EOTWBlocks.MANGO_LOG);
        blockItem(EOTWBlocks.STRIPPED_MANGO_LOG);
        blockItem(EOTWBlocks.MANGO_WOOD);
        blockItem(EOTWBlocks.STRIPPED_MANGO_WOOD);

        logBlock(((RotatedPillarBlock) EOTWBlocks.MANGO_LOG.get()));
        axisBlock(((RotatedPillarBlock) EOTWBlocks.MANGO_WOOD.get()), blockTexture(EOTWBlocks.MANGO_LOG.get()), blockTexture(EOTWBlocks.MANGO_LOG.get()));
        axisBlock((RotatedPillarBlock) EOTWBlocks.STRIPPED_MANGO_LOG.get(), ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "block/stripped_mango_log"),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "block/stripped_mango_log_top"));
        axisBlock((RotatedPillarBlock) EOTWBlocks.STRIPPED_MANGO_WOOD.get(), ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "block/stripped_mango_log"),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "block/stripped_mango_log"));

        saplingBlock(EOTWBlocks.MANGO_SAPLING);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private void leavesBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), ResourceLocation.fromNamespaceAndPath("minecraft", "block/leaves"),
                        "all", blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject, String appendix) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("essenceofthewild:block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath() + appendix));
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("essenceofthewild:block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}

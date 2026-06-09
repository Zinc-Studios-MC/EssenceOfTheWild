package net.mrmisc.essenceofthewild.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.block.custom.crops.RedOnionCropBlock;
import net.mrmisc.essenceofthewild.util.EOTWUtils;

import java.util.function.Function;

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
        blockItem(EOTWBlocks.MANGO_STAIRS);
        blockItem(EOTWBlocks.MANGO_SLAB);
        blockItem(EOTWBlocks.MANGO_FENCE_GATE);
        blockItem(EOTWBlocks.MANGO_PRESURE_PLATE);
        blockItem(EOTWBlocks.MANGO_BUTTON);

        blockWithItem(EOTWBlocks.MANGO_PLANKS);

        logBlock(((RotatedPillarBlock) EOTWBlocks.MANGO_LOG.get()));
        axisBlock(((RotatedPillarBlock) EOTWBlocks.MANGO_WOOD.get()), blockTexture(EOTWBlocks.MANGO_LOG.get()), blockTexture(EOTWBlocks.MANGO_LOG.get()));
        axisBlock((RotatedPillarBlock) EOTWBlocks.STRIPPED_MANGO_LOG.get(), EOTWUtils.getLoc("block/stripped_mango_log"), EOTWUtils.getLoc("block/stripped_mango_log_top"));
        axisBlock((RotatedPillarBlock) EOTWBlocks.STRIPPED_MANGO_WOOD.get(), EOTWUtils.getLoc("block/stripped_mango_log"), EOTWUtils.getLoc("block/stripped_mango_log"));
        doorBlockWithRenderType((DoorBlock) EOTWBlocks.MANGO_DOOR.get(), EOTWUtils.getLoc("block/mango_door_bottom"), EOTWUtils.getLoc("block/mango_door_top"), "cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock) EOTWBlocks.MANGO_TRAPDOOR.get(), EOTWUtils.getLoc("block/mango_trapdoor"), true, "cutout");
        slabBlock((SlabBlock) EOTWBlocks.MANGO_SLAB.get(), blockTexture(EOTWBlocks.MANGO_PLANKS.get()), blockTexture(EOTWBlocks.MANGO_PLANKS.get()));
        buttonBlock((ButtonBlock) EOTWBlocks.MANGO_BUTTON.get(), blockTexture(EOTWBlocks.MANGO_PLANKS.get()));
        fenceBlockWithRenderType((FenceBlock) EOTWBlocks.MANGO_FENCE.get(), blockTexture(EOTWBlocks.MANGO_PLANKS.get()), "cutout");
        fenceGateBlockWithRenderType((FenceGateBlock) EOTWBlocks.MANGO_FENCE_GATE.get(), blockTexture(EOTWBlocks.MANGO_PLANKS.get()), "cutout");
        pressurePlateBlock((PressurePlateBlock) EOTWBlocks.MANGO_PRESURE_PLATE.get(), blockTexture(EOTWBlocks.MANGO_PLANKS.get()));
        stairsBlock((StairBlock) EOTWBlocks.MANGO_STAIRS.get(), blockTexture(EOTWBlocks.MANGO_PLANKS.get()));
        signBlock(((StandingSignBlock) EOTWBlocks.MANGO_SIGN.get()), EOTWBlocks.MANGO_WALL_SIGN.get(), blockTexture(EOTWBlocks.MANGO_PLANKS.get()));
        hangingSignBlock(EOTWBlocks.MANGO_HANGING_SIGN.get(), EOTWBlocks.MANGO_WALL_HANGING_SIGN.get(), blockTexture(EOTWBlocks.MANGO_PLANKS.get()));
        saplingBlock(EOTWBlocks.MANGO_SAPLING);

        makeCrop((CropBlock) EOTWBlocks.RED_ONION_CROP.get(), "red_onion_stage", "red_onion_stage");

        EOTWBlocks.getRolledWool().forEach(this::makeRolledWool);


    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }
    private ConfiguredModel[] states(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((RedOnionCropBlock) block).getAgeProperty()),
                ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "block/" + textureName + state.getValue(((RedOnionCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    public void makeCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> states(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    public void makeRolledWool(RegistryObject<Block> reg){
        Block block = reg.get();
        simpleBlock(block, models().cubeColumn(name(block), EOTWUtils.getLoc("block/" + name(block) + "_side"), EOTWUtils.getLoc("block/" + name(block))));
        blockItem(reg);
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

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("essenceofthewild:block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}

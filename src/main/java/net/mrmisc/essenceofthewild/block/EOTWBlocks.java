package net.mrmisc.essenceofthewild.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.custom.cheesemaker.CheeseMakerBlock;
import net.mrmisc.essenceofthewild.block.custom.crops.RedOnionCropBlock;
import net.mrmisc.essenceofthewild.block.custom.freezer.WoodenFreezerBlock;
import net.mrmisc.essenceofthewild.block.custom.misc.MangoBlock;
import net.mrmisc.essenceofthewild.block.custom.crops.StrawberryCropBlock;
import net.mrmisc.essenceofthewild.block.custom.wood.*;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import net.mrmisc.essenceofthewild.worldgen.tree.mango.MangoTreeGrower;

import java.util.function.Supplier;

public class EOTWBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<Block> WOODEN_FREEZER = registerBlock("wooden_freezer",
            ()-> new WoodenFreezerBlock(BlockBehaviour.Properties.of().noOcclusion().noParticlesOnBreak()));

    public static RegistryObject<Block> CHEESE_MAKER = registerBlock("cheese_maker",
            ()-> new CheeseMakerBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static RegistryObject<Block> MANGO = BLOCKS.register("mango",
            ()-> new MangoBlock(BlockBehaviour.Properties.copy(Blocks.COCOA).noOcclusion()));

    public static RegistryObject<Block> MANGO_SAPLING = registerBlock("mango_sapling",
            ()-> new SaplingBlock(new MangoTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));


    public static final RegistryObject<Block> MANGO_LEAVES = registerBlock("mango_leaves",
            () -> createLeaves(60, 30));

    public static final RegistryObject<Block> VANILLA_LEAVES = registerBlock("vanilla_leaves",
            () -> createLeaves(60, 30));

    public static RegistryObject<Block> MANGO_LOG = registerBlock("mango_log",
            ()-> new EOTWWoodBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static RegistryObject<Block> MANGO_PLANKS = registerBlock("mango_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });
    public static RegistryObject<Block> MANGO_STAIRS = registerBlock("mango_stairs",
            ()-> new StairBlock(()-> MANGO_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static RegistryObject<Block> MANGO_SLAB = registerBlock("mango_slabs",
            ()-> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static RegistryObject<Block> MANGO_FENCE = registerBlock("mango_fence",
            ()-> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static RegistryObject<Block> MANGO_FENCE_GATE = registerBlock("mango_fence_gate",
            ()-> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE), EOTWUtils.MANGO_WOOD));
    public static RegistryObject<Block> MANGO_TRAPDOOR = registerBlock("mango_trapdoor",
            ()-> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), EOTWUtils.MANGO));
    public static RegistryObject<Block> MANGO_DOOR = registerBlock("mango_door",
            ()-> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), EOTWUtils.MANGO));
    public static RegistryObject<Block> MANGO_BUTTON = registerBlock("mango_button",
            ()-> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), EOTWUtils.MANGO, 10, true));
    public static RegistryObject<SignBlock> MANGO_SIGN = BLOCKS.register("mango_sign",
            ()-> new EOTWStandingSign(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), EOTWUtils.MANGO_WOOD));
    public static RegistryObject<WallSignBlock> MANGO_WALL_SIGN = BLOCKS.register("mango_wall_sign",
            ()-> new EOTWWallSign(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), EOTWUtils.MANGO_WOOD));
    public static RegistryObject<CeilingHangingSignBlock> MANGO_HANGING_SIGN = BLOCKS.register("mango_hanging_sign",
            ()-> new EOTWHangingSign(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), EOTWUtils.MANGO_WOOD));
    public static RegistryObject<WallHangingSignBlock> MANGO_WALL_HANGING_SIGN = BLOCKS.register("mango_wall_hanging_sign",
            ()-> new EOTWWallHangingSign(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), EOTWUtils.MANGO_WOOD));
    public static RegistryObject<Block> MANGO_PRESURE_PLATE = registerBlock("mango_pressure_plate",
            ()-> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), EOTWUtils.MANGO));
    public static RegistryObject<Block> MANGO_WOOD = registerBlock("mango_wood",
            ()-> new EOTWWoodBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static RegistryObject<Block> STRIPPED_MANGO_LOG = registerBlock("stripped_mango_log",
            ()-> new EOTWWoodBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static RegistryObject<Block> STRIPPED_MANGO_WOOD = registerBlock("stripped_mango_wood",
            ()-> new EOTWWoodBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));

    public static RegistryObject<Block> STRAWBERRY_CROP = registerBlock("strawberry_crop",
            ()-> new StrawberryCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static RegistryObject<Block> RED_ONION_CROP = registerBlock("red_onion_crop",
            ()-> new RedOnionCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return EOTWItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static LeavesBlock createLeaves(int flammability, int fireSpreadSpeed){
        return new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).noOcclusion()) {
            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return fireSpreadSpeed;
            }

            @Override
            public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
                if(pState.is(VANILLA_LEAVES.get()) && pPlayer.getItemInHand(pHand).is(Items.SHEARS)){
                    level.setBlockAndUpdate(pos, EOTWBlocks.MANGO_LEAVES.get().defaultBlockState());
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(EOTWItems.VANILLA_FLOWER.get())));
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.FAIL;
            }
        };
    }
}

















































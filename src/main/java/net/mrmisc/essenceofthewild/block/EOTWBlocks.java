package net.mrmisc.essenceofthewild.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.custom.burrow.BurrowBlock;
import net.mrmisc.essenceofthewild.block.custom.cheesemaker.CheeseMakerBlock;
import net.mrmisc.essenceofthewild.block.custom.crops.RedOnionCropBlock;
import net.mrmisc.essenceofthewild.block.custom.freezer.WoodenFreezerBlock;
import net.mrmisc.essenceofthewild.block.custom.misc.MangoBlock;
import net.mrmisc.essenceofthewild.block.custom.crops.StrawberryBushBlock;
import net.mrmisc.essenceofthewild.block.custom.nest.NestBlock;
import net.mrmisc.essenceofthewild.block.custom.sleeping_bag.SleepingBagBlock;
import net.mrmisc.essenceofthewild.block.custom.sleeping_bag.SleepingBagPart;
import net.mrmisc.essenceofthewild.block.custom.wood.*;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import net.mrmisc.essenceofthewild.worldgen.tree.mango.MangoTreeGrower;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EOTWBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<Block> WOODEN_FREEZER = registerBlock("wooden_freezer",
            ()-> new WoodenFreezerBlock(BlockBehaviour.Properties.of().noOcclusion().noParticlesOnBreak()));
    public static RegistryObject<Block> WHITE_SLEEPING_BAG = registerBlock("white_sleeping_bag",()-> sleepingBagBlock(DyeColor.WHITE));
    public static RegistryObject<Block> YELLOW_SLEEPING_BAG = registerBlock("yellow_sleeping_bag",()-> sleepingBagBlock(DyeColor.YELLOW));
    public static RegistryObject<Block> BLACK_SLEEPING_BAG = registerBlock("black_sleeping_bag",()-> sleepingBagBlock(DyeColor.BLACK));
    public static RegistryObject<Block> BLUE_SLEEPING_BAG = registerBlock("blue_sleeping_bag",()-> sleepingBagBlock(DyeColor.BLUE));
    public static RegistryObject<Block> BROWN_SLEEPING_BAG = registerBlock("brown_sleeping_bag",()-> sleepingBagBlock(DyeColor.BROWN));
    public static RegistryObject<Block> CYAN_SLEEPING_BAG = registerBlock("cyan_sleeping_bag",()-> sleepingBagBlock(DyeColor.CYAN));
    public static RegistryObject<Block> GRAY_SLEEPING_BAG = registerBlock("gray_sleeping_bag",()-> sleepingBagBlock(DyeColor.GRAY));
    public static RegistryObject<Block> LIGHT_BLUE_SLEEPING_BAG = registerBlock("light_blue_sleeping_bag",()-> sleepingBagBlock(DyeColor.LIGHT_BLUE));
    public static RegistryObject<Block> LIGHT_GRAY_SLEEPING_BAG = registerBlock("light_gray_sleeping_bag",()-> sleepingBagBlock(DyeColor.LIGHT_GRAY));
    public static RegistryObject<Block> LIME_SLEEPING_BAG = registerBlock("lime_sleeping_bag",()-> sleepingBagBlock(DyeColor.LIME));
    public static RegistryObject<Block> MAGENTA_SLEEPING_BAG = registerBlock("magenta_sleeping_bag",()-> sleepingBagBlock(DyeColor.MAGENTA));
    public static RegistryObject<Block> ORANGE_SLEEPING_BAG = registerBlock("orange_sleeping_bag",()-> sleepingBagBlock(DyeColor.ORANGE));
    public static RegistryObject<Block> PINK_SLEEPING_BAG = registerBlock("pink_sleeping_bag",()-> sleepingBagBlock(DyeColor.PINK));
    public static RegistryObject<Block> PURPLE_SLEEPING_BAG = registerBlock("purple_sleeping_bag",()-> sleepingBagBlock(DyeColor.PURPLE));
    public static RegistryObject<Block> RED_SLEEPING_BAG = registerBlock("red_sleeping_bag",()-> sleepingBagBlock(DyeColor.RED));
    public static RegistryObject<Block> GREEN_SLEEPING_BAG = registerBlock("green_sleeping_bag",()-> sleepingBagBlock(DyeColor.GREEN));

    public static RegistryObject<Block> CHEESE_MAKER = registerBlock("cheese_maker",
            ()-> new CheeseMakerBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static RegistryObject<Block> NEST = registerBlock("nest",
            ()-> new NestBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).noOcclusion()));

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
    public static RegistryObject<Block> STRAWBERRY_BUSH = registerBlock("strawberry_bush",
            ()-> new StrawberryBushBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static RegistryObject<Block> RED_ONION_CROP = registerBlock("red_onion_crop",
            ()-> new RedOnionCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    public static RegistryObject<Block> WHITE_ROLLED_WOOL = registerBlock("white_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)));
    public static RegistryObject<Block> BLACK_ROLLED_WOOL = registerBlock("black_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));

    public static RegistryObject<Block> BLUE_ROLLED_WOOL = registerBlock("blue_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_WOOL)));

    public static RegistryObject<Block> BROWN_ROLLED_WOOL = registerBlock("brown_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL)));

    public static RegistryObject<Block> CYAN_ROLLED_WOOL = registerBlock("cyan_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.CYAN_WOOL)));

    public static RegistryObject<Block> GREEN_ROLLED_WOOL = registerBlock("green_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.GREEN_WOOL)));

    public static RegistryObject<Block> GREY_ROLLED_WOOL = registerBlock("grey_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.GRAY_WOOL)));

    public static RegistryObject<Block> LIGHT_BLUE_ROLLED_WOOL = registerBlock("light_blue_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.LIGHT_BLUE_WOOL)));

    public static RegistryObject<Block> LIGHT_GREY_ROLLED_WOOL = registerBlock("light_grey_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_WOOL)));

    public static RegistryObject<Block> LIME_ROLLED_WOOL = registerBlock("lime_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.LIME_WOOL)));

    public static RegistryObject<Block> MAGENTA_ROLLED_WOOL = registerBlock("magenta_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.MAGENTA_WOOL)));

    public static RegistryObject<Block> ORANGE_ROLLED_WOOL = registerBlock("orange_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.ORANGE_WOOL)));

    public static RegistryObject<Block> PINK_ROLLED_WOOL = registerBlock("pink_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.PINK_WOOL)));

    public static RegistryObject<Block> PURPLE_ROLLED_WOOL = registerBlock("purple_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.PURPLE_WOOL)));

    public static RegistryObject<Block> RED_ROLLED_WOOL = registerBlock("red_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));

    public static RegistryObject<Block> YELLOW_ROLLED_WOOL = registerBlock("yellow_rolled_wool",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.YELLOW_WOOL  )));
    
    public static RegistryObject<Block> DIRT_BURROW_BLOCK = registerBlock("dirt_burrow_block",
            ()-> new BurrowBlock(BlockBehaviour.Properties.copy(Blocks.DIRT).noOcclusion()));
    public static RegistryObject<Block> SAND_BURROW_BLOCK = registerBlock("sand_burrow_block",
            ()-> new BurrowBlock(BlockBehaviour.Properties.copy(Blocks.SAND).noOcclusion()));
    public static RegistryObject<Block> MUD_BURROW_BLOCK = registerBlock("mud_burrow_block",
            ()-> new BurrowBlock(BlockBehaviour.Properties.copy(Blocks.MUD).noOcclusion()));

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
            protected boolean decaying(BlockState pState) {
                return pState.getValue(PERSISTENT) && pState.getValue(DISTANCE) == 15;
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

    public static List<RegistryObject<Block>> getRolledWool(){
        List<RegistryObject<Block>> wool = new ArrayList<>();
        wool.add(WHITE_ROLLED_WOOL);
        wool.add(BLACK_ROLLED_WOOL);
        wool.add(BLUE_ROLLED_WOOL);
        wool.add(BROWN_ROLLED_WOOL);
        wool.add(CYAN_ROLLED_WOOL);
        wool.add(GREEN_ROLLED_WOOL);
        wool.add(GREY_ROLLED_WOOL);
        wool.add(LIGHT_BLUE_ROLLED_WOOL);
        wool.add(LIGHT_GREY_ROLLED_WOOL);
        wool.add(LIME_ROLLED_WOOL);
        wool.add(PINK_ROLLED_WOOL);
        wool.add(MAGENTA_ROLLED_WOOL);
        wool.add(PURPLE_ROLLED_WOOL);
        wool.add(ORANGE_ROLLED_WOOL);
        wool.add(RED_ROLLED_WOOL);
        wool.add(YELLOW_ROLLED_WOOL);
        return wool;
    }
    public static List<RegistryObject<Block>> getSleepingBags(){
        List<RegistryObject<Block>> sb = new ArrayList<>();
        sb.add(WHITE_SLEEPING_BAG);
        sb.add(BLACK_SLEEPING_BAG);
        sb.add(BLUE_SLEEPING_BAG);
        sb.add(BROWN_SLEEPING_BAG);
        sb.add(CYAN_SLEEPING_BAG);
        sb.add(GREEN_SLEEPING_BAG);
        sb.add(GRAY_SLEEPING_BAG);
        sb.add(LIGHT_BLUE_SLEEPING_BAG);
        sb.add(LIGHT_GRAY_SLEEPING_BAG);
        sb.add(LIME_SLEEPING_BAG);
        sb.add(PINK_SLEEPING_BAG);
        sb.add(MAGENTA_SLEEPING_BAG);
        sb.add(PURPLE_SLEEPING_BAG);
        sb.add(ORANGE_SLEEPING_BAG);
        sb.add(RED_SLEEPING_BAG);
        sb.add(YELLOW_SLEEPING_BAG);
        return sb;
    }

    private static SleepingBagBlock sleepingBagBlock(DyeColor pColor) {
        return new SleepingBagBlock(pColor, BlockBehaviour.Properties.of().mapColor((state) -> state.getValue(SleepingBagBlock.PART) == BedPart.FOOT ? pColor.getMapColor() : MapColor.WOOL).sound(SoundType.WOOD).strength(0.2F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY));
    }
}

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.custom.cheesemaker.CheeseMakerBlock;
import net.mrmisc.essenceofthewild.block.custom.freezer.WoodenFreezerBlock;
import net.mrmisc.essenceofthewild.block.custom.misc.MangoBlock;
import net.mrmisc.essenceofthewild.block.custom.wood.EOTWWoodBlock;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.worldgen.tree.mango.MangoTreeGrower;

import java.util.function.Supplier;

public class EOTWBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, EssenceOfTheWildMod.MOD_ID);

    public static RegistryObject<Block> WOODEN_FREEZER = registerBlock("wooden_freezer",
            ()-> new WoodenFreezerBlock(BlockBehaviour.Properties.of().noOcclusion().noParticlesOnBreak()));

    public static RegistryObject<Block> CHEESE_MAKER = registerBlock("cheese_maker",
            ()-> new CheeseMakerBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static RegistryObject<Block> MANGO = registerBlock("mango",
            ()-> new MangoBlock(BlockBehaviour.Properties.copy(Blocks.COCOA).noOcclusion()));

    public static RegistryObject<Block> MANGO_SAPLING = registerBlock("mango_sapling",
            ()-> new SaplingBlock(new MangoTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));


    public static final RegistryObject<Block> MANGO_LEAVES = registerBlock("mango_leaves",
            () -> createLeaves(60, 30));

    public static final RegistryObject<Block> VANILLA_LEAVES = registerBlock("vanilla_leaves",
            () -> createLeaves(60, 30));

    public static RegistryObject<Block> MANGO_LOG = registerBlock("mango_log",
            ()-> new EOTWWoodBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static RegistryObject<Block> MANGO_WOOD = registerBlock("mango_wood",
            ()-> new EOTWWoodBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static RegistryObject<Block> STRIPPED_MANGO_LOG = registerBlock("stripped_mango_log",
            ()-> new EOTWWoodBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static RegistryObject<Block> STRIPPED_MANGO_WOOD = registerBlock("stripped_mango_wood",
            ()-> new EOTWWoodBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
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

















































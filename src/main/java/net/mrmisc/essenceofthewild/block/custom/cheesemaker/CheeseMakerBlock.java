package net.mrmisc.essenceofthewild.block.custom.cheesemaker;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.mrmisc.essenceofthewild.block.entity.custom.cheesemaker.CheeseMakerBlockEntity;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import net.mrmisc.essenceofthewild.recipe.cheesemaker.CheeseMakerRecipe;

public class CheeseMakerBlock extends BaseEntityBlock {

    public static final int EMPTY = 0;
    public static final int MILK = 1;
    public static final int CHEESE = 2;

    public static final IntegerProperty CONTENT = IntegerProperty.create("state", EMPTY, CHEESE);

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 4, 0, 16, 6, 16),      // base plate
            Block.box(0, 0, 12, 4, 4, 16),     // leg 1
            Block.box(12, 0, 12, 16, 4, 16),   // leg 2
            Block.box(0, 0, 0, 4, 4, 4),       // leg 3
            Block.box(12, 0, 0, 16, 4, 4),     // leg 4
            Block.box(0, 6, 0, 16, 13, 2),     // wall n
            Block.box(0, 6, 14, 16, 13, 16),   // wall s
            Block.box(14, 6, 2, 16, 13, 14),   // wall e
            Block.box(0, 6, 2, 2, 13, 14),     // wall w
            Block.box(2, 9, 2, 14, 11, 14)     // content
    );

    public CheeseMakerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(CONTENT, EMPTY));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (!(blockEntity instanceof CheeseMakerBlockEntity cheeseMakerBlockEntity)) {
            return InteractionResult.PASS;
        }

        ItemStack stack = pPlayer.getItemInHand(pHand);

        if (cheeseMakerBlockEntity.hasCheese()) {
            cheeseMakerBlockEntity.giveCheese(pPlayer);
            return InteractionResult.CONSUME;
        }

        Optional<CheeseMakerRecipe> recipe = cheeseMakerBlockEntity.getRecipeFor(stack);
        if (recipe.isPresent() && cheeseMakerBlockEntity.isEmpty()) {
            cheeseMakerBlockEntity.startCheese(recipe.get(), stack);
            pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack filledResult = ItemUtils.createFilledResult(stack, pPlayer, recipe.get().getContainer());
            pPlayer.setItemInHand(pHand, filledResult);
            return InteractionResult.CONSUME;
        }

        if (stack.is(Items.BUCKET) && cheeseMakerBlockEntity.hasMilk() && cheeseMakerBlockEntity.getContainerStack().is(Items.BUCKET)) {
            ItemStack input = cheeseMakerBlockEntity.getInputStack();
            cheeseMakerBlockEntity.empty();
            pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack filledResult = ItemUtils.createFilledResult(stack, pPlayer, input);
            pPlayer.setItemInHand(pHand, filledResult);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CheeseMakerBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, EOTWBlockEntities.CHEESE_MAKER.get(), CheeseMakerBlockEntity::tick);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof CheeseMakerBlockEntity cheeseMakerBlockEntity) {
                cheeseMakerBlockEntity.drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(CONTENT);
    }

}

package net.mrmisc.essenceofthewild.block.custom.cheesemaker;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.mrmisc.essenceofthewild.item.EOTWItems;

public class CheeseMakerBlock extends Block {

    private static final IntegerProperty CONTENT = IntegerProperty.create("state", 0, 2);

    public CheeseMakerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(CONTENT, 0));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide()){
            ItemStack stack = pPlayer.getItemInHand(pHand);
            if(stack.is(EOTWItems.SHEEP_MILK_BUCKET.get()) && pState.getValue(CONTENT) == 0){
                pLevel.setBlockAndUpdate(pPos, pState.setValue(CONTENT, 1));
                pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                ItemStack filledResult = ItemUtils.createFilledResult(stack, pPlayer, Items.BUCKET.getDefaultInstance());
                pPlayer.setItemInHand(pHand, filledResult);
                return InteractionResult.SUCCESS;
            }
            if(stack.is(Items.BUCKET) && pState.getValue(CONTENT) == 1){
                pLevel.setBlockAndUpdate(pPos, pState.setValue(CONTENT, 0));
                pPlayer.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                ItemStack filledResult = ItemUtils.createFilledResult(stack, pPlayer, EOTWItems.SHEEP_MILK_BUCKET.get().getDefaultInstance());
                pPlayer.setItemInHand(pHand, filledResult);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(CONTENT);
    }


}

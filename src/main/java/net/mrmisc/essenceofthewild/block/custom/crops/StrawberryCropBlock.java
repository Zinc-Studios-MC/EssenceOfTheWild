package net.mrmisc.essenceofthewild.block.custom.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.mrmisc.essenceofthewild.item.EOTWItems;

public class StrawberryCropBlock extends CropBlock{

    public StrawberryCropBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected net.minecraft.world.level.ItemLike getBaseSeedId() {
            return EOTWItems.STRAWBERRY.get();
    };

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        int i = pState.getValue(AGE);
        boolean flag = (i == 7) || (i == 6) || (i == 5) || (i == 4);
        if (!flag && pPlayer.getItemInHand(pHand).is(Items.BONE_MEAL)) {
                return InteractionResult.PASS;
        } else if (i > 1) {
                int j = pLevel.random.nextInt(2);
                popResource(pLevel, pPos, new ItemStack(EOTWItems.STRAWBERRY.get(), i <= 2 ? i : i + j));
                pLevel.playSound(null, pPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);
                BlockState blockstate = pState.setValue(AGE, 1);
                pLevel.setBlock(pPos, blockstate, 2);
                pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, blockstate));
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {
                return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
    }
}
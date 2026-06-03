package net.mrmisc.essenceofthewild.item.custom.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.mrmisc.essenceofthewild.item.EOTWItems;

public class IceAxe extends Item {
    public IceAxe(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if(pState.is(BlockTags.ICE)){
            return 20.0f;
        }
        return 1f;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if(pAttacker instanceof Player player) {
            pTarget.hurt(player.damageSources().playerAttack(player), 5);
        }
        pStack.hurtAndBreak(2, pAttacker, player -> player.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pMiningEntity) {
        if(!pLevel.isClientSide()){
            if(pState.is(BlockTags.ICE)){
                pLevel.destroyBlock(pPos, false);
                ItemStack stack = new ItemStack(EOTWItems.ICE_CUBES.get(), pLevel.getRandom().nextInt(2,5));
                Block.popResource(pLevel, pPos, stack);
                pStack.hurtAndBreak(2, pMiningEntity, player -> player.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            }
        }
        return super.mineBlock(pStack, pLevel, pState, pPos, pMiningEntity);
    }
}

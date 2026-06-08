package net.mrmisc.essenceofthewild.block.custom.sleeping_bag;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.mrmisc.essenceofthewild.block.entity.custom.sleeping_bag.server.SleepingBagBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SleepingBagBlock extends BedBlock {
    public static final String IS_SLEEPING_BAG = "isSleepingBag";
    public SleepingBagBlock(DyeColor pColor, Properties pProperties) {
        super(pColor, pProperties);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if(!level.isClientSide()){
            ServerPlayer serverPlayer = (ServerPlayer) player;
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    private boolean kickVillagerOutOfBed(Level pLevel, BlockPos pPos) {
        List<Villager> villagers = pLevel.getEntitiesOfClass(Villager.class, new AABB(pPos), LivingEntity::isSleeping);
        if (villagers.isEmpty()) {
            return false;
        } else {
            villagers.get(0).stopSleeping();
            return true;
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (pState.getValue(PART) != BedPart.HEAD) {
                pPos = pPos.relative(pState.getValue(FACING));
                pState = pLevel.getBlockState(pPos);
                if (!pState.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            if (!canSetSpawn(pLevel)) {
                pLevel.removeBlock(pPos, false);
                BlockPos pos = pPos.relative(((Direction)pState.getValue(FACING)).getOpposite());
                if (pLevel.getBlockState(pos).is(this)) {
                    pLevel.removeBlock(pos, false);
                }

                Vec3 $$7 = pPos.getCenter();
                pLevel.explode(null, pLevel.damageSources().badRespawnPointExplosion($$7), null, $$7, 5.0F, true, Level.ExplosionInteraction.BLOCK);
                return InteractionResult.SUCCESS;
            } else if (pState.getValue(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(pLevel, pPos)) {
                    pPlayer.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                }

                return InteractionResult.SUCCESS;
            } else {
                pPlayer.getPersistentData().putBoolean(IS_SLEEPING_BAG,true);
                pPlayer.startSleepInBed(pPos).ifLeft((problem) -> {
                    if (problem.getMessage() != null) {
                        pPlayer.displayClientMessage(problem.getMessage(), true);
                    }

                });
                return InteractionResult.SUCCESS;
            }
        }
    }
}

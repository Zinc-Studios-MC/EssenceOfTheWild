package net.mrmisc.essenceofthewild.entity.custom.rat;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

// once a tamed rat is assigned to a composter with a stick it harvests grown crops around it,
// dumps the produce in the nearest chest and brings the leftover seeds back to the composter
public class RatHarvestGoal extends Goal {

    private static final int SEARCH_RADIUS = 8;
    private static final int SEARCH_HEIGHT = 4;
    private static final double REACH_SQR = 2.5D * 2.5D;

    private static final Set<Item> SEEDS = Set.of(
            Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.PUMPKIN_SEEDS,
            Items.MELON_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD);

    private final RatEntity rat;
    private int actionCooldown = 0;

    public RatHarvestGoal(RatEntity rat) {
        this.rat = rat;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.rat.isTame() || this.rat.isBaby() || this.rat.isOrderedToSit()
                || this.rat.getTarget() != null || !this.rat.hasComposter()) {
            return false;
        }
        BlockPos composter = this.rat.getComposterPos();
        if (composter == null || !this.rat.level().getBlockState(composter).is(net.minecraft.world.level.block.Blocks.COMPOSTER)) {
            return false;
        }
        return hasWork() || isFarFromComposter();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void stop() {
        this.rat.getNavigation().stop();
    }

    @Override
    public void tick() {
        BlockPos composter = this.rat.getComposterPos();
        if (composter == null) {
            return;
        }

        // 1. empty a finished composter and carry the bone meal off, needs a free slot
        if (isComposterReady() && hasBufferSpace()) {
            if (moveWithinReach(composter)) {
                collectBonemeal(composter);
            }
            return;
        }

        // 2. holding produce so take it to a chest
        if (hasBufferMatching(false)) {
            BlockPos chest = findContainer();
            if (chest != null) {
                if (moveWithinReach(chest)) {
                    depositProduce(chest);
                }
                return;
            }
            // no chest around, just hang onto it and carry on
        }

        // 3. holding seeds so go compost them
        if (hasBufferMatching(true)) {
            if (moveWithinReach(composter)) {
                compostSeeds(composter);
            }
            return;
        }

        // 4. go break a grown crop
        BlockPos crop = findMatureCrop();
        if (crop != null) {
            if (moveWithinReach(crop)) {
                harvest(crop);
            }
            return;
        }

        // 5. nothing to do, just idle near the composter
        if (isFarFromComposter()) {
            this.rat.getNavigation().moveTo(composter.getX() + 0.5, composter.getY(), composter.getZ() + 0.5, 1.0D);
        }
    }

    // figuring out if theres work to do

    private boolean hasWork() {
        return isComposterReady() || !bufferIsEmpty() || findMatureCrop() != null;
    }

    private boolean isComposterReady() {
        BlockPos c = this.rat.getComposterPos();
        if (c == null) {
            return false;
        }
        BlockState s = this.rat.level().getBlockState(c);
        return s.is(net.minecraft.world.level.block.Blocks.COMPOSTER)
                && s.getValue(ComposterBlock.LEVEL) == 8;
    }

    // is there space for at least one more bone meal
    private boolean hasBufferSpace() {
        for (int i = 0; i < this.rat.harvestInventory.getContainerSize(); i++) {
            ItemStack s = this.rat.harvestInventory.getItem(i);
            if (s.isEmpty()) {
                return true;
            }
            if (s.is(Items.BONE_MEAL) && s.getCount() < s.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    private boolean isFarFromComposter() {
        BlockPos c = this.rat.getComposterPos();
        return c != null && this.rat.distanceToSqr(c.getX() + 0.5, c.getY(), c.getZ() + 0.5) > 6 * 6;
    }

    private boolean bufferIsEmpty() {
        return this.rat.harvestInventory.isEmpty();
    }

    // true if the buffer has something that is or isnt a seed, depending on the flag
    private boolean hasBufferMatching(boolean seeds) {
        for (int i = 0; i < this.rat.harvestInventory.getContainerSize(); i++) {
            ItemStack s = this.rat.harvestInventory.getItem(i);
            if (!s.isEmpty() && isSeed(s) == seeds) {
                return true;
            }
        }
        return false;
    }

    private boolean isSeed(ItemStack stack) {
        return SEEDS.contains(stack.getItem());
    }

    // movement

    private boolean moveWithinReach(BlockPos pos) {
        double distSqr = this.rat.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        this.rat.getLookControl().setLookAt(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        if (distSqr > REACH_SQR) {
            this.rat.getNavigation().moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1.0D);
            return false;
        }
        this.rat.getNavigation().stop();
        if (this.actionCooldown > 0) {
            this.actionCooldown--;
            return false;
        }
        this.actionCooldown = 10;
        return true;
    }

    // harvesting

    @Nullable
    private BlockPos findMatureCrop() {
        BlockPos c = this.rat.getComposterPos();
        if (c == null) {
            return null;
        }
        Level level = this.rat.level();
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                c.offset(-SEARCH_RADIUS, -SEARCH_HEIGHT, -SEARCH_RADIUS),
                c.offset(SEARCH_RADIUS, SEARCH_HEIGHT, SEARCH_RADIUS))) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state)) {
                double d = this.rat.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                if (d < bestDist) {
                    bestDist = d;
                    best = pos.immutable();
                }
            }
        }
        return best;
    }

    private void harvest(BlockPos pos) {
        if (!(this.rat.level() instanceof ServerLevel server)) {
            return;
        }
        BlockState state = server.getBlockState(pos);
        if (!(state.getBlock() instanceof CropBlock crop) || !crop.isMaxAge(state)) {
            return;
        }
        List<ItemStack> drops = Block.getDrops(state, server, pos, server.getBlockEntity(pos), this.rat, ItemStack.EMPTY);
        for (ItemStack drop : drops) {
            ItemStack leftover = addToBuffer(drop);
            if (!leftover.isEmpty()) {
                net.minecraft.world.entity.item.ItemEntity ie = new net.minecraft.world.entity.item.ItemEntity(
                        server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, leftover);
                server.addFreshEntity(ie);
            }
        }
        // replant it
        server.setBlock(pos, crop.getStateForAge(0), 3);
        server.levelEvent(2001, pos, Block.getId(state));
    }

    // dropping produce off in a chest

    @Nullable
    private BlockPos findContainer() {
        BlockPos c = this.rat.getComposterPos();
        if (c == null) {
            return null;
        }
        Level level = this.rat.level();
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                c.offset(-SEARCH_RADIUS, -SEARCH_HEIGHT, -SEARCH_RADIUS),
                c.offset(SEARCH_RADIUS, SEARCH_HEIGHT, SEARCH_RADIUS))) {
            if (level.getBlockEntity(pos) instanceof Container) {
                double d = this.rat.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                if (d < bestDist) {
                    bestDist = d;
                    best = pos.immutable();
                }
            }
        }
        return best;
    }

    private void depositProduce(BlockPos chestPos) {
        if (!(this.rat.level().getBlockEntity(chestPos) instanceof Container container)) {
            return;
        }
        for (int i = 0; i < this.rat.harvestInventory.getContainerSize(); i++) {
            ItemStack stack = this.rat.harvestInventory.getItem(i);
            if (stack.isEmpty() || isSeed(stack)) {
                continue;
            }
            ItemStack leftover = insertIntoContainer(container, stack);
            this.rat.harvestInventory.setItem(i, leftover);
        }
    }

    private ItemStack insertIntoContainer(Container container, ItemStack stack) {
        // top up matching stacks first, then use empty slots
        for (int i = 0; i < container.getContainerSize() && !stack.isEmpty(); i++) {
            ItemStack slot = container.getItem(i);
            if (!slot.isEmpty() && ItemStack.isSameItemSameTags(slot, stack)
                    && slot.getCount() < slot.getMaxStackSize()) {
                int move = Math.min(stack.getCount(), slot.getMaxStackSize() - slot.getCount());
                slot.grow(move);
                stack.shrink(move);
            }
        }
        for (int i = 0; i < container.getContainerSize() && !stack.isEmpty(); i++) {
            if (container.getItem(i).isEmpty() && container.canPlaceItem(i, stack)) {
                container.setItem(i, stack.copy());
                stack.setCount(0);
            }
        }
        container.setChanged();
        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    // composting the seeds

    private void collectBonemeal(BlockPos composterPos) {
        if (!(this.rat.level() instanceof ServerLevel server)) {
            return;
        }
        BlockState state = server.getBlockState(composterPos);
        if (!state.is(net.minecraft.world.level.block.Blocks.COMPOSTER)
                || state.getValue(ComposterBlock.LEVEL) != 8) {
            return;
        }
        ItemStack leftover = addToBuffer(new ItemStack(Items.BONE_MEAL));
        server.setBlock(composterPos, state.setValue(ComposterBlock.LEVEL, 0), 3);
        server.playSound(null, composterPos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!leftover.isEmpty()) {
            net.minecraft.world.entity.item.ItemEntity ie = new net.minecraft.world.entity.item.ItemEntity(
                    server, composterPos.getX() + 0.5, composterPos.getY() + 1, composterPos.getZ() + 0.5, leftover);
            server.addFreshEntity(ie);
        }
    }

    private void compostSeeds(BlockPos composterPos) {
        if (!(this.rat.level() instanceof ServerLevel server)) {
            return;
        }
        BlockState state = server.getBlockState(composterPos);
        if (!state.is(net.minecraft.world.level.block.Blocks.COMPOSTER)) {
            return;
        }
        int level = state.getValue(ComposterBlock.LEVEL);
        if (level >= 7) {
            return; // full, wait til someone empties it
        }
        for (int i = 0; i < this.rat.harvestInventory.getContainerSize(); i++) {
            ItemStack stack = this.rat.harvestInventory.getItem(i);
            if (stack.isEmpty() || !isSeed(stack)) {
                continue;
            }
            float chance = ComposterBlock.COMPOSTABLES.getOrDefault(stack.getItem(), 0.0F);
            boolean success = level == 0 || this.rat.getRandom().nextFloat() < chance;
            if (success) {
                int newLevel = level + 1;
                server.setBlock(composterPos, state.setValue(ComposterBlock.LEVEL, newLevel), 3);
                if (newLevel == 7) {
                    server.scheduleTick(composterPos, state.getBlock(), 20);
                }
            }
            server.levelEvent(1500, composterPos, success ? 1 : 0);
            stack.shrink(1);
            return; // one item per action tick
        }
    }

    private ItemStack addToBuffer(ItemStack stack) {
        return this.rat.harvestInventory.addItem(stack);
    }
}

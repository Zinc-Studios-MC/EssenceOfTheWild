package net.mrmisc.essenceofthewild.block.entity.custom.cheesemaker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mrmisc.essenceofthewild.block.custom.cheesemaker.CheeseMakerBlock;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.mrmisc.essenceofthewild.config.EOTWConfig;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import net.mrmisc.essenceofthewild.recipe.EOTWRecipes;
import net.mrmisc.essenceofthewild.recipe.cheesemaker.CheeseMakerRecipe;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

public class CheeseMakerBlockEntity extends BlockEntity {
    private int content = CheeseMakerBlock.EMPTY;
    private int progress = 0;
    private int recipeProcessTicks = 0;
    private ItemStack inputStack = new ItemStack(EOTWItems.SHEEP_MILK_BUCKET.get());
    private ItemStack resultStack = new ItemStack(EOTWItems.SHEEP_CHEESE.get());
    private ItemStack containerStack = new ItemStack(Items.BUCKET);

    public CheeseMakerBlockEntity(BlockPos pos, BlockState state) {
        super(EOTWBlockEntities.CHEESE_MAKER.get(), pos, state);

        if (state.hasProperty(CheeseMakerBlock.CONTENT)) {
            content = state.getValue(CheeseMakerBlock.CONTENT);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CheeseMakerBlockEntity blockEntity) {
        if (blockEntity.content != CheeseMakerBlock.MILK) {
            if (blockEntity.progress != 0) {
                blockEntity.progress = 0;
                setChanged(level, pos, state);
            }

            return;
        }

        int maxProgress = blockEntity.getMaxProgress();
        int previousMilestone = blockEntity.getMilestone(maxProgress);

        blockEntity.progress++;
        blockEntity.spawnProgressEffects(level, pos, maxProgress);

        int nextMilestone = blockEntity.getMilestone(maxProgress);
        if (nextMilestone > previousMilestone && nextMilestone < 4) {
            blockEntity.playMilestoneEffects(level, pos, nextMilestone);
        }

        if (blockEntity.progress >= maxProgress) {
            blockEntity.finishCheese(level, pos);
        }

        setChanged(level, pos, state);
    }

    public boolean isEmpty() {
        return content == CheeseMakerBlock.EMPTY;
    }

    public boolean hasMilk() {
        return content == CheeseMakerBlock.MILK;
    }

    public boolean hasCheese() {
        return content == CheeseMakerBlock.CHEESE;
    }

    public int getContent() {
        return content;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return recipeProcessTicks > 0 ? recipeProcessTicks : EOTWConfig.cheeseMakerProcessTicks();
    }

    public ItemStack getInputStack() {
        return inputStack.copy();
    }

    public ItemStack getResultStack() {
        return resultStack.copy();
    }

    public ItemStack getContainerStack() {
        return containerStack.copy();
    }

    public Optional<CheeseMakerRecipe> getRecipeFor(ItemStack stack) {
        if (level == null || stack.isEmpty()) {
            return Optional.empty();
        }

        Container container = new SimpleContainer(stack);
        return level.getRecipeManager().getRecipeFor(EOTWRecipes.CHEESE_MAKER_TYPE.get(), container, level);
    }

    public void startCheese(CheeseMakerRecipe recipe, ItemStack input) {
        content = CheeseMakerBlock.MILK;
        progress = 0;
        recipeProcessTicks = recipe.getProcessTicks();
        inputStack = input.copyWithCount(1);
        resultStack = recipe.getResultItem(level == null ? RegistryAccess.EMPTY : level.registryAccess());
        containerStack = recipe.getContainer();
        setChanged();
        syncBlockState();
    }

    public void empty() {
        content = CheeseMakerBlock.EMPTY;
        progress = 0;
        recipeProcessTicks = 0;
        setChanged();
        syncBlockState();
    }

    public void giveCheese(Player player) {
        if (!hasCheese()) {
            return;
        }

        ItemStack cheese = resultStack.copy();
        if (!player.addItem(cheese)) {
            player.drop(cheese, false);
        }

        if (level != null) {
            level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 1.15F);
        }

        empty();
    }

    public void drops() {
        if (level == null) {
            return;
        }

        if (hasCheese()) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), resultStack.copy());
        }
    }

    private void finishCheese(Level level, BlockPos pos) {
        content = CheeseMakerBlock.CHEESE;
        progress = 0;

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EFFECT, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, 16, 0.3D, 0.1D, 0.3D, 0.03D);
        }

        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.9F, 1.35F);
        setChanged();
        syncBlockState();
    }

    private void spawnProgressEffects(Level level, BlockPos pos, int maxProgress) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        float progressRatio = progress / (float) maxProgress;
        int interval = Math.max(20, 100 - Mth.floor(progressRatio * 80.0F));

        if (progress % interval != 0) {
            return;
        }

        int count = 1 + Mth.floor(progressRatio * 4.0F);
        serverLevel.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, count, 0.22D, 0.03D, 0.22D, 0.01D);
        serverLevel.sendParticles(ParticleTypes.EFFECT, pos.getX() + 0.5D, pos.getY() + 0.78D, pos.getZ() + 0.5D, Math.max(1, count / 2), 0.18D, 0.02D, 0.18D, 0.015D);
    }

    private void playMilestoneEffects(Level level, BlockPos pos, int milestone) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EFFECT, pos.getX() + 0.5D, pos.getY() + 0.85D, pos.getZ() + 0.5D, 4 + milestone * 3, 0.25D, 0.08D, 0.25D, 0.025D);
        }

        level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.35F, 0.8F + milestone * 0.1F);
    }

    private int getMilestone(int maxProgress) {
        return Mth.clamp(progress * 4 / maxProgress, 0, 4);
    }

    private void syncBlockState() {
        if (level == null || level.isClientSide) {
            return;
        }

        BlockState state = getBlockState();
        if (!state.hasProperty(CheeseMakerBlock.CONTENT) || state.getValue(CheeseMakerBlock.CONTENT) == content) {
            return;
        }

        level.setBlock(worldPosition, state.setValue(CheeseMakerBlock.CONTENT, content), Block.UPDATE_CLIENTS);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("Content", content);
        tag.putInt("Progress", progress);
        tag.putInt("RecipeProcessTicks", recipeProcessTicks);
        tag.put("Input", inputStack.save(new CompoundTag()));
        tag.put("Result", resultStack.save(new CompoundTag()));
        tag.put("Container", containerStack.save(new CompoundTag()));

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        content = Mth.clamp(tag.contains("Content") ? tag.getInt("Content") : getContentFromState(), CheeseMakerBlock.EMPTY, CheeseMakerBlock.CHEESE);
        progress = content == CheeseMakerBlock.MILK ? Math.max(0, tag.getInt("Progress")) : 0;
        recipeProcessTicks = Math.max(0, tag.getInt("RecipeProcessTicks"));
        inputStack = tag.contains("Input") ? ItemStack.of(tag.getCompound("Input")) : new ItemStack(EOTWItems.SHEEP_MILK_BUCKET.get());
        resultStack = tag.contains("Result") ? ItemStack.of(tag.getCompound("Result")) : new ItemStack(EOTWItems.SHEEP_CHEESE.get());
        containerStack = tag.contains("Container") ? ItemStack.of(tag.getCompound("Container")) : new ItemStack(Items.BUCKET);
    }

    private int getContentFromState() {
        BlockState state = getBlockState();
        return state.hasProperty(CheeseMakerBlock.CONTENT) ? state.getValue(CheeseMakerBlock.CONTENT) : CheeseMakerBlock.EMPTY;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}

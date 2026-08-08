package net.mrmisc.essenceofthewild.block.entity.custom.nest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.mrmisc.essenceofthewild.config.EOTWConfig;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;
import net.mrmisc.essenceofthewild.item.EOTWItems;
import org.jetbrains.annotations.Nullable;

public class NestBlockEntity extends BlockEntity {
    private int eggCount = 0;
    private int hatchProgress = 0;
    private int animationTicks = 0;
    @Nullable
    private ResourceLocation hatchEntityId;
    private String variantId = "";

    public NestBlockEntity(BlockPos pos, BlockState state) {
        super(EOTWBlockEntities.NEST.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, NestBlockEntity blockEntity) {
        if (level.isClientSide) {
            blockEntity.animationTicks++;
            return;
        }

        if (!blockEntity.hasEggs()) {
            return;
        }

        int maxProgress = blockEntity.getMaxHatchTicks();
        int previousMilestone = blockEntity.getMilestone(maxProgress);

        blockEntity.hatchProgress++;
        blockEntity.playEggMovementEffects(level, pos, maxProgress);

        int nextMilestone = blockEntity.getMilestone(maxProgress);
        if (nextMilestone > previousMilestone && nextMilestone < 4) {
            blockEntity.playMilestoneEffects(level, pos, nextMilestone);
        }

        if (blockEntity.hatchProgress >= maxProgress) {
            blockEntity.hatch((ServerLevel) level, pos);
        } else if (blockEntity.hatchProgress % 20 == 0) {
            blockEntity.syncState();
        }

        setChanged(level, pos, state);
    }

    public boolean hasEggs() {
        return eggCount > 0;
    }

    public boolean canAcceptClutch() {
        return !hasEggs();
    }

    public boolean addClutch(Animal parent, int count) {
        if (!canAcceptClutch()) {
            return false;
        }

        eggCount = Mth.clamp(count, 1, 3);
        hatchProgress = 0;
        animationTicks = 0;
        hatchEntityId = ForgeRegistries.ENTITY_TYPES.getKey(parent.getType());
        variantId = parent instanceof VariantCarrier carrier ? carrier.getVariantId() : "";

        setChanged();
        syncState();
        return true;
    }

    public int getEggCount() {
        return eggCount;
    }

    // takes the whole clutch out and hands it back as a stack, duck eggs for duck nests and normal
    // ones otherwise, gives back EMPTY if theres nothing in there
    public ItemStack collectEggs() {
        if (!hasEggs()) {
            return ItemStack.EMPTY;
        }
        Item eggItem = isDuckClutch() ? EOTWItems.DUCK_EGG.get() : Items.EGG;
        ItemStack stack = new ItemStack(eggItem, eggCount);
        clearClutch();
        return stack;
    }

    private boolean isDuckClutch() {
        EntityType<?> type = hatchEntityId == null ? null : ForgeRegistries.ENTITY_TYPES.getValue(hatchEntityId);
        return type == EOTWEntities.DUCK.get();
    }

    public int getHatchProgress() {
        return hatchProgress;
    }

    public int getMaxHatchTicks() {
        return EOTWConfig.nestHatchTicks();
    }

    public int getRemainingHatchTicks() {
        return Math.max(0, getMaxHatchTicks() - hatchProgress);
    }

    public int getAnimationTicks() {
        return animationTicks;
    }

    public float getHatchProgressRatio() {
        return hasEggs() ? Mth.clamp(hatchProgress / (float) getMaxHatchTicks(), 0.0F, 1.0F) : 0.0F;
    }

    @Nullable
    public ResourceLocation getHatchEntityId() {
        return hatchEntityId;
    }

    private void hatch(ServerLevel level, BlockPos pos) {
        EntityType<?> type = hatchEntityId == null ? null : ForgeRegistries.ENTITY_TYPES.getValue(hatchEntityId);

        if (type != null) {
            for (int i = 0; i < eggCount; i++) {
                Entity entity = type.create(level);

                if (entity instanceof AgeableMob baby) {
                    baby.setBaby(true);
                }

                if (entity instanceof VariantCarrier carrier && !variantId.isEmpty()) {
                    carrier.setVariantById(variantId);
                }

                if (entity != null) {
                    double x = pos.getX() + 0.35D + level.random.nextDouble() * 0.3D;
                    double z = pos.getZ() + 0.35D + level.random.nextDouble() * 0.3D;
                    entity.moveTo(x, pos.getY() + 0.45D, z, level.random.nextFloat() * 360.0F, 0.0F);
                    level.addFreshEntity(entity);
                }
            }
        }

        level.sendParticles(ParticleTypes.POOF, pos.getX() + 0.5D, pos.getY() + 0.45D, pos.getZ() + 0.5D, 18, 0.22D, 0.12D, 0.22D, 0.02D);
        level.playSound(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.85F, 1.15F);
        clearClutch();
    }

    private void clearClutch() {
        eggCount = 0;
        hatchProgress = 0;
        hatchEntityId = null;
        variantId = "";
        setChanged();
        syncState();
    }

    private void playEggMovementEffects(Level level, BlockPos pos, int maxProgress) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        float progressRatio = hatchProgress / (float) maxProgress;
        int interval = Math.max(8, 90 - Mth.floor(progressRatio * 78.0F));

        if (hatchProgress % interval != 0) {
            return;
        }

        int count = 1 + Mth.floor(progressRatio * 4.0F);
        serverLevel.sendParticles(ParticleTypes.EFFECT, pos.getX() + 0.5D, pos.getY() + 0.52D, pos.getZ() + 0.5D, count, 0.16D, 0.05D, 0.16D, 0.015D);
        level.playSound(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.18F + progressRatio * 0.25F, 0.75F + progressRatio * 0.45F);
    }

    private void playMilestoneEffects(Level level, BlockPos pos, int milestone) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EFFECT, pos.getX() + 0.5D, pos.getY() + 0.58D, pos.getZ() + 0.5D, 3 + milestone * 2, 0.22D, 0.06D, 0.22D, 0.02D);
        }

        level.playSound(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.35F, 0.8F + milestone * 0.12F);
    }

    private int getMilestone(int maxProgress) {
        return Mth.clamp(hatchProgress * 4 / maxProgress, 0, 4);
    }

    private void syncState() {
        if (level == null || level.isClientSide) {
            return;
        }

        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("EggCount", eggCount);
        tag.putInt("HatchProgress", hatchProgress);
        tag.putString("Variant", variantId);

        if (hatchEntityId != null) {
            tag.putString("HatchEntity", hatchEntityId.toString());
        }

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        eggCount = Mth.clamp(tag.getInt("EggCount"), 0, 3);
        hatchProgress = Math.max(0, tag.getInt("HatchProgress"));
        variantId = tag.getString("Variant");
        hatchEntityId = null;

        if (tag.contains("HatchEntity")) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString("HatchEntity"));
            if (id != null) {
                hatchEntityId = id;
            }
        }

        if (eggCount <= 0) {
            hatchProgress = 0;
        }
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

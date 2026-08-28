package net.mrmisc.essenceofthewild.entity.custom.ferret;

import net.mrmisc.essenceofthewild.entity.util.LevelBiomeQuery;
import net.mrmisc.essenceofthewild.entity.util.VariantSlot;
import net.mrmisc.essenceofthewild.entity.util.Variant;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.network.NetworkHooks;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.VariantCarrier;
import net.mrmisc.essenceofthewild.menu.ferret.FerretMenu;
import net.mrmisc.essenceofthewild.util.EOTWEntityUtils;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FerretEntity extends TamableAnimal implements MenuProvider, VariantCarrier, GeoEntity {
    public static final int INVENTORY_SIZE = 9;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.ferret.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.ferret.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.ferret.run");
    private static final RawAnimation DIG = RawAnimation.begin().thenPlayAndHold("animation.ferret.dig");
    private static final RawAnimation DIG_OUT = RawAnimation.begin().thenPlayAndHold("animation.ferret.dig_out");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Boolean> RUNNING = 
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
        
    private static final EntityDataAccessor<Boolean> DIGGING_IN =
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DIGGING_OUT =
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> TO_DIG =
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> PRIMED_TO_DIG =
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.INT);

    private final VariantSlot<Variant> variant = new VariantSlot<>(this.entityData, VARIANT, FerretVariants.SET);
    
    public int ticks = 0;

    private final SimpleContainer inventory = new SimpleContainer(INVENTORY_SIZE) {
        @Override
        public void setChanged() {
            super.setChanged();
            FerretEntity.this.setPersistenceRequired();
        }
    };

    public FerretEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
        .add(Attributes.MAX_HEALTH, 14)
        .add(Attributes.MOVEMENT_SPEED, (double)0.5F)
        .add(Attributes.FOLLOW_RANGE, 25)
        .add(Attributes.ATTACK_DAMAGE, 4)
        .add(Attributes.ATTACK_KNOCKBACK, 1)
        .add(Attributes.ATTACK_SPEED, 1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new FerretPanicGoal(this, 1));
        this.goalSelector.addGoal(1, new FindOrDigBurrowsGoal(this));
        this.goalSelector.addGoal(1, new DigGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));

        this.goalSelector.addGoal(2, new TemptGoal(this, 0.5D, Ingredient.of(Items.RABBIT), false));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));

        this.goalSelector.addGoal(3, new FollowParentGoal(this, 0.7D));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(3, new AttackRabbitGoal(this, 1, true));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FerretFollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, Monster.class, false));
    }

    @Override
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        FerretEntity child = new FerretEntity(EOTWEntities.FERRET.get(), pLevel);
        child.setVariant(this.getVariant());
        return child;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.RABBIT);
    }
    @Override
    public void finalizeSpawnChildFromBreeding(ServerLevel pLevel, Animal pAnimal, AgeableMob pBaby) {
        super.finalizeSpawnChildFromBreeding(pLevel, pAnimal, pBaby);
        pBaby.setAge(-18000);
    }

    @Override
    public void tick() {
        super.tick();
        if(!this.level().isClientSide()){
            if(!this.isBaby()){
                dayTick();
            }
        }
    }


    private void dayTick(){
        if(this.level().isDay()){
            if(ticks == 0 && this.isDiggingOut()){
                this.setDiggingOut(false);
                this.setNoAi(false);
            }else if(ticks > 0 && this.isDiggingOut()){
                this.setNoAi(true);
                this.moveTo(this.getX(), this.getY()+0.05, this.getZ(), 0, 0);
                --ticks;
            }
            else{
                --ticks;
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, this::movementController));
    }

    private PlayState movementController(AnimationState<FerretEntity> state) {
        if(this.isDiggingOut()){
            return state.setAndContinue(DIG_OUT);
        }
        if(this.isDiggingIn()){
            return state.setAndContinue(DIG);
        }
        if(!state.isMoving()){
            return state.setAndContinue(IDLE);
        }
        return state.setAndContinue(this.isRunning() ? RUN : WALK);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING){
            f = Math.min(pPartialTick * 6f, 1f);
        }else{
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.level().isClientSide) {
            if (this.isTame() && this.isOwnedBy(pPlayer)) {
                return InteractionResult.SUCCESS;
            } else {
                return !this.isFood(itemstack) || !(this.getHealth() < this.getMaxHealth()) && this.isTame() ? InteractionResult.PASS : InteractionResult.SUCCESS;
            }
        }
        else {
            if (!this.isTame()) {
                //TODO: make it 5-9 rabbit
                if (this.isFood(itemstack)) {
                    this.usePlayerItem(pPlayer, pHand, itemstack);
                    if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                        this.tame(pPlayer);
                        this.level().broadcastEntityEvent(this, (byte)7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte)6);
                    }
    
                    this.setPersistenceRequired();
                    return InteractionResult.CONSUME;
                }
            }

            if (this.isTame() && this.isOwnedBy(pPlayer)) {
                if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                    this.heal((float) itemstack.getFoodProperties(this).getNutrition());
                    this.usePlayerItem(pPlayer, pHand, itemstack);
                    this.setPersistenceRequired();
                    return InteractionResult.CONSUME;
                }
                if(!itemstack.is(Items.STICK)){
                    openInventory(pPlayer);
                }
                this.setPersistenceRequired();
                if(itemstack.is(Items.STICK)){
                    EOTWEntityUtils.setFerretClicked(this, pPlayer);
                    this.setShouldDig(true);
                }
                return InteractionResult.CONSUME;
            }

            InteractionResult interactionresult1 = super.mobInteract(pPlayer, pHand);
            if (interactionresult1.consumesAction()) {
                this.setPersistenceRequired();
            }
            return interactionresult1;
        }
    }

    private void openInventory(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, this, buffer -> buffer.writeInt(getId()));
        }
    }

    public Container getInventory() {
        return inventory;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new FerretMenu(id, inventory, this.inventory, this);
    }
    
    public void setRunning(boolean running){
        this.entityData.set(RUNNING, running);
    }
    public boolean isRunning(){
        return this.entityData.get(RUNNING);
    }

    public void setDiggingIn(boolean bool){
        this.entityData.set(DIGGING_IN, bool);
    }
    public void setDiggingOut(boolean bool){
        this.entityData.set(DIGGING_OUT, bool);
    }
    public boolean isDiggingIn(){
        return this.entityData.get(DIGGING_IN);
    }
    public boolean isDiggingOut(){
        return this.entityData.get(DIGGING_OUT);
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(RUNNING, false);
        this.entityData.define(DIGGING_IN, false);
        this.entityData.define(DIGGING_OUT, false);
        this.entityData.define(TO_DIG, new BlockPos(0,0,0));
        this.entityData.define(PRIMED_TO_DIG, false);
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty,
            MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pDataTag != null && pDataTag.contains("Variant")) {
            variant.load(pDataTag);
            return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        }
        setVariant(pickVariant(pLevel.getLevel(), blockPosition()));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public CompoundTag saveInventoryToTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("Items", createInventoryTag());
        return tag;
    }

    public void loadInventoryFromTag(CompoundTag tag) {
        if (tag.contains("Items", Tag.TAG_LIST)) {
            loadInventoryTag(tag.getList("Items", Tag.TAG_COMPOUND));
        }
    }

    private ListTag createInventoryTag() {
        ListTag listTag = new ListTag();

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);

            if (!stack.isEmpty()) {
                CompoundTag stackTag = stack.save(new CompoundTag());
                stackTag.putByte("Slot", (byte) slot);
                listTag.add(stackTag);
            }
        }

        return listTag;
    }

    private void loadInventoryTag(ListTag listTag) {
        inventory.clearContent();

        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag stackTag = listTag.getCompound(i);
            int slot = stackTag.contains("Slot", Tag.TAG_BYTE) ? stackTag.getByte("Slot") & 255 : i;

            if (slot >= 0 && slot < inventory.getContainerSize()) {
                inventory.setItem(slot, ItemStack.of(stackTag));
            }
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);

            if (!stack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(stack)) {
                spawnAtLocation(stack);
            }

            inventory.setItem(slot, ItemStack.EMPTY);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("Inventory", createInventoryTag());
        variant.save(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.contains("Inventory", Tag.TAG_LIST)) {
            loadInventoryTag(pCompound.getList("Inventory", Tag.TAG_COMPOUND));
        }
        variant.load(pCompound);
    }
    @Override
    public boolean isInWall() {
        if (this.noPhysics) {
         return false;
      } else {
         float f = this.getDimensions(this.getPose()).width * 0.8F;
         AABB aabb = AABB.ofSize(this.getEyePosition(), (double)f, 1.0E-6D, (double)f);
         return BlockPos.betweenClosedStream(aabb).anyMatch((p_201942_) -> {
            BlockState blockstate = this.level().getBlockState(p_201942_);
            if(
                blockstate.is(EOTWBlocks.DIRT_BURROW_BLOCK.get()) ||
                blockstate.is(EOTWBlocks.SAND_BURROW_BLOCK.get()) ||
                blockstate.is(EOTWBlocks.MUD_BURROW_BLOCK.get()) ||
                this.isDiggingIn()
            ){
                return false;
            }
            return !blockstate.isAir() && blockstate.isSuffocating(this.level(), p_201942_) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level(), p_201942_).move((double)p_201942_.getX(), (double)p_201942_.getY(), (double)p_201942_.getZ()), Shapes.create(aabb), BooleanOp.AND);
         });
      }
    }

    public void setBlockToDig(BlockPos pos){
        this.entityData.set(TO_DIG, pos);
    }
    public BlockPos getBlockToDig(){
        return this.entityData.get(TO_DIG);
    }
    public void setShouldDig(boolean bool){
        this.entityData.set(PRIMED_TO_DIG, bool);
    }
    public boolean shouldDig(){
        return this.entityData.get(PRIMED_TO_DIG);
    }
    @Override
    public void die(DamageSource pCause) {
        if(this.getOwner()!=null){
            this.getOwner().getPersistentData().remove("OwnsFerret");
        }
        super.die(pCause);
    }
    @Override
    public void setOwnerUUID(UUID pUuid) {
        super.setOwnerUUID(pUuid);
        this.getOwner().getPersistentData().putBoolean("OwnsFerret", true);
    }

    public Variant getVariant() {
        return variant.get();
    }

    public void setVariant(Variant v) {
        variant.set(v);
    }

    @Override
    public String getVariantId() {
        return variant.id();
    }

    @Override
    public void setVariantById(String id) {
        variant.setById(id);
    }

    private Variant pickVariant(Level level, BlockPos pos) {
        return FerretVariants.pick(new LevelBiomeQuery(level, pos), level.random::nextBoolean);
    }
}
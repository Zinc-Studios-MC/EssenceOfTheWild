package net.mrmisc.essenceofthewild.entity.custom.ferret;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
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
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.network.NetworkHooks;
import net.mrmisc.essenceofthewild.block.EOTWBlocks;
import net.mrmisc.essenceofthewild.block.entity.custom.burrow.BurrowBlockEntity;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.util.MobVariant;
import net.mrmisc.essenceofthewild.menu.ferret.FerretMenu;
import net.mrmisc.essenceofthewild.util.EOTWEntityUtils;

public class FerretEntity extends TamableAnimal implements MenuProvider {
    public static final int INVENTORY_SIZE = 9;

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final AnimationState diggingInAnimationState = new AnimationState();
    private int diggingInAnimationTimeout = 0;
    public final AnimationState diggingOutAnimationState = new AnimationState();
    private int diggingOutAnimationTimeout = 0;

    private static final EntityDataAccessor<Boolean> RUNNING = 
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    
    private static final EntityDataAccessor<Integer> VARIANT =
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.INT);
    
    private static final EntityDataAccessor<Boolean> DIGGING_IN =
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DIGGING_OUT =
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> TO_DIG =
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> PRIMED_TO_DIG =
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);

    private static BlockPos foundBurrow = null;
    private static boolean wasDigging = false;
    
    public int ticks = 0;
    public int diggingTicks = 0;

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
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));

        this.goalSelector.addGoal(2, new TemptGoal(this, 0.5D, Ingredient.of(Items.RABBIT), false));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));

        this.goalSelector.addGoal(3, new FollowParentGoal(this, 0.7D));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());

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
        return EOTWEntities.FERRET.get().create(pLevel);
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
        if(this.level().isClientSide){
            setupAnimationStates();
        }
        if(!this.level().isClientSide()){
            if(!this.isBaby()){
                dayTick();
                //TODO: move these to their own goals, I'm too lazy to do it right now
                nightTicks();
                shouldDigTicks();
            }
        }
    }

    private void shouldDigTicks(){
        if(this.shouldDig()){
            BlockPos pos = this.getBlockToDig();
            if(pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0){
                return;
            }
            BlockState state = this.level().getBlockState(pos);
            if(!(this.inventory.hasAnyOf(Set.of(Items.GOLDEN_CARROT)))){
                if(!(state.is(Blocks.SUSPICIOUS_SAND) || state.is(Blocks.SUSPICIOUS_GRAVEL))){
                    return;
                }
            }
            AABB bound = this.getBoundingBox().inflate(1);
            if(!bound.contains(pos.getX(), pos.getY(), pos.getZ())){
                this.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1);
                return;
            }
            if(diggingTicks <= 0){
                this.setDiggingIn(true);
                this.diggingTicks = 30;
            }
            else if (diggingTicks == 1){
                if(state.is(Blocks.SUSPICIOUS_SAND) || state.is(Blocks.SUSPICIOUS_GRAVEL)){
                    BrushableBlockEntity be = (BrushableBlockEntity)this.level().getBlockEntity(pos);

                    ItemStack stack = new ItemStack(Items.AIR);

                    be.saveToItem(stack);

                    String tagString = stack.getTag().get("BlockEntityTag").getAsString();

                    String s = tagString.substring(tagString.indexOf("\"")+1, tagString.indexOf("\","));

                    LootTable loottable = this.level().getServer().getLootData().getLootTable(ResourceLocation.tryParse(s));

                    ObjectArrayList<ItemStack> items = loottable.getRandomItems((new LootParams.Builder((ServerLevel)this.level())).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.getOnPos().above())).withLuck(((Player)this.getOwner()).getLuck()).withParameter(LootContextParams.THIS_ENTITY, (Player)this.getOwner()).create(LootContextParamSets.CHEST));
                    ItemStack itemstack;
                    switch (items.size()) {
                        case 0:
                        itemstack = ItemStack.EMPTY;
                        break;
                        case 1:
                        itemstack = items.get(0);
                        break;
                        default:
                        itemstack = items.get(0);
                    }
                    this.level().destroyBlock(pos, false);
                    if(state.is(Blocks.SUSPICIOUS_SAND)){
                        this.level().setBlock(pos, Blocks.SAND.defaultBlockState(), 2);
                    }
                    else if(state.is(Blocks.SUSPICIOUS_GRAVEL)){
                        this.level().setBlock(pos, Blocks.GRAVEL.defaultBlockState(), 2);
                    }
                    for(int i = 0; i < this.getInventory().getContainerSize(); i++){
                        ItemStack it = this.getInventory().getItem(i);
                        if(it.is(Items.AIR)){
                            this.getInventory().setItem(i, itemstack);
                            break;
                        }else if(it.is(itemstack.getItem())){
                            it.setCount(it.getCount()+itemstack.getCount());
                            break;
                        }
                    }
                    if(!this.getInventory().hasAnyOf(Set.of(itemstack.getItem()))){
                        ItemEntity ite = new ItemEntity(this.level(), this.getX(), this.getY()+1, this.getZ(), itemstack);
                        this.level().addFreshEntity(ite);
                    }
                }else{
                    this.level().destroyBlock(pos, true);
                }
                this.setDiggingIn(false);
                this.setShouldDig(false);
                --this.diggingTicks;
            }else{
                --this.diggingTicks;
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

    private void nightTicks(){
        if (this.level().isNight()) {
            if(foundBurrow == null){
                AABB aabb = this.getBoundingBox().inflate(20);
                for (BlockPos pos : BlockPos.betweenClosed(
                        Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ),
                        Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))
                    ) {
                    BlockState state = this.level().getBlockState(pos);
                    if (state.is(EOTWBlocks.DIRT_BURROW_BLOCK.get()) ||
                        state.is(EOTWBlocks.SAND_BURROW_BLOCK.get()) ||
                        state.is(EOTWBlocks.MUD_BURROW_BLOCK.get())
                    ) {
                        BurrowBlockEntity be = (BurrowBlockEntity)level().getBlockEntity(pos);
                        if(be.canAddFerret()){
                            foundBurrow = pos.immutable();
                            break;
                        }
                    }
                }
            }
            if(foundBurrow == null){
                if(ticks <= 0){
                    this.setDiggingIn(true);
                    this.ticks = 30;
                    this.setNoAi(true);//become a vegitable to let the digging animation play nice
                    wasDigging = true;
                }else if(this.ticks > 1 && this.ticks < 25){
                    this.moveTo(this.getX(), this.getY()-0.02, this.getZ(), 0, 0);
                    --this.ticks;
                }
                else if (ticks == 1){
                    if(this.level().getBlockState(this.getOnPos()).is(Blocks.DIRT)){
                        this.level().setBlock(this.getOnPos(), EOTWBlocks.DIRT_BURROW_BLOCK.get().defaultBlockState(), 2);
                    }
                    else if(this.level().getBlockState(this.getOnPos()).is(Blocks.SAND)){
                        this.level().setBlock(this.getOnPos(), EOTWBlocks.SAND_BURROW_BLOCK.get().defaultBlockState(), 2);
                    }
                    else if(this.level().getBlockState(this.getOnPos()).is(Blocks.MUD)){
                        this.level().setBlock(this.getOnPos(), EOTWBlocks.MUD_BURROW_BLOCK.get().defaultBlockState(), 2);
                    }
                    else{
                        this.level().setBlock(this.getOnPos(), EOTWBlocks.DIRT_BURROW_BLOCK.get().defaultBlockState(), 2);
                    }
                    if (level().getBlockEntity(this.getOnPos()) instanceof BurrowBlockEntity bbe && bbe.addFerret(this)) {
                        this.ticks = 0;
                        this.remove(RemovalReason.DISCARDED);
                    } else {
                        this.setDiggingIn(false);
                        this.setNoAi(false);
                        this.ticks = 0;
                    }
                }else{
                    --this.ticks;
                }
            }else{
                if(wasDigging){
                    this.setDiggingIn(false);
                    this.setNoAi(false);
                    this.ticks = 0;
                    wasDigging = true;
                }
                AABB bound = this.getBoundingBox().inflate(1);
                if(!bound.contains(foundBurrow.getX(), foundBurrow.getY(), foundBurrow.getZ())){
                    this.getNavigation().moveTo(foundBurrow.getX(), foundBurrow.getY(), foundBurrow.getZ(), 1);
                    return;
                }
                if(diggingTicks <= 0){
                    this.setDiggingIn(true);
                    this.diggingTicks = 30;
                }
                else if (diggingTicks == 1){
                    if (level().getBlockEntity(foundBurrow) instanceof BurrowBlockEntity bbe && bbe.addFerret(this)) {
                        foundBurrow = null;
                        this.ticks = 0;
                        this.setDiggingIn(false);
                        this.setShouldDig(false);
                        this.remove(RemovalReason.DISCARDED);
                    }
                    --this.diggingTicks;
                }else{
                    --this.diggingTicks;
                }
            }
        }
    }

    private void setupAnimationStates(){
        if(this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = this.random.nextInt(40)+80;
            this.idleAnimationState.start(this.tickCount);
        }else{
            --this.idleAnimationTimeout;
        }
        if(this.isDiggingIn() && diggingInAnimationTimeout <=0){
            diggingInAnimationTimeout = 30;
            diggingInAnimationState.start(this.tickCount);
        }else{
            --this.diggingInAnimationTimeout;
        }
        if(this.isDiggingOut() && diggingOutAnimationTimeout <=0){
            diggingOutAnimationTimeout = 28;
            diggingOutAnimationState.start(this.tickCount);
        }else{
            --this.diggingOutAnimationTimeout;
        }
        if(!this.isDiggingIn()){
            diggingInAnimationState.stop();
        }
        if(!this.isDiggingOut()){
            diggingOutAnimationState.stop();
        }
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

    public MobVariant getVariant() {
        int i = this.entityData.get(VARIANT);
        return (i >= 0 && i < FerretVariants.ALL.size())
                ? FerretVariants.ALL.get(i)
                : FerretVariants.ALL.get(0);
    }

    private MobVariant pickVariant(Level level, BlockPos pos) {
        if (level.getBiome(pos).is(Biomes.FOREST) || 
            level.getBiome(pos).is(Biomes.BIRCH_FOREST) ||
            level.getBiome(pos).is(Biomes.FLOWER_FOREST)
            ) {
            return FerretVariants.BASIC;
        } else if (
            level.getBiome(pos).is(Biomes.OLD_GROWTH_SPRUCE_TAIGA) || 
            level.getBiome(pos).is(Biomes.OLD_GROWTH_PINE_TAIGA)) {
            return FerretVariants.RED_FERRET;
        } else if (
            level.getBiome(pos).is(Biomes.TAIGA) || 
            level.getBiome(pos).is(Biomes.SNOWY_TAIGA)) {
            return FerretVariants.WHITE_FERRET;
        }
        return level.random.nextBoolean() ? FerretVariants.BASIC : FerretVariants.RED_FERRET;
    }

    public void setRunning(boolean running){
        this.entityData.set(RUNNING, running);
    }
    public boolean isRunning(){
        return this.entityData.get(RUNNING);
    }

    public void setVariant(MobVariant variant) {
        this.entityData.set(VARIANT, FerretVariants.ALL.indexOf(variant));
    }

    private void setVariantById(String id) {
        for (int i = 0; i < FerretVariants.ALL.size(); i++) {
            if (FerretVariants.ALL.get(i).id().equals(id)) {
                this.entityData.set(VARIANT, i);
                return;
            }
        }
        this.entityData.set(VARIANT, 0);
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
        this.entityData.define(VARIANT, 0);
        this.entityData.define(DIGGING_IN, false);
        this.entityData.define(DIGGING_OUT, false);
        this.entityData.define(TO_DIG, new BlockPos(0,0,0));
        this.entityData.define(PRIMED_TO_DIG, false);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty,
            MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pDataTag == null || !pDataTag.contains("Variant")) {
            this.setVariant(pickVariant(pLevel.getLevel(), this.blockPosition()));
        }
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
        pCompound.putString("Variant", getVariant().id());
        pCompound.put("Inventory", createInventoryTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariantById(pCompound.getString("Variant"));

        if (pCompound.contains("Inventory", Tag.TAG_LIST)) {
            loadInventoryTag(pCompound.getList("Inventory", Tag.TAG_COMPOUND));
        }
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
}
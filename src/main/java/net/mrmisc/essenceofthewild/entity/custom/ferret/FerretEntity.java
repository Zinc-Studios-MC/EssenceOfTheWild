package net.mrmisc.essenceofthewild.entity.custom.ferret;

import javax.annotation.Nullable;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;

public class FerretEntity extends TamableAnimal{

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    private static final EntityDataAccessor<Boolean> RUNNING = 
        SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN); 

    public FerretEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
        .add(Attributes.MAX_HEALTH, 14)
        .add(Attributes.MOVEMENT_SPEED, (double)0.5F)
        .add(Attributes.FOLLOW_RANGE, 25);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new PanicGoal(this, 0.4D));

        this.goalSelector.addGoal(2, new TemptGoal(this, 0.5D, Ingredient.of(Items.RABBIT), false));

        this.goalSelector.addGoal(3, new FollowParentGoal(this, 0.5D));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FerretFollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
    }

    @Override
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return EOTWEntities.FERRET.get().create(pLevel);
    }

    @Override
    public boolean canBreed() {
        //false cause no baby
        return false;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.RABBIT);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide){
            setupAnimationStates();
        }
    }

    private void setupAnimationStates(){
        if(this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = this.random.nextInt(40)+80;
            this.idleAnimationState.start(this.tickCount);
        }else{
            --this.idleAnimationTimeout;
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
        Item item = itemstack.getItem();
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
            InteractionResult interactionresult1 = super.mobInteract(pPlayer, pHand);
            if (interactionresult1.consumesAction()) {
                this.setPersistenceRequired();
            }
            return interactionresult1;
        }
    }

    public void setRunning(boolean running){
        this.entityData.set(RUNNING, running);
    }
    public boolean isRunning(){
        return this.entityData.get(RUNNING);
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(RUNNING, false);
    }
}
package net.mrmisc.essenceofthewild.entity.custom.spider;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SpiderEntity extends Spider implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.spider.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.spider.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.spider.run");
    private static final RawAnimation DEATH = RawAnimation.begin().thenPlayAndHold("animation.spider.death");

    private static final int POISON_SECONDS_NORMAL = 5;
    private static final int POISON_SECONDS_HARD = 10;

    private static final EntityDataAccessor<Boolean> CHASING =
            SynchedEntityData.defineId(SpiderEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public SpiderEntity(EntityType<? extends Spider> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHASING, false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, this::movementController));
    }

    private PlayState movementController(AnimationState<SpiderEntity> state) {
        if (this.isDeadOrDying()) {
            return state.setAndContinue(DEATH);
        }
        if (!state.isMoving()) {
            return state.setAndContinue(IDLE);
        }
        return state.setAndContinue(this.isChasing() ? RUN : WALK);
    }

    public boolean isChasing() {
        return this.entityData.get(CHASING);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            boolean chasing = this.getTarget() != null;
            if (chasing != this.isChasing()) {
                this.entityData.set(CHASING, chasing);
            }
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (!super.doHurtTarget(target)) {
            return false;
        }
        if (target instanceof LivingEntity living) {
            int seconds = poisonSeconds();
            if (seconds > 0) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, seconds * 20, 0), this);
            }
        }
        return true;
    }

    private int poisonSeconds() {
        Difficulty difficulty = this.level().getDifficulty();
        if (difficulty == Difficulty.HARD) {
            return POISON_SECONDS_HARD;
        }
        return difficulty == Difficulty.NORMAL ? POISON_SECONDS_NORMAL : 0;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}

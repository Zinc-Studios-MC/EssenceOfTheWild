package net.mrmisc.essenceofthewild.entity.custom.cave_spider;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mrmisc.essenceofthewild.entity.misc.SilkBall;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CaveSpiderEntity extends CaveSpider implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.cave_spider.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.cave_spider.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.cave_spider.run");
    private static final RawAnimation DEATH = RawAnimation.begin().thenPlayAndHold("animation.cave_spider.death");
    private static final RawAnimation SPIT = RawAnimation.begin().thenPlayAndHold("animation.cave_spider.spit");

    private static final double MUZZLE_FORWARD = 0.4D;
    private static final float SILK_BALL_VELOCITY = 0.85F;
    private static final float SILK_BALL_INACCURACY = 6.0F;

    private static final EntityDataAccessor<Boolean> CHASING =
            SynchedEntityData.defineId(CaveSpiderEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SPITTING =
            SynchedEntityData.defineId(CaveSpiderEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public CaveSpiderEntity(EntityType<? extends CaveSpider> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new SilkBallAttackGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHASING, false);
        this.entityData.define(SPITTING, false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, this::movementController));
    }

    private PlayState movementController(AnimationState<CaveSpiderEntity> state) {
        if (this.isDeadOrDying()) {
            return state.setAndContinue(DEATH);
        }
        if (this.isSpitting()) {
            return state.setAndContinue(SPIT);
        }
        if (!state.isMoving()) {
            return state.setAndContinue(IDLE);
        }
        return state.setAndContinue(this.isChasing() ? RUN : WALK);
    }

    public boolean isChasing() {
        return this.entityData.get(CHASING);
    }

    public boolean isSpitting() {
        return this.entityData.get(SPITTING);
    }

    public void setSpitting(boolean spitting) {
        this.entityData.set(SPITTING, spitting);
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

    public void spitSilkBall(LivingEntity target) {
        Vec3 muzzle = this.getEyePosition().add(this.getLookAngle().scale(MUZZLE_FORWARD));
        SilkBall ball = new SilkBall(this.level(), this);
        ball.setPos(muzzle.x, muzzle.y, muzzle.z);

        double dx = target.getX() - muzzle.x;
        double dz = target.getZ() - muzzle.z;
        double dy = target.getY(0.5D) - muzzle.y;
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        ball.shoot(dx, dy + horizontal * 0.15D, dz, SILK_BALL_VELOCITY, SILK_BALL_INACCURACY);

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.SPIDER_HURT, SoundSource.HOSTILE, 0.8F, 1.6F);
        this.level().addFreshEntity(ball);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}

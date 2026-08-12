package net.mrmisc.essenceofthewild.entity.misc;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.mrmisc.essenceofthewild.effect.EOTWEffects;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;

import org.jetbrains.annotations.NotNull;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SilkBall extends ThrowableProjectile implements GeoEntity {

    private static final float IMPACT_DAMAGE = 1.0F;
    private static final int WEBBED_TICKS = 20 * 8;
    private static final float GRAVITY = 0.015F;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public SilkBall(EntityType<? extends SilkBall> type, Level level) {
        super(type, level);
    }

    public SilkBall(Level level, LivingEntity shooter) {
        super(EOTWEntities.SILK_BALL.get(), shooter, level);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    protected float getGravity() {
        return GRAVITY;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        Entity hit = result.getEntity();
        Entity owner = this.getOwner();
        hit.hurt(this.damageSources().mobProjectile(this, owner instanceof LivingEntity living ? living : null),
                IMPACT_DAMAGE);
        if (hit instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(EOTWEffects.WEBBED.get(), WEBBED_TICKS, 0),
                    owner == null ? this : owner);
        }
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide) {
            return;
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.COBWEB)),
                    this.getX(), this.getY(), this.getZ(), 10, 0.12D, 0.12D, 0.12D, 0.06D);
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.WOOL_PLACE, SoundSource.HOSTILE, 0.7F, 1.4F);
        this.discard();
    }
}

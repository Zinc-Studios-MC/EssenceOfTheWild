package net.mrmisc.essenceofthewild.entity.custom.duck;

import java.util.EnumSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class DuckImprintFollowGoal extends Goal {

    private static final double START_FOLLOW_DIST_SQR = 3.0D * 3.0D;
    private static final double STOP_FOLLOW_DIST_SQR = 2.0D * 2.0D;

    private final DuckEntity duck;
    private final double speed;
    private LivingEntity target;
    private int repathCooldown;

    public DuckImprintFollowGoal(DuckEntity duck, double speed) {
        this.duck = duck;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.duck.isBaby()) {
            return false;
        }
        LivingEntity imprint = this.duck.getImprintTarget();
        if (imprint == null || !imprint.isAlive()) {
            return false;
        }
        if (this.duck.distanceToSqr(imprint) < START_FOLLOW_DIST_SQR) {
            return false;
        }
        this.target = imprint;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.duck.isBaby() || this.target == null || !this.target.isAlive()) {
            return false;
        }
        return this.duck.distanceToSqr(this.target) > STOP_FOLLOW_DIST_SQR;
    }

    @Override
    public void start() {
        this.repathCooldown = 0;
    }

    @Override
    public void stop() {
        this.target = null;
        this.duck.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.duck.getLookControl().setLookAt(this.target, 10.0F, (float) this.duck.getMaxHeadXRot());
        if (--this.repathCooldown <= 0) {
            this.repathCooldown = 10;
            this.duck.getNavigation().moveTo(this.target, this.speed);
        }
    }
}

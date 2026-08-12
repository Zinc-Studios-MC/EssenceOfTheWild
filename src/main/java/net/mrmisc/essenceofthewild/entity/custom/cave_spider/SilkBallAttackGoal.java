package net.mrmisc.essenceofthewild.entity.custom.cave_spider;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SilkBallAttackGoal extends Goal {

    private static final double MIN_RANGE = 4.5D;
    private static final double MAX_RANGE = 14.0D;

    private static final int WINDUP_TICKS = 3;
    private static final int RECOVERY_TICKS = 3;
    private static final int COOLDOWN_TICKS = 60;
    private static final int COOLDOWN_JITTER = 40;

    private final CaveSpiderEntity spider;

    private int nextSpitTick;
    private int timer;

    public SilkBallAttackGoal(CaveSpiderEntity spider) {
        this.spider = spider;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.spider.tickCount < this.nextSpitTick) {
            return false;
        }
        LivingEntity target = this.spider.getTarget();
        if (target == null || !target.isAlive() || this.spider.isVehicle()) {
            return false;
        }
        double distance = this.spider.distanceTo(target);
        return distance >= MIN_RANGE && distance <= MAX_RANGE && this.spider.getSensing().hasLineOfSight(target);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.spider.getTarget();
        return target != null && target.isAlive() && this.timer < WINDUP_TICKS + RECOVERY_TICKS;
    }

    @Override
    public void start() {
        this.timer = 0;
        this.spider.getNavigation().stop();
        this.spider.setSpitting(true);
    }

    @Override
    public void tick() {
        LivingEntity target = this.spider.getTarget();
        if (target == null) {
            return;
        }
        this.spider.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (this.timer == WINDUP_TICKS) {
            this.spider.spitSilkBall(target);
        }
        this.timer++;
    }

    @Override
    public void stop() {
        this.spider.setSpitting(false);
        this.nextSpitTick = this.spider.tickCount + COOLDOWN_TICKS
                + this.spider.getRandom().nextInt(COOLDOWN_JITTER);
        this.timer = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}

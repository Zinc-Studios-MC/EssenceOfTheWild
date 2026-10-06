package net.mrmisc.essenceofthewild.entity.custom.fox;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

class FoxHuntGoal extends Goal {
    private final FoxEntity fox;
    private int airborne;
    private int attackDelay;
    private boolean hit;

    FoxHuntGoal(FoxEntity fox) {
        this.fox = fox;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = fox.getTarget();
        return fox.readyToHunt() && target != null && target.isAlive() && fox.canAttack(target);
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        fox.clearStates();
        fox.setIsPouncing(false);
        attackDelay = 0;
    }

    @Override
    public void stop() {
        fox.setIsPouncing(false);
        fox.setIsCrouching(false);
        fox.setIsInterested(false);
        fox.setJumping(false);
        fox.setXRot(0);
        fox.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = fox.getTarget();
        if (target == null || !target.isAlive() || !fox.readyToHunt()) {
            return;
        }
        fox.getLookControl().setLookAt(target, 60, 30);
        if (attackDelay > 0) {
            attackDelay--;
        }
        if (fox.isPouncing()) {
            airborne++;
            Vec3 motion = fox.getDeltaMovement();
            if (!hit) {
                Vec3 aim = target.position().subtract(fox.position()).multiply(1, 0, 1);
                Vec3 steer = aim.normalize().scale(Math.min(0.8, aim.length() * 0.25));
                motion = new Vec3(motion.x + (steer.x - motion.x) * 0.4, motion.y,
                        motion.z + (steer.z - motion.z) * 0.4);
                fox.setDeltaMovement(motion);
                if (motion.horizontalDistanceSqr() > 0.001) {
                    fox.setYRot((float) Math.toDegrees(Math.atan2(-motion.x, motion.z)));
                }
            }
            fox.setXRot((float) Math.toDegrees(Math.atan2(-motion.y, motion.horizontalDistance())));
            if (!hit && fox.getBoundingBox().inflate(0.6).intersects(target.getBoundingBox())
                    && fox.getSensing().hasLineOfSight(target)) {
                hit = fox.doHurtTarget(target);
                attackDelay = 20;
            }
            if (airborne > 2 && fox.onGround()) {
                fox.setIsPouncing(false);
                fox.setJumping(false);
                fox.setXRot(0);
            }
            return;
        }
        if (fox.canPounce() && fox.onGround() && fox.distanceToSqr(target) <= 36
                && fox.getSensing().hasLineOfSight(target) && Fox.isPathClear(fox, target)) {
            fox.getNavigation().stop();
            fox.setIsInterested(true);
            fox.setIsCrouching(true);
            if (fox.isFullyCrouched()) {
                Vec3 direction = target.position().subtract(fox.position()).normalize();
                fox.beginPounce();
                fox.setIsCrouching(false);
                fox.setIsInterested(false);
                fox.setJumping(true);
                fox.setDeltaMovement(fox.getDeltaMovement().add(direction.x * 0.8, 0.9, direction.z * 0.8));
                airborne = 0;
                hit = false;
            }
            return;
        }
        fox.setIsCrouching(false);
        fox.setIsInterested(false);
        fox.getNavigation().moveTo(target, 1.2);
        double reach = fox.getBbWidth() * 2 + target.getBbWidth();
        if (attackDelay == 0 && fox.distanceToSqr(target) <= reach * reach && fox.getSensing().hasLineOfSight(target)) {
            fox.doHurtTarget(target);
            attackDelay = 20;
        }
    }
}

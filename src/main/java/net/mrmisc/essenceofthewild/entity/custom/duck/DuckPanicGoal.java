package net.mrmisc.essenceofthewild.entity.custom.duck;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class DuckPanicGoal extends PanicGoal {
    private final DuckEntity duck;
    private Vec3 from;
    private Vec3 landing;
    private int flightTicks;
    private int flightLength;
    private int runTicks;
    private int hurtAt;

    public DuckPanicGoal(DuckEntity duck) {
        super(duck, 1.4D);
        this.duck = duck;
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!shouldPanic()) {
            return false;
        }
        landing = null;
        if (!duck.isBaby() && !duck.isInWaterOrBubble()) {
            from = duck.position();
            landing = findLanding();
            if (landing != null) {
                return true;
            }
        }
        return super.canUse();
    }

    private Vec3 findLanding() {
        LivingEntity threat = duck.getLastHurtByMob();
        double angle = threat == null ? duck.getRandom().nextDouble() * Math.PI * 2.0D
                : Math.atan2(from.z - threat.getZ(), from.x - threat.getX());
        for (int i = 0; i < 24; i++) {
            double heading = angle + (duck.getRandom().nextDouble() - 0.5D) * Math.PI;
            BlockPos column = BlockPos.containing(from.x + Math.cos(heading) * 9.5D, from.y,
                    from.z + Math.sin(heading) * 9.5D);
            if (!duck.level().hasChunkAt(column)) {
                continue;
            }
            for (int y = 4; y >= -4; y--) {
                BlockPos pos = column.offset(0, y, 0);
                Vec3 target = Vec3.atBottomCenterOf(pos);
                double distance = target.subtract(from).horizontalDistanceSqr();
                if (distance < 81.0D || distance > 100.0D
                        || !duck.level().getBlockState(pos.below()).isFaceSturdy(duck.level(), pos.below(), Direction.UP)
                        || !duck.level().getFluidState(pos).isEmpty()) {
                    continue;
                }
                boolean clear = true;
                for (int step = 1; step <= 40; step++) {
                    Vec3 point = flightPoint(target, step / 40.0D);
                    if (duck.level().getBlockCollisions(duck,
                            duck.getBoundingBox().move(point.subtract(from))).iterator().hasNext()) {
                        clear = false;
                        break;
                    }
                }
                if (clear) {
                    return target;
                }
            }
        }
        return null;
    }

    private Vec3 flightPoint(Vec3 target, double progress) {
        return from.lerp(target, progress).add(0.0D, Math.sin(progress * Math.PI) * 1.25D, 0.0D);
    }

    @Override
    public void start() {
        duck.setPanicking(true);
        hurtAt = duck.getLastHurtByMobTimestamp();
        flightTicks = 0;
        runTicks = 40;
        if (landing == null) {
            super.start();
            return;
        }
        startFlight();
    }

    private void startFlight() {
        flightTicks = 0;
        runTicks = 40;
        duck.getNavigation().stop();
        duck.setDeltaMovement(Vec3.ZERO);
        double speed = duck.getAttributeValue(Attributes.MOVEMENT_SPEED) * speedModifier * 1.1D;
        flightLength = Math.max(1, Mth.ceil(landing.subtract(from).horizontalDistance() / speed));
        duck.setFlying(true);
    }

    @Override
    public boolean canContinueToUse() {
        return duck.isFlying() || runTicks > 0 || shouldPanic();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (duck.getLastHurtByMobTimestamp() != hurtAt) {
            hurtAt = duck.getLastHurtByMobTimestamp();
            if (!duck.isFlying() && !duck.isBaby() && !duck.isInWaterOrBubble()) {
                from = duck.position();
                landing = findLanding();
                if (landing != null) {
                    startFlight();
                }
            }
        }
        if (duck.isFlying()) {
            if (flightTicks >= flightLength || (flightTicks > 0 && duck.horizontalCollision) || duck.isInWaterOrBubble()) {
                duck.setFlying(false);
                duck.setDeltaMovement(Vec3.ZERO);
            } else {
                Vec3 point = flightPoint(landing, ++flightTicks / (double) flightLength);
                Vec3 movement = point.subtract(duck.position());
                duck.setDeltaMovement(movement);
                duck.hasImpulse = true;
                float yaw = (float) (Mth.atan2(landing.z - from.z, landing.x - from.x) * Mth.RAD_TO_DEG) - 90.0F;
                duck.setYRot(yaw);
                duck.setYHeadRot(yaw);
                duck.setYBodyRot(yaw);
                return;
            }
        }
        if (!duck.onGround() && !duck.isInWaterOrBubble()) {
            return;
        }
        if (runTicks > 0) {
            runTicks--;
        }
        if (duck.getNavigation().isDone() && findRandomPosition()) {
            duck.getNavigation().moveTo(posX, posY, posZ, speedModifier);
        }
    }

    @Override
    public void stop() {
        super.stop();
        duck.setFlying(false);
        duck.setPanicking(false);
        duck.getNavigation().stop();
        Vec3 movement = duck.getDeltaMovement();
        duck.setDeltaMovement(0.0D, movement.y, 0.0D);
        landing = null;
    }
}

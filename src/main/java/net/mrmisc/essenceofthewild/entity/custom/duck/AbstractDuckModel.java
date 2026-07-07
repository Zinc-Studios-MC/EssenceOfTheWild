package net.mrmisc.essenceofthewild.entity.custom.duck;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public abstract class AbstractDuckModel extends HierarchicalModel<DuckEntity> {
    protected static final float DEG_TO_RAD = ((float) Math.PI / 180F);
    // Matches the authored DUCK_SWIM leg arc: 1.0s (20 tick) loop, legs sweeping 10° -> -35°.
    private static final float PADDLE_PHASE_PER_TICK = (float) (Math.PI * 2.0 / 20.0);
    private static final float PADDLE_CENTER = -12.5F * DEG_TO_RAD;
    private static final float PADDLE_SWEEP = 22.5F * DEG_TO_RAD;

    private final ModelPart root;
    protected final ModelPart body;
    protected final ModelPart head;
    protected final ModelPart leftWing;
    protected final ModelPart rightWing;
    protected final ModelPart leftLeg;
    protected final ModelPart rightLeg;
    protected final ModelPart tail;

    protected AbstractDuckModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.leftWing = this.body.getChild("left_wing");
        this.rightWing = this.body.getChild("right_wing");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
        this.tail = this.body.getChild("tail");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(DuckEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.head.yRot = netHeadYaw * DEG_TO_RAD;
        this.head.xRot += headPitch * DEG_TO_RAD;

        if (entity.isSittingOnNestDelivery()) {
            this.animate(entity.idleAnimationState, DuckAnimations.DUCK_IDLE, ageInTicks);
            applyNestSittingPose();
            return;
        }

        if (entity.isDivingAnimationActive()) {
            this.animate(entity.diveAnimationState, DuckAnimations.DUCK_DIVING, ageInTicks);
            // DUCK_DIVING has no leg channels; kick hard while chasing fish underwater.
            applyPaddle(ageInTicks, 1.0F);
            return;
        }

        if (entity.isInWaterOrBubble()) {
            // Water travel is slow (~0.03 blocks/tick), so limbSwingAmount tops out around ~0.1
            // here. animateWalk() scales both keyframe amplitude and playback speed by it, which
            // squashed the swim loop to a near-frozen pose — drive the authored loop on the
            // animation-state clock instead and use the walk signal only to pick swim vs drift.
            if (limbSwingAmount > 0.02F) {
                this.animate(entity.swimAnimationState, DuckAnimations.DUCK_SWIM, ageInTicks);
            } else {
                this.animate(entity.waterIdleAnimationState, DuckAnimations.DUCK_WATER_IDLE, ageInTicks);
                // DUCK_WATER_IDLE has no leg channels; tread water lazily so the feet never freeze.
                applyPaddle(ageInTicks, 0.4F);
            }
            return;
        }

        // Flap whenever airborne (and not floating in water). Driven by the synced onGround flag
        // rather than client deltaMovement, which decays to ~0 mid-fall for remote entities.
        if (!entity.onGround()) {
            this.animate(entity.flapAnimationState, DuckAnimations.DUCK_FLY, ageInTicks);
            return;
        }

        if (entity.isCenteringOnNestDelivery()) {
            this.animateWalk(DuckAnimations.DUCK_WALK, ageInTicks, 0.7F, 1.5F, 1.0F);
            return;
        }

        if (limbSwingAmount < 0.01F) {
            this.animate(entity.idleAnimationState, DuckAnimations.DUCK_IDLE, ageInTicks);
        } else {
            this.animateWalk(DuckAnimations.DUCK_WALK, limbSwing, limbSwingAmount, 1.8F, 1.25F);
        }
    }

    // Procedural alternating leg stroke at a constant rate, mirroring the authored DUCK_SWIM
    // arc at strength 1 so handoffs between the water branches don't pop.
    private void applyPaddle(float ageInTicks, float strength) {
        float phase = ageInTicks * PADDLE_PHASE_PER_TICK;
        float sweep = PADDLE_SWEEP * strength;
        this.leftLeg.xRot += PADDLE_CENTER * strength + Mth.cos(phase) * sweep;
        this.rightLeg.xRot += PADDLE_CENTER * strength - Mth.cos(phase) * sweep;
    }

    private void applyNestSittingPose() {
        this.body.y += 2.0F;
        this.body.xRot -= 8.0F * DEG_TO_RAD;
        this.head.xRot += 8.0F * DEG_TO_RAD;
        this.leftWing.xRot += 6.0F * DEG_TO_RAD;
        this.rightWing.xRot += 6.0F * DEG_TO_RAD;
        this.leftLeg.xRot -= 20.0F * DEG_TO_RAD;
        this.rightLeg.xRot -= 20.0F * DEG_TO_RAD;
        this.tail.xRot += 10.0F * DEG_TO_RAD;
    }
}

package net.mrmisc.essenceofthewild.entity.custom.duck;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public abstract class AbstractDuckModel extends HierarchicalModel<DuckEntity> {
    protected static final float DEG_TO_RAD = ((float) Math.PI / 180F);

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
            return;
        }

        if (entity.isInWaterOrBubble()) {
            // Use limbSwingAmount (synced walk speed), not client deltaMovement which is ~0 for
            // remote mobs and would pin the duck to the idle branch so swim never plays.
            if (limbSwingAmount < 0.01F) {
                this.animate(entity.waterIdleAnimationState, DuckAnimations.DUCK_WATER_IDLE, ageInTicks);
            } else {
                this.animateWalk(DuckAnimations.DUCK_SWIM, limbSwing, limbSwingAmount, 1.8F, 1.15F);
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

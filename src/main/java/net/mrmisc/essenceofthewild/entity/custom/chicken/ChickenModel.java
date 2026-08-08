package net.mrmisc.essenceofthewild.entity.custom.chicken;
// blockbench export

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class ChickenModel<T extends Entity> extends HierarchicalModel<ChickenEntity> {
	private static final float DEG_TO_RAD = ((float) Math.PI / 180F);
	private static final double RUN_SPEED_THRESHOLD = 0.22D;

	// bake this in the renderer and hand it to the constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "chicken"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart left_wing;
	private final ModelPart right_wing;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart tail;

	public ChickenModel(ModelPart root) {
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.left_wing = this.body.getChild("left_wing");
		this.right_wing = this.body.getChild("right_wing");
		this.left_leg = this.body.getChild("left_leg");
		this.right_leg = this.body.getChild("right_leg");
		this.tail = this.body.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(14, 43).addBox(-1.5F, -5.0F, -3.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(-1.5F, -5.0F, -3.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(26, 0).addBox(-2.5F, -7.0F, -5.0F, 5.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(39, 20).addBox(-2.0F, -1.0F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.01F))
		.texOffs(50, 0).addBox(-2.0F, -4.0F, -1.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(60, 30).addBox(1.5F, -4.0F, -1.5F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(60, 30).mirror().addBox(-3.5F, -4.0F, -1.5F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(51, 50).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 43).addBox(0.0F, -2.0F, -5.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(36, 29).addBox(0.0F, -8.0F, -5.0F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -3.0F));

		PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 13).addBox(-0.01F, 1.0F, -1.01F, 0.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(36, 40).addBox(-1.0F, 1.0F, -1.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -4.0F, -2.0F));

		PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(40, 10).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).mirror().addBox(-0.99F, 0.0F, -1.01F, 0.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -3.0F, -2.0F));

		PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(26, 50).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).mirror().addBox(-1.0F, 5.0F, 0.0F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(50, 42).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.01F)), PartPose.offset(1.0F, 3.0F, 1.0F));

		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(26, 50).mirror().addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 62).addBox(-1.0F, 5.0F, 0.0F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(38, 50).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.01F)), PartPose.offset(-2.0F, 3.0F, 1.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, 29).addBox(0.0F, -5.0F, -1.0F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-2.0F, -0.99F, -1.0F, 4.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 3.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.body;
	}

	@Override
	public void setupAnim(ChickenEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.head.yRot = pNetHeadYaw * DEG_TO_RAD;
		this.head.xRot += pHeadPitch * DEG_TO_RAD;

		if (pEntity.isSittingOnNestDelivery()) {
			this.animate(pEntity.idleAnimationState, ChickenAnimations.chicken_idle, pAgeInTicks);
			applyNestSittingPose();
			return;
		}

		if (isFlapping(pEntity)) {
			this.animate(pEntity.flapAnimationState, ChickenAnimations.chicken_flap, pAgeInTicks);
			return;
		}

		if (pEntity.isCenteringOnNestDelivery()) {
			this.animateWalk(ChickenAnimations.chicken_walk, pAgeInTicks, 0.7F, 1.6F, 1.1F);
			return;
		}

		if (pLimbSwingAmount < 0.01F) {
			this.animate(pEntity.idleAnimationState, ChickenAnimations.chicken_idle, pAgeInTicks);
		} else {
			double speed = pEntity.getDeltaMovement().horizontalDistance();

			if (speed > RUN_SPEED_THRESHOLD) {
				this.animateWalk(ChickenAnimations.chicken_run, pLimbSwing, pLimbSwingAmount, 3.2F, 2.2F);
			} else {
				this.animateWalk(ChickenAnimations.chicken_walk, pLimbSwing, pLimbSwingAmount, 1.8F, 1.4F);
			}
		}
	}

	private boolean isFlapping(ChickenEntity entity) {
		return !entity.onGround() && Math.abs(entity.getDeltaMovement().y) > 0.02D;
	}

	private void applyNestSittingPose() {
		this.body.y += 2.0F;
		this.body.xRot -= 4.0F * DEG_TO_RAD;
		this.head.xRot += 5.0F * DEG_TO_RAD;
		this.left_wing.xRot += 5.0F * DEG_TO_RAD;
		this.right_wing.xRot += 5.0F * DEG_TO_RAD;
		this.left_leg.xRot -= 18.0F * DEG_TO_RAD;
		this.right_leg.xRot -= 18.0F * DEG_TO_RAD;
		this.tail.xRot += 6.0F * DEG_TO_RAD;
	}
}

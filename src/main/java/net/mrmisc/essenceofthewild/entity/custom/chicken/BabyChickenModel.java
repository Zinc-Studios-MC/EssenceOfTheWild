package net.mrmisc.essenceofthewild.entity.custom.chicken;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class BabyChickenModel extends HierarchicalModel<ChickenEntity> {
	private static final float DEG_TO_RAD = ((float) Math.PI / 180F);
	private static final double RUN_SPEED_THRESHOLD = 0.22D;

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "baby_chicken"), "main");
	private final ModelPart Root;
	private final ModelPart head;

	public BabyChickenModel(ModelPart root) {
		this.Root = root.getChild("Root");
		this.head = this.Root.getChild("body").getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Root = partdefinition.addOrReplaceChild("Root", CubeListBuilder.create(), PartPose.offset(0.0F, 25.0F, 0.0F));

		PartDefinition body = Root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 27).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(0.0F, -8.0F, -5.0F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 1.0F));

		PartDefinition tail = head.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 22).addBox(-1.5F, -0.99F, 0.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 1.0F));

		PartDefinition tail2 = head.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(14, 11).addBox(0.0F, -3.0F, 0.0F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition left_wing = head.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 22).addBox(0.01F, 0.0009F, -1.0536F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -2.0F, -1.0F));

		PartDefinition right_wing = head.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-0.01F, 0.0F, -1.01F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, -2.0F, -1.0F));

		PartDefinition left_leg = Root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(12, 22).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -4.0F, 0.0F));

		PartDefinition right_leg = Root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(12, 22).mirror().addBox(-1.0F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, -4.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.Root;
	}

	@Override
	public void setupAnim(ChickenEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.head.yRot = pNetHeadYaw * DEG_TO_RAD;
		this.head.xRot += pHeadPitch * DEG_TO_RAD;

		if (isFlapping(pEntity)) {
			this.animate(pEntity.flapAnimationState, BabyChickenAnimations.chicken_flap, pAgeInTicks);
			return;
		}

		if (pLimbSwingAmount < 0.01F) {
			this.animate(pEntity.idleAnimationState, BabyChickenAnimations.chicken_idle, pAgeInTicks);
		} else {
			double speed = pEntity.getDeltaMovement().horizontalDistance();

			if (speed > RUN_SPEED_THRESHOLD) {
				this.animateWalk(BabyChickenAnimations.chicken_run, pLimbSwing, pLimbSwingAmount, 3.2F, 2.2F);
			} else {
				this.animateWalk(BabyChickenAnimations.chicken_walk, pLimbSwing, pLimbSwingAmount, 1.8F, 1.4F);
			}
		}
	}

	private boolean isFlapping(ChickenEntity entity) {
		return !entity.onGround() && Math.abs(entity.getDeltaMovement().y) > 0.02D;
	}
}

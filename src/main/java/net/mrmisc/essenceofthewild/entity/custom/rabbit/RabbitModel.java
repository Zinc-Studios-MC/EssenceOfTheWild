package net.mrmisc.essenceofthewild.entity.custom.rabbit;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.custom.rabbit.RabbitEntity;
import net.mrmisc.essenceofthewild.entity.custom.rabbit.RabbitAnimations;

public class RabbitModel extends HierarchicalModel<RabbitEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "rabbit"), "main");
	private final ModelPart body;
	private final ModelPart left_thigh;
	private final ModelPart left_foot;
	private final ModelPart right_thigh;
	private final ModelPart right_foot;
	private final ModelPart bone;
	private final ModelPart head;
	private final ModelPart left_ear;
	private final ModelPart left_ear_ice;
	private final ModelPart left_whiskers;
	private final ModelPart right_ear;
	private final ModelPart right_ear_ice;
	private final ModelPart right_whiskers;
	private final ModelPart tail;
	private final ModelPart left_arm;
	private final ModelPart right_arm;

	public RabbitModel(ModelPart root) {
		this.body = root.getChild("body");
		this.left_thigh = this.body.getChild("left_thigh");
		this.left_foot = this.left_thigh.getChild("left_foot");
		this.right_thigh = this.body.getChild("right_thigh");
		this.right_foot = this.right_thigh.getChild("right_foot");
		this.bone = this.body.getChild("bone");
		this.head = this.bone.getChild("head");
		this.left_ear = this.head.getChild("left_ear");
		this.left_ear_ice = this.head.getChild("left_ear_ice");
		this.left_whiskers = this.head.getChild("left_whiskers");
		this.right_ear = this.head.getChild("right_ear");
		this.right_ear_ice = this.head.getChild("right_ear_ice");
		this.right_whiskers = this.head.getChild("right_whiskers");
		this.tail = this.bone.getChild("tail");
		this.left_arm = this.body.getChild("left_arm");
		this.right_arm = this.body.getChild("right_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 7.0F));

		PartDefinition left_thigh = body.addOrReplaceChild("left_thigh", CubeListBuilder.create().texOffs(14, 31).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 1.0F, -1.5F));

		PartDefinition left_foot = left_thigh.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(20, 15).addBox(-1.0F, 0.0F, -6.5F, 2.0F, 1.0F, 7.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, 3.0F, 1.5F));

		PartDefinition right_thigh = body.addOrReplaceChild("right_thigh", CubeListBuilder.create().texOffs(26, 31).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 1.0F, -1.5F));

		PartDefinition right_foot = right_thigh.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(20, 23).addBox(-1.0F, 0.0F, -6.5F, 2.0F, 1.0F, 7.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, 3.0F, 1.5F));

		PartDefinition bone = body.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -8.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(28, 47).addBox(-3.5F, -5.0F, -8.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 2.0F, -2.0F));

		PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -3.5F, -6.0F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -7.0F));

		PartDefinition head_rotation_r1 = head.addOrReplaceChild("head_rotation_r1", CubeListBuilder.create().texOffs(40, 15).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 0.5F, -6.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(8, 32).mirror().addBox(-0.5F, -5.0F, -1.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -3.5F, 0.0F));

		PartDefinition left_ear_ice = head.addOrReplaceChild("left_ear_ice", CubeListBuilder.create().texOffs(14, 25).mirror().addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, -2.5F, -0.5F));

		PartDefinition left_whiskers = head.addOrReplaceChild("left_whiskers", CubeListBuilder.create().texOffs(0, 60).addBox(0.0F, -2.5F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offset(1.5F, -1.0F, -5.0F));

		PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(8, 32).addBox(-1.5F, -5.0F, -1.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -3.5F, 0.0F));

		PartDefinition right_ear_ice = head.addOrReplaceChild("right_ear_ice", CubeListBuilder.create().texOffs(14, 25).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -2.5F, -0.5F));

		PartDefinition right_whiskers = head.addOrReplaceChild("right_whiskers", CubeListBuilder.create().texOffs(0, 60).mirror().addBox(-4.0F, -2.5F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(-1.5F, -1.0F, -5.0F));

		PartDefinition tail = bone.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.25F, -1.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.75F, 1.5F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 32).addBox(-1.01F, 0.0F, -0.99F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, -9.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 0).addBox(-0.99F, 0.0F, -0.99F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.0F, -9.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ModelPart root() {
		return this.body;
	}

	@Override
	public void setupAnim(RabbitEntity pEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if (limbSwingAmount < 0.01F) {
			this.animate(pEntity.idleAnimationState, RabbitAnimations.idle, ageInTicks);
		} else {
			double speed = pEntity.getDeltaMovement().horizontalDistance();

			if (speed > 0.2D) {
				this.animateWalk(RabbitAnimations.run, limbSwing, limbSwingAmount, 3.5F, 2.5F);
			} else {
				this.animateWalk(RabbitAnimations.walk, limbSwing, limbSwingAmount, 1.6F, 1.3F);
			}
		}
	}
}
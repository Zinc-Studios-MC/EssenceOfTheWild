package net.mrmisc.essenceofthewild.entity.custom.sheep;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class BabySheepModel extends SheepModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "baby_sheep"), "main");
	private final ModelPart Root;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart right_ear;
	private final ModelPart left_ear;
	private final ModelPart tail;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;

	public BabySheepModel(ModelPart root) {
		super(root);
		this.Root = root.getChild("Root");
		this.body = this.Root.getChild("body");
		this.head = this.body.getChild("head");
		this.right_ear = this.head.getChild("right_ear");
		this.left_ear = this.head.getChild("left_ear");
		this.tail = this.body.getChild("tail");
		this.leg1 = this.Root.getChild("leg1");
		this.leg2 = this.Root.getChild("leg2");
		this.leg3 = this.Root.getChild("leg3");
		this.leg4 = this.Root.getChild("leg4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Root = partdefinition.addOrReplaceChild("Root", CubeListBuilder.create(), PartPose.offset(0.0F, 26.0F, 0.0F));

		PartDefinition body = Root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 8.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(26, 43).addBox(-3.5F, -1.5F, -8.5F, 7.0F, 9.0F, 12.0F, new CubeDeformation(0.005F)), PartPose.offset(0.0F, -16.0F, 2.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 8.0F, 3.0F, new CubeDeformation(0.01F))
		.texOffs(46, 4).addBox(2.0F, -5.0F, -3.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(34, 0).addBox(-4.0F, -5.0F, -3.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 29).addBox(2.0F, -3.0F, -3.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 31).addBox(-6.0F, -3.0F, -3.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.0F));

		PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(34, 6).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -2.0F, -1.0F));

		PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(34, 9).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -2.0F, -1.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(34, 12).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

		PartDefinition leg1 = Root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(8, 31).mirror().addBox(0.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(-0.005F)).mirror(false)
		.texOffs(26, 19).addBox(-0.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -9.0F, 4.0F));

		PartDefinition leg2 = Root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(8, 31).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(-0.005F))
		.texOffs(26, 25).addBox(-2.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -9.0F, 4.0F));

		PartDefinition leg3 = Root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(24, 31).mirror().addBox(0.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(-0.005F)).mirror(false)
		.texOffs(14, 25).addBox(-0.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -9.0F, -5.0F));

		PartDefinition leg4 = Root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(24, 31).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(-0.005F))
		.texOffs(14, 19).addBox(-2.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -9.0F, -5.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(SheepEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity.isEating()) {
			this.animate(entity.eatAnimationState, BabySheepAnimations.sheep_eat, ageInTicks);
			return;
		}

		if (limbSwingAmount < 0.01F) {
			this.animate(entity.idleAnimationState, BabySheepAnimations.sheep_idle, ageInTicks);
		} else {
			double speed = entity.getDeltaMovement().horizontalDistance();

			if (speed > 0.2D) {
				this.animateWalk(BabySheepAnimations.sheep_run, limbSwing, limbSwingAmount, 3.5F, 2.5F);
			} else {
				this.animateWalk(BabySheepAnimations.sheep_walk, limbSwing, limbSwingAmount, 1.6F, 1.3F);
			}
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	@Override
	public ModelPart root() {
		return this.Root;
	}
}
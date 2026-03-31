package net.mrmisc.essenceofthewild.entity.custom.sheep;// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


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

public class SheepModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "sheep"), "main");
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

	public SheepModel(ModelPart root) {
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

		PartDefinition Root = partdefinition.addOrReplaceChild("Root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = Root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -2.0F, -9.0F, 7.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 79).addBox(-4.5F, -3.0F, -9.5F, 9.0F, 11.0F, 17.0F, new CubeDeformation(0.005F)), PartPose.offset(0.0F, -16.0F, 2.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 55).addBox(-2.5F, -4.0F, -5.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 113).addBox(-3.0F, -5.5F, -5.5F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(23, 25).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 45).addBox(4.5F, -6.0F, -5.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(46, 4).addBox(2.5F, -6.0F, -5.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(46, 10).addBox(-4.5F, -6.0F, -5.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(46, 0).addBox(-8.5F, -6.0F, -5.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 29).addBox(2.5F, -4.0F, -5.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 25).addBox(-8.5F, -4.0F, -5.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -8.0F));

		PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(12, 46).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -3.0F, -3.0F));

		PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(46, 16).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -3.0F, -3.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(50, 54).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 7.0F));

		PartDefinition leg1 = Root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 33).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(-0.005F))
		.texOffs(68, 97).mirror().addBox(-1.5F, 0.0F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -9.0F, 7.0F));

		PartDefinition leg2 = Root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(12, 34).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(-0.005F))
		.texOffs(68, 97).addBox(-2.5F, 0.0F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -9.0F, 7.0F));

		PartDefinition leg3 = Root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(24, 34).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(-0.005F))
		.texOffs(47, 119).mirror().addBox(-1.5F, 0.0F, -2.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -9.0F, -5.0F));

		PartDefinition leg4 = Root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(36, 34).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(-0.005F))
		.texOffs(47, 119).addBox(-2.5F, 0.0F, -2.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -9.0F, -5.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
						  float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		SheepEntity sheep = (SheepEntity) entity;

		if (sheep.isEating()) {
			this.animate(sheep.eatAnimationState, SheepAnimations.sheep_eat, ageInTicks);
			return;
		}

		if (limbSwingAmount < 0.01F) {
			this.animate(sheep.idleAnimationState, SheepAnimations.sheep_idle, ageInTicks);
		} else {
			double speed = entity.getDeltaMovement().horizontalDistance();

			if (speed > 0.2F) {
				this.animateWalk(SheepAnimations.sheep_run, limbSwing, limbSwingAmount, 3.5F, 2.5F);
			} else {
				this.animateWalk(SheepAnimations.sheep_walk, limbSwing, limbSwingAmount, 1.6F, 1.3F);
			}
		}
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		Root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
	}

	@Override
	public ModelPart root() {
		return this.Root;
	}
}
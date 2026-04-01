package net.mrmisc.essenceofthewild.entity.custom.sheep;// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

import net.minecraft.client.model.HierarchicalModel;

public class WoolModel extends HierarchicalModel<SheepEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "wool"), "main");
	private final ModelPart bone;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;
	private final ModelPart body;
	private final ModelPart head;

	public WoolModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.leg1 = this.bone.getChild("leg1");
		this.leg2 = this.bone.getChild("leg2");
		this.leg3 = this.bone.getChild("leg3");
		this.leg4 = this.bone.getChild("leg4");
		this.body = this.bone.getChild("body");
		this.head = this.body.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 27.0F, 0.0F));

		PartDefinition leg1 = bone.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(68, 97).mirror().addBox(-1.5F, 0.0F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offset(-3.0F, -12.0F, 7.0F));

		PartDefinition leg2 = bone.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(68, 97).addBox(-2.5F, 0.0F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.01F)), PartPose.offset(3.0F, -12.0F, 7.0F));

		PartDefinition leg3 = bone.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(47, 119).mirror().addBox(-1.5F, 0.0F, -2.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offset(-3.0F, -12.0F, -5.0F));

		PartDefinition leg4 = bone.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(47, 119).addBox(-2.5F, 0.0F, -2.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.01F)), PartPose.offset(3.0F, -12.0F, -5.0F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 79).addBox(-4.5F, -3.0F, -9.5F, 9.0F, 11.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -19.0F, 2.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 113).addBox(-3.0F, -5.5F, -5.5F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.bone;
	}

	@Override
	public void setupAnim(SheepEntity sheep, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (sheep.isEating()) {
			this.animate(sheep.eatAnimationState, WoolAnimations.sheep_eat, ageInTicks);
			return;
		}

		if (limbSwingAmount < 0.01F) {
			this.animate(sheep.idleAnimationState, WoolAnimations.sheep_idle, ageInTicks);
		} else {
			double speed = sheep.getDeltaMovement().horizontalDistance();

			if (speed > 0.2D) {
				this.animateWalk(WoolAnimations.sheep_run, limbSwing, limbSwingAmount, 3.5F, 2.5F);
			} else {
				this.animateWalk(WoolAnimations.sheep_walk, limbSwing, limbSwingAmount, 1.6F, 1.3F);
			}
		}
	}
}
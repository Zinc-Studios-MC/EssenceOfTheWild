package net.mrmisc.essenceofthewild.entity.custom.pig;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class PigModel extends HierarchicalModel<PigEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "pig"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart bone4;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;

	public PigModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.head = this.body.getChild("head");
		this.bone = this.head.getChild("bone");
		this.bone2 = this.head.getChild("bone2");
		this.bone4 = this.body.getChild("bone4");
		this.leg1 = this.root.getChild("leg1");
		this.leg2 = this.root.getChild("leg2");
		this.leg3 = this.root.getChild("leg3");
		this.leg4 = this.root.getChild("leg4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-4.0F, 4.0F, -4.0F, 8.0F, 3.0F, 14.0F, new CubeDeformation(0.01F))
		.texOffs(76, 9).addBox(-4.5F, -4.0F, -1.0F, 9.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(88, 19).addBox(4.5F, -2.0F, 0.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(76, 19).addBox(-4.5F, -2.0F, 0.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, -4.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 22).addBox(-2.5F, -5.0F, -5.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(31, 0).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(40, 46).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(44, 33).addBox(-2.5F, 3.0F, -5.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(24, 40).addBox(-2.0F, 0.0F, -7.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));

		PartDefinition bone = head.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(36, 40).addBox(-3.0F, -4.0F, 0.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -8.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -4.0F, -2.0F));

		PartDefinition bone2 = head.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(7, 9).addBox(0.0F, -8.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(0.0F, -4.0F, 0.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -4.0F, -2.0F));

		PartDefinition bone4 = body.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(20, 22).addBox(0.0F, -0.5F, 0.0F, 0.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 10.0F));

		PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(32, 22).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(-0.001F)), PartPose.offset(-2.0F, -6.0F, 5.0F));

		PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(32, 31).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(-0.001F)), PartPose.offset(2.0F, -6.0F, 5.0F));

		PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 35).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(-0.001F)), PartPose.offset(-2.0F, -6.0F, -5.0F));

		PartDefinition leg4 = root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(12, 36).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(-0.001F)), PartPose.offset(2.0F, -6.0F, -5.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(PigEntity pig, float limbSwing, float limbSwingAmount, float ageInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (limbSwingAmount < 0.01F) {
			this.animate(pig.idleAnimationState, PigAnimations.idle, ageInTicks);
		} else {
			double speed = pig.getDeltaMovement().horizontalDistance();
			if (speed > 0.2D) {
				this.animateWalk(PigAnimations.run, limbSwing, limbSwingAmount, 4.0f, 2.5F);
			} else {
				this.animateWalk(PigAnimations.walk, limbSwing, limbSwingAmount, 2.8f, 1.3F);
			}
		}
	}
}
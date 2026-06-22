package net.mrmisc.essenceofthewild.entity.custom.ferret;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class BabyFerretModel extends FerretModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "baby_ferret"), "main");
	private final ModelPart body;
	private final ModelPart back_left_leg;
	private final ModelPart back_right_leg;
	private final ModelPart bone;
	private final ModelPart tail;
	private final ModelPart mainPart;
	private final ModelPart head;
	private final ModelPart right_ear_cube;
	private final ModelPart left_ear_cube;
	private final ModelPart front_left_leg;
	private final ModelPart front_right_leg;

	public BabyFerretModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.back_left_leg = this.body.getChild("back_left_leg");
		this.back_right_leg = this.body.getChild("back_right_leg");
		this.bone = this.body.getChild("bone");
		this.tail = this.bone.getChild("tail");
		this.mainPart = this.bone.getChild("main part");
		this.head = this.mainPart.getChild("head");
		this.right_ear_cube = this.head.getChild("right_ear_cube");
		this.left_ear_cube = this.head.getChild("left_ear_cube");
		this.front_left_leg = this.mainPart.getChild("front_left_leg");
		this.front_right_leg = this.mainPart.getChild("front_right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 4.0F));

		PartDefinition back_left_leg = body.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(0, 26).addBox(-1.0F, 0.0F, -1.01F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -1.0F, 4.0F));

		PartDefinition back_right_leg = body.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(8, 26).addBox(-1.0F, 0.0F, -1.01F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.0F, 4.0F));

		PartDefinition bone = body.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -5.0F, -12.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		PartDefinition tail = bone.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(6, 32).addBox(-1.0F, -0.5F, 7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 16).addBox(-0.5F, 1.5F, -1.0F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition mainPart = bone.addOrReplaceChild("main part", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, -11.0F));

		PartDefinition head = mainPart.addOrReplaceChild("head", CubeListBuilder.create().texOffs(20, 25).addBox(-2.5F, -2.99F, -3.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 7).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition right_ear_cube = head.addOrReplaceChild("right_ear_cube", CubeListBuilder.create().texOffs(16, 26).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -2.0F, -0.6F));

		PartDefinition left_ear_cube = head.addOrReplaceChild("left_ear_cube", CubeListBuilder.create().texOffs(16, 29).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -2.0F, -0.6F));

		PartDefinition front_left_leg = mainPart.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(0, 31).addBox(0.0F, -1.01F, 0.0F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 2.0F, -1.0F));

		PartDefinition front_right_leg = mainPart.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(32, -1).addBox(0.0F, -1.01F, 0.0F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 2.0F, -1.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(FerretEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation((FerretEntity)entity, netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(entity.isRunning() ? BabyFerretAnimation.run : BabyFerretAnimation.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(entity.idleAnimationState, BabyFerretAnimation.idle, ageInTicks, 1);
		this.animate(entity.diggingInAnimationState, BabyFerretAnimation.dig, ageInTicks, 1);
		this.animate(entity.diggingOutAnimationState, BabyFerretAnimation.digOut, ageInTicks, 1);
	}

	private void applyHeadRotation(FerretEntity pEntity, float pNetHeadYaw, float pHeadPitch, float pAgeInTicks){
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30, 30);
		pHeadPitch = Mth.clamp(pHeadPitch, -25, 45);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return body;
	}
}
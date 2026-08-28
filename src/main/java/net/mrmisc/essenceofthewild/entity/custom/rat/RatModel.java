package net.mrmisc.essenceofthewild.entity.custom.rat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class RatModel extends HierarchicalModel<RatEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "rat"), "main");
	private final ModelPart body;
	private final ModelPart back_left_leg;
	private final ModelPart back_right_leg;
	private final ModelPart bone;
	private final ModelPart head;
	private final ModelPart right_ear_cube;
	private final ModelPart right_whiskey;
	private final ModelPart left_ear_cube;
	private final ModelPart left_whiskey;
	private final ModelPart front_left_leg;
	private final ModelPart front_right_leg;
	private final ModelPart tail;

	public RatModel(ModelPart root) {
		this.body = root.getChild("body");
		this.back_left_leg = this.body.getChild("back_left_leg");
		this.back_right_leg = this.body.getChild("back_right_leg");
		this.bone = this.body.getChild("bone");
		this.head = this.bone.getChild("head");
		this.right_ear_cube = this.head.getChild("right_ear_cube");
		this.right_whiskey = this.head.getChild("right_whiskey");
		this.left_ear_cube = this.head.getChild("left_ear_cube");
		this.left_whiskey = this.head.getChild("left_whiskey");
		this.front_left_leg = this.bone.getChild("front_left_leg");
		this.front_right_leg = this.bone.getChild("front_right_leg");
		this.tail = this.bone.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 4.0F));

		PartDefinition back_left_leg = body.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(0, 36).addBox(-1.0F, -1.0F, -3.01F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 37).addBox(-1.5F, 3.0F, -7.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 2.0F));

		PartDefinition back_right_leg = body.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(12, 37).addBox(-1.0F, -1.0F, -3.01F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(38, 0).addBox(-1.5F, 3.0F, -7.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 0.0F, 2.0F));

		PartDefinition bone = body.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -12.0F, 6.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

		PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(22, 18).addBox(-2.5F, -3.0F, -8.0F, 5.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -12.0F));

		PartDefinition head_rotation_r1 = head.addOrReplaceChild("head_rotation_r1", CubeListBuilder.create().texOffs(38, 14).addBox(-3.0F, 0.0F, 0.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, -8.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition right_ear_cube = head.addOrReplaceChild("right_ear_cube", CubeListBuilder.create().texOffs(38, 37).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -2.0F, -0.6F));

		PartDefinition right_whiskey = head.addOrReplaceChild("right_whiskey", CubeListBuilder.create().texOffs(38, 9).addBox(-5.0F, -2.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -2.0F, -6.6F));

		PartDefinition left_ear_cube = head.addOrReplaceChild("left_ear_cube", CubeListBuilder.create().texOffs(40, 29).addBox(0.0F, -5.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -2.0F, -0.6F));

		PartDefinition left_whiskey = head.addOrReplaceChild("left_whiskey", CubeListBuilder.create().texOffs(38, 4).addBox(0.0F, -2.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -2.0F, -6.6F));

		PartDefinition front_left_leg = bone.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(22, 29).addBox(-1.0F, -0.01F, -1.0F, 6.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 1.0F, -11.0F));

		PartDefinition front_right_leg = bone.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(22, 33).addBox(-5.0F, -0.01F, -1.0F, 6.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 1.0F, -11.0F));

		PartDefinition tail = bone.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, -1.0F, -1.0F, 0.0F, 7.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 1.0F));

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
	public void setupAnim(RatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch);

		this.animateWalk(entity.isRunning() ? RatAnimations.run : RatAnimations.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(entity.idleAnimationState, RatAnimations.idle, ageInTicks, 1f);
	}

	private void applyHeadRotation(float netHeadYaw, float headPitch) {
		netHeadYaw = Mth.clamp(netHeadYaw, -30f, 30f);
		headPitch = Mth.clamp(headPitch, -25f, 45f);

		this.head.yRot = netHeadYaw * ((float) Math.PI / 180f);
		this.head.xRot = headPitch * ((float) Math.PI / 180f);
	}
}
package net.mrmisc.essenceofthewild.entity.custom.rat;

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

public class BabyRatModel extends RatModel {
	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "baby_rat"), "main");

	public BabyRatModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition back_left_leg = body.addOrReplaceChild("back_left_leg", CubeListBuilder.create()
		.texOffs(0, 26).addBox(-1.0F, 0.0F, -0.01F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(26, 5).addBox(-1.5F, 2.0F, -3.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -2.0F, 3.0F));

		PartDefinition back_right_leg = body.addOrReplaceChild("back_right_leg", CubeListBuilder.create()
		.texOffs(26, 0).addBox(-1.0F, 0.0F, -0.01F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(26, 8).addBox(-1.5F, 2.0F, -3.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -2.0F, 3.0F));

		PartDefinition bone = body.addOrReplaceChild("bone", CubeListBuilder.create()
		.texOffs(0, 0).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create()
		.texOffs(16, 13).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(18, 28).addBox(-1.0F, 1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -2.0F));

		PartDefinition right_ear_cube = head.addOrReplaceChild("right_ear_cube", CubeListBuilder.create()
		.texOffs(10, 26).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 0.0F, -0.6F));

		PartDefinition left_ear_cube = head.addOrReplaceChild("left_ear_cube", CubeListBuilder.create()
		.texOffs(14, 28).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, -0.6F));

		PartDefinition right_whiskey = head.addOrReplaceChild("right_whiskey", CubeListBuilder.create()
		.texOffs(38, 9).addBox(-5.0F, -5.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 1.0F, -4.6F));

		PartDefinition left_whiskey = head.addOrReplaceChild("left_whiskey", CubeListBuilder.create()
		.texOffs(38, 4).addBox(0.0F, -5.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.0F, -4.6F));

		PartDefinition front_left_leg = bone.addOrReplaceChild("front_left_leg", CubeListBuilder.create()
		.texOffs(16, 20).addBox(0.0F, -0.01F, -1.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -1.0F, -0.5F));

		PartDefinition front_right_leg = bone.addOrReplaceChild("front_right_leg", CubeListBuilder.create()
		.texOffs(16, 24).addBox(-4.0F, -0.01F, -1.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -1.0F, -0.5F));

		PartDefinition tail = bone.addOrReplaceChild("tail", CubeListBuilder.create()
		.texOffs(0, 13).addBox(0.0F, -3.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 7.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}

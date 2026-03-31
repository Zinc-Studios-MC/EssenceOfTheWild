package net.mrmisc.essenceofthewild.entity.custom.pig;// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class PigModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "pig"), "main");
	private final ModelPart bone3;
	private final ModelPart head;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;
	private final ModelPart bone4;

	public PigModel(ModelPart root) {
		this.bone3 = root.getChild("bone3");
		this.head = this.bone3.getChild("head");
		this.bone = this.head.getChild("bone");
		this.bone2 = this.head.getChild("bone2");
		this.leg1 = this.bone3.getChild("leg1");
		this.leg2 = this.bone3.getChild("leg2");
		this.leg3 = this.bone3.getChild("leg3");
		this.leg4 = this.bone3.getChild("leg4");
		this.bone4 = this.bone3.getChild("bone4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone3 = partdefinition.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-4.0F, 4.0F, -4.0F, 8.0F, 3.0F, 14.0F, new CubeDeformation(0.01F))
		.texOffs(76, 9).addBox(-4.5F, -4.0F, -1.0F, 9.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(88, 19).addBox(4.5F, -2.0F, 0.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(76, 19).addBox(-4.5F, -2.0F, 0.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, -4.0F));

		PartDefinition head = bone3.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 22).addBox(-2.5F, -5.0F, -5.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(31, 0).addBox(-3.0F, -6.0F, -6.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(40, 46).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(44, 33).addBox(-2.5F, 3.0F, -5.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(24, 40).addBox(-2.0F, 0.0F, -7.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));

		PartDefinition bone = head.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(36, 40).addBox(-3.0F, -4.0F, 0.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -8.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -4.0F, -2.0F));

		PartDefinition bone2 = head.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(7, 9).addBox(0.0F, -8.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(0.0F, -4.0F, 0.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -4.0F, -2.0F));

		PartDefinition leg1 = bone3.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(32, 22).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 5.0F, 9.0F));

		PartDefinition leg2 = bone3.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(32, 31).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 5.0F, 9.0F));

		PartDefinition leg3 = bone3.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 35).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 5.0F, -1.0F));

		PartDefinition leg4 = bone3.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(12, 36).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 5.0F, -1.0F));

		PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(20, 22).addBox(0.0F, -0.5F, 0.0F, 0.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 10.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
package net.mrmisc.essenceofthewild.entity.custom.sheep;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class WoolModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("essenceofthewild", "wool"), "main");
	private final ModelPart bone;
	private final ModelPart head;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;

	public WoolModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.head = this.bone.getChild("head");
		this.leg1 = this.bone.getChild("leg1");
		this.leg2 = this.bone.getChild("leg2");
		this.leg3 = this.bone.getChild("leg3");
		this.leg4 = this.bone.getChild("leg4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 79).addBox(-4.5F, -22.0F, -7.5F, 9.0F, 11.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 27.0F, 0.0F));

		PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 113).addBox(-3.0F, -5.5F, -5.5F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, -6.0F));

		PartDefinition leg1 = bone.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(68, 97).mirror().addBox(-1.5F, 1.0F, -1.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -12.0F, 7.0F));

		PartDefinition leg2 = bone.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(68, 97).addBox(-2.5F, 1.0F, -1.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -12.0F, 7.0F));

		PartDefinition leg3 = bone.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(47, 120).mirror().addBox(-1.5F, 1.0F, -2.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -12.0F, -5.0F));

		PartDefinition leg4 = bone.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(47, 120).addBox(-2.5F, 1.0F, -2.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -12.0F, -5.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
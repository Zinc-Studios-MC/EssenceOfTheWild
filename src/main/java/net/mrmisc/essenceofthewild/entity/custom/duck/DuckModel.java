package net.mrmisc.essenceofthewild.entity.custom.duck;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class DuckModel extends AbstractDuckModel {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "duck"), "main");

    public DuckModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition body = partDefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(9, 9).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 9.0F, new CubeDeformation(0.001F))
                        .texOffs(0, 15).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0001F)),
                PartPose.offset(0.0F, 16.0F, 0.0F)
        );

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -2.0F));
        head.addOrReplaceChild("head_main",
                CubeListBuilder.create()
                        .texOffs(34, 41).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(30, 0).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 9.0F, 3.0F, new CubeDeformation(0.0001F)),
                PartPose.ZERO
        );
        head.addOrReplaceChild("bill_upper",
                CubeListBuilder.create()
                        .texOffs(44, 8).addBox(-2.0F, 1.0F, -6.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0001F))
                        .texOffs(48, 16).addBox(-1.5F, 1.0F, -6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO
        );
        head.addOrReplaceChild("bill_center",
                CubeListBuilder.create().texOffs(20, 41).addBox(0.0F, 2.0F, -6.0F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.001F)),
                PartPose.ZERO
        );

        body.addOrReplaceChild("left_wing",
                CubeListBuilder.create()
                        .texOffs(0, 28).addBox(-0.01F, 0.0F, -1.0F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.001F))
                        .texOffs(20, 30).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(4.0F, -1.0F, -2.0F)
        );

        body.addOrReplaceChild("right_wing",
                CubeListBuilder.create()
                        .texOffs(34, 30).addBox(0.0F, 0.0F, -1.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                        .texOffs(28, 15).addBox(0.01F, 0.0F, -1.0F, 0.0F, 5.0F, 10.0F, new CubeDeformation(0.001F)),
                PartPose.offset(-4.0F, -1.0F, -2.0F)
        );

        body.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(0, 43).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 12).addBox(-2.0F, 0.5F, 4.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -1.0F, 3.0F)
        );

        partDefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(46, 41).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 28).addBox(-1.5F, 5.0F, 1.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.001F)),
                PartPose.offset(2.0F, 19.0F, 1.0F)
        );

        partDefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(44, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 29).addBox(-1.5F, 5.0F, 1.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.001F)),
                PartPose.offset(-2.0F, 19.0F, 1.0F)
        );

        return LayerDefinition.create(meshDefinition, 64, 64);
    }
}

package net.mrmisc.essenceofthewild.entity.custom.pig;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class PigSaddleModel extends PigModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "pig_saddle"), "main");

    public PigSaddleModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        return PigModel.createBodyLayer();
    }
}

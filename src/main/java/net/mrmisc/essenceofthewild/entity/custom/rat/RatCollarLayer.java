package net.mrmisc.essenceofthewild.entity.custom.rat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class RatCollarLayer extends RenderLayer<RatEntity, RatModel> {

    private final ResourceLocation COLLAR_LOCATION = ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/rat/rat_collar.png");
    private final RatCollarModel collarModel;

    public RatCollarLayer(RenderLayerParent<RatEntity, RatModel> pRenderer, EntityModelSet modelSet) {
        super(pRenderer);
        this.collarModel = new RatCollarModel(modelSet.bakeLayer(RatCollarModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, RatEntity ratEntity, float v, float v1, float v2, float v3, float v4, float v5) {
        if (ratEntity.isTame() && !ratEntity.isInvisible()) {
            float[] $$10 = ratEntity.getCollarColor().getTextureDiffuseColors();
            renderColoredCutoutModel(this.getParentModel(), COLLAR_LOCATION, poseStack, multiBufferSource, i, ratEntity, $$10[0], $$10[1], $$10[2]);
        }
    }
}

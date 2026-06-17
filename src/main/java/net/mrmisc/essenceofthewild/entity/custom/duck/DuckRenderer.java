package net.mrmisc.essenceofthewild.entity.custom.duck;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DuckRenderer extends MobRenderer<DuckEntity, AbstractDuckModel> {
    private final DuckModel adultModel;
    private final DucklingModel babyModel;

    public DuckRenderer(EntityRendererProvider.Context context) {
        super(context, new DuckModel(context.bakeLayer(DuckModel.LAYER_LOCATION)), 0.3F);
        this.adultModel = (DuckModel) this.model;
        this.babyModel = new DucklingModel(context.bakeLayer(DucklingModel.LAYER_LOCATION));
    }

    @Override
    public void render(DuckEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = entity.isBaby() ? this.babyModel : this.adultModel;
        this.shadowRadius = entity.isBaby() ? 0.18F : 0.3F;
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DuckEntity entity) {
        DuckVariant variant = entity.getVariant();
        return entity.isBaby() ? variant.babyLocation() : variant.adultLocation();
    }
}

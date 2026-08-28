package net.mrmisc.essenceofthewild.entity.custom.chicken;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class ChickenRenderer extends MobRenderer<ChickenEntity, HierarchicalModel<ChickenEntity>> {

    private final HierarchicalModel<ChickenEntity> adult;
    private final BabyChickenModel baby;

    public ChickenRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ChickenModel<>(pContext.bakeLayer(ChickenModel.LAYER_LOCATION)), 0.3f);

        this.adult = new ChickenModel<>(pContext.bakeLayer(ChickenModel.LAYER_LOCATION));
        this.baby = new BabyChickenModel(pContext.bakeLayer(BabyChickenModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(ChickenEntity pEntity) {
        if (pEntity.isBaby()) {
            String path = pEntity.getVariant().location().getPath();

            int index = path.lastIndexOf('/') + 1;

            String babyPath = path.substring(0, index)
                    + "baby_"
                    + path.substring(index);
            return ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, babyPath);
        }
        return pEntity.getVariant().location();
    }

    @Override
    public void render(ChickenEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        this.model = pEntity.isBaby() ? baby : adult;
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}

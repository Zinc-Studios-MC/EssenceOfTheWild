package net.mrmisc.essenceofthewild.entity.custom.pig;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import org.jetbrains.annotations.NotNull;

public class PigRenderer extends MobRenderer<PigEntity, PigModel> {

    public PigModel adult;
    public BabyPigModel baby;
    public PigRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PigModel(pContext.bakeLayer(PigModel.LAYER_LOCATION)), 0.7f);
        this.addLayer(new PigSaddleLayer(this, pContext.getModelSet()));

        this.adult = new PigModel(pContext.bakeLayer(PigModel.LAYER_LOCATION));
        this.baby = new BabyPigModel(pContext.bakeLayer(BabyPigModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(PigEntity pEntity) {
        if(pEntity.isBaby()){
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
    public void render(PigEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            this.model = baby;
        } else {
            this.model = adult;
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}

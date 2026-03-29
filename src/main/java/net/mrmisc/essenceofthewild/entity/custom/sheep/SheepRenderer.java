package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;

public class SheepRenderer extends MobRenderer<SheepEntity, SheepModel<SheepEntity>> {
    private static final ResourceLocation COLD_TEXTURE = ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/sheep/cold_sheep.png");
    private static final ResourceLocation WARM_TEXTURE = ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/sheep/warm_sheep.png");
    private static final ResourceLocation BASIC = ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/sheep/sheep.png");
    private static final ResourceLocation BASIC_2 = ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "textures/entity/sheep/sheep_grey.png");

    public SheepRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SheepModel<>(pContext.bakeLayer(SheepModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new WoolLayer(this, pContext.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(SheepEntity pEntity) {
        switch(pEntity.getVariantEnum()) {
            case COLD -> {
                return COLD_TEXTURE;
            }
            case WARM -> {
                return WARM_TEXTURE;
            }
            case BASIC_2 -> {
                return BASIC_2;
            }
            default -> {
                return BASIC;
            }
        }
    }


}

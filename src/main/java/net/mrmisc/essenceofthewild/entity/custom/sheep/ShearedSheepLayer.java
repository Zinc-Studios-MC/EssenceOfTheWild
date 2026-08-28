package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@OnlyIn(Dist.CLIENT)
public class ShearedSheepLayer extends SheepOverlayLayer {

    public ShearedSheepLayer(GeoRenderer<SheepEntity> renderer) {
        super(renderer, new ShearedGeoModel());
    }

    @Override
    protected boolean appliesTo(SheepEntity sheep) {
        return sheep.isSheared();
    }

    private static class ShearedGeoModel extends GeoModel<SheepEntity> {
        private static final ResourceLocation MODEL = SheepGeoModel.path("geo/entity/sheared_sheep.geo.json");
        private static final ResourceLocation TEXTURE = SheepGeoModel.path("textures/entity/sheep/sheared_sheep.png");

        @Override
        public ResourceLocation getModelResource(SheepEntity animatable) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(SheepEntity animatable) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(SheepEntity animatable) {
            return SheepGeoModel.ANIMATIONS;
        }
    }
}

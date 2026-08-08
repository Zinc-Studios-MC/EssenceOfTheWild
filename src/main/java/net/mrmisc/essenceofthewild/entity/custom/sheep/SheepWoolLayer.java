package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

// the fleece, drawn over a sheep that hasnt been sheared and tinted by its dye colour
@OnlyIn(Dist.CLIENT)
public class SheepWoolLayer extends SheepOverlayLayer {

    public SheepWoolLayer(GeoRenderer<SheepEntity> renderer) {
        super(renderer, new WoolGeoModel());
    }

    @Override
    protected boolean appliesTo(SheepEntity sheep) {
        return !sheep.isSheared();
    }

    private static class WoolGeoModel extends GeoModel<SheepEntity> {
        private static final ResourceLocation MODEL = SheepGeoModel.path("geo/entity/sheep_wool.geo.json");
        private static final ResourceLocation TEXTURE = SheepGeoModel.path("textures/entity/sheep/sheep_wool.png");

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

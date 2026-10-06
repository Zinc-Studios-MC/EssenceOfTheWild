package net.mrmisc.essenceofthewild.entity.custom.spider;

import net.mrmisc.essenceofthewild.util.EOTWUtils;
import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SpiderRenderer extends GeoEntityRenderer<SpiderEntity> {
    public SpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new EotwGeoModel<>("spider", EOTWUtils.getLoc("textures/entity/spider/spider.png")));
        addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected RenderType getRenderType(SpiderEntity animatable) {
                return RenderType.eyes(EOTWUtils.getLoc("textures/entity/spider/spider_glowing.png"));
            }
        });
        this.shadowRadius = 0.7f;
    }

    @Override
    protected float getDeathMaxRotation(SpiderEntity animatable) {
        return 0.0F;
    }
}

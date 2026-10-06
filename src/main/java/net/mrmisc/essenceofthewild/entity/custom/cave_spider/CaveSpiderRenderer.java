package net.mrmisc.essenceofthewild.entity.custom.cave_spider;

import net.mrmisc.essenceofthewild.util.EOTWUtils;
import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class CaveSpiderRenderer extends GeoEntityRenderer<CaveSpiderEntity> {
    public CaveSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new EotwGeoModel<>("cave_spider", EOTWUtils.getLoc("textures/entity/cave_spider/cave_spider.png")));
        addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected RenderType getRenderType(CaveSpiderEntity animatable) {
                return RenderType.eyes(EOTWUtils.getLoc("textures/entity/cave_spider/cave_spider_glowing.png"));
            }
        });
        this.shadowRadius = 0.4f;
    }

    @Override
    protected float getDeathMaxRotation(CaveSpiderEntity animatable) {
        return 0.0F;
    }
}

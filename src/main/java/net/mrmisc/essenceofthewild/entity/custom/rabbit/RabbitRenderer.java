package net.mrmisc.essenceofthewild.entity.custom.rabbit;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RabbitRenderer extends GeoEntityRenderer<RabbitEntity> {
    public RabbitRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new RabbitGeoModel());
        this.shadowRadius = 0.2f;
    }
}

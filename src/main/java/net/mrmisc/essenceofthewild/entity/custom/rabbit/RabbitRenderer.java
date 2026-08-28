package net.mrmisc.essenceofthewild.entity.custom.rabbit;

import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RabbitRenderer extends GeoEntityRenderer<RabbitEntity> {
    public RabbitRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new EotwGeoModel<RabbitEntity>("rabbit", rabbit -> rabbit.getRabbitVariant().location()));
        this.shadowRadius = 0.2f;
    }
}

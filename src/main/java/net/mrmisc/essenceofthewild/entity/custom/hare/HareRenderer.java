package net.mrmisc.essenceofthewild.entity.custom.hare;

import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HareRenderer extends GeoEntityRenderer<HareEntity> {
    public HareRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new EotwGeoModel<HareEntity>("hare", hare -> hare.getRabbitVariant().location()));
        this.shadowRadius = 0.2f;
    }
}

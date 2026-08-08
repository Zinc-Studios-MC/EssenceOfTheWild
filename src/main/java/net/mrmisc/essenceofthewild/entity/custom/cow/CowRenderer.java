package net.mrmisc.essenceofthewild.entity.custom.cow;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CowRenderer extends GeoEntityRenderer<CowEntity> {
    public CowRenderer(EntityRendererProvider.Context context) {
        super(context, new CowGeoModel());
        this.shadowRadius = 0.8f;
    }
}

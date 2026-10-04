package net.mrmisc.essenceofthewild.entity.custom.warthog;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.mrmisc.essenceofthewild.entity.util.AgedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WarthogRenderer extends GeoEntityRenderer<WarthogEntity> {
    public WarthogRenderer(EntityRendererProvider.Context context) {
        super(context, new AgedGeoModel<>("warthog", "baby_warthog", WarthogEntity::getVariant));
        shadowRadius = 0.6F;
    }
}

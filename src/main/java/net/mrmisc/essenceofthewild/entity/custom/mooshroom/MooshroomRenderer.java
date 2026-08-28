package net.mrmisc.essenceofthewild.entity.custom.mooshroom;

import net.mrmisc.essenceofthewild.entity.util.AgedGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MooshroomRenderer extends GeoEntityRenderer<MooshroomEntity> {
    public MooshroomRenderer(EntityRendererProvider.Context context) {
        super(context, new AgedGeoModel<>("mooshroom", "baby_mooshroom", MooshroomEntity::getVariantMooshroom));
        this.shadowRadius = 0.8f;
    }
}

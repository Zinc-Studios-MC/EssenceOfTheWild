package net.mrmisc.essenceofthewild.entity.custom.mooshroom;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MooshroomRenderer extends GeoEntityRenderer<MooshroomEntity> {
    public MooshroomRenderer(EntityRendererProvider.Context context) {
        super(context, new MooshroomGeoModel());
        this.shadowRadius = 0.8f;
    }
}

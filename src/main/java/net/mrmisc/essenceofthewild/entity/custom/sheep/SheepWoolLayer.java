package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.mrmisc.essenceofthewild.util.EOTWUtils;
import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoRenderer;

@OnlyIn(Dist.CLIENT)
public class SheepWoolLayer extends SheepOverlayLayer {

    public SheepWoolLayer(GeoRenderer<SheepEntity> renderer) {
        super(renderer, new EotwGeoModel<>("sheep_wool", "sheep", animatable -> EOTWUtils.getLoc("textures/entity/sheep/sheep_wool.png")));
    }

    @Override
    protected boolean appliesTo(SheepEntity sheep) {
        return !sheep.isSheared();
    }
}

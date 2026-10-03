package net.mrmisc.essenceofthewild.entity.custom.rabbit;

import net.mrmisc.essenceofthewild.entity.util.EotwGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.util.EOTWUtils;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RabbitRenderer extends GeoEntityRenderer<RabbitEntity> {
    public RabbitRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new EotwGeoModel<RabbitEntity>("rabbit", rabbit -> rabbit.getRabbitVariant().location()) {
            private final ResourceLocation babyModel = EOTWUtils.getLoc("geo/entity/baby_rabbit.geo.json");
            private final ResourceLocation babyAnimations = EOTWUtils.getLoc("animations/entity/baby_rabbit.animation.json");
            private final ResourceLocation babyTexture = EOTWUtils.getLoc("textures/entity/rabbit/baby_rabbit_texture.png");
            private final ResourceLocation whiteTexture = EOTWUtils.getLoc("textures/entity/rabbit/baby_white_rabbit_texture.png");
            private final ResourceLocation snowTexture = EOTWUtils.getLoc("textures/entity/rabbit/baby_snow_rabbit_texture.png");

            @Override
            public ResourceLocation getModelResource(RabbitEntity rabbit) {
                return rabbit.isBaby() ? babyModel : super.getModelResource(rabbit);
            }

            @Override
            public ResourceLocation getAnimationResource(RabbitEntity rabbit) {
                return rabbit.isBaby() ? babyAnimations : super.getAnimationResource(rabbit);
            }

            @Override
            public ResourceLocation getTextureResource(RabbitEntity rabbit) {
                if (!rabbit.isBaby()) {
                    return super.getTextureResource(rabbit);
                }
                return switch (rabbit.getVariantId()) {
                    case "basic_grey" -> whiteTexture;
                    case "cold" -> snowTexture;
                    default -> babyTexture;
                };
            }
        });
        this.shadowRadius = 0.2f;
    }
}

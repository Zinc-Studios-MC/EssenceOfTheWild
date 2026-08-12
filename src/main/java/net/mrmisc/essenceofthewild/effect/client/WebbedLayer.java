package net.mrmisc.essenceofthewild.effect.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.mrmisc.essenceofthewild.effect.EOTWEffects;
import net.mrmisc.essenceofthewild.util.EOTWUtils;

import org.jetbrains.annotations.NotNull;

public class WebbedLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public static final ModelLayerLocation WIDE_ARMS =
            new ModelLayerLocation(EOTWUtils.getLoc("webbed_player"), "main");
    public static final ModelLayerLocation SLIM_ARMS =
            new ModelLayerLocation(EOTWUtils.getLoc("webbed_player_slim"), "main");

    private static final ResourceLocation TEXTURE = EOTWUtils.getLoc("textures/entity/misc/webbed_overlay.png");

    private static final float INFLATE = 0.25F;

    private final PlayerModel<AbstractClientPlayer> model;

    public WebbedLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
                       EntityModelSet models, boolean slim) {
        super(parent);
        this.model = new PlayerModel<>(models.bakeLayer(slim ? SLIM_ARMS : WIDE_ARMS), slim);
    }

    public static LayerDefinition createLayer(boolean slim) {
        return LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(INFLATE), slim), 64, 64);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        if (player.isInvisible() || player.isSpectator() || !player.hasEffect(EOTWEffects.WEBBED.get())) {
            return;
        }

        this.getParentModel().copyPropertiesTo(this.model);
        hideOuterSkinLayers();
        this.model.prepareMobModel(player, limbSwing, limbSwingAmount, partialTick);
        this.model.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE));
        this.model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void hideOuterSkinLayers() {
        this.model.hat.visible = false;
        this.model.jacket.visible = false;
        this.model.leftSleeve.visible = false;
        this.model.rightSleeve.visible = false;
        this.model.leftPants.visible = false;
        this.model.rightPants.visible = false;
    }
}

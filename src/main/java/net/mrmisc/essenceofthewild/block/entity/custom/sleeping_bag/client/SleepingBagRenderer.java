package net.mrmisc.essenceofthewild.block.entity.custom.sleeping_bag.client;
// blockbench export


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.block.custom.sleeping_bag.SleepingBagBlock;
import net.mrmisc.essenceofthewild.block.entity.EOTWBlockEntities;
import net.mrmisc.essenceofthewild.block.entity.custom.sleeping_bag.server.SleepingBagBlockEntity;

@OnlyIn(Dist.CLIENT)
public class SleepingBagRenderer implements BlockEntityRenderer<SleepingBagBlockEntity> {
	private final ModelPart headRoot;
	private final ModelPart footRoot;

	public static ModelLayerLocation HEAD = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "sleeping_bag_head"), "main");
	public static ModelLayerLocation FOOT = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, "sleeping_bag_foot"), "main");

	public SleepingBagRenderer(BlockEntityRendererProvider.Context context){
		ModelPart headLayer = context.bakeLayer(HEAD);
		ModelPart footLayer = context.bakeLayer(FOOT);

		this.headRoot = headLayer.getChild("head");
		this.footRoot = footLayer.getChild("foot");
	}

	public static LayerDefinition createFootLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition foot = partdefinition.addOrReplaceChild("foot", CubeListBuilder.create(), PartPose.offset(8.0F, 24.0F, -8.0F));

		PartDefinition foot_r1 = foot.addOrReplaceChild("foot_r1", CubeListBuilder.create().texOffs(0, 23).addBox(-8.0F, -4.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 8.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public static LayerDefinition createHeadLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(59, 87).mirror().addBox(-24.0F, -4.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void render(SleepingBagBlockEntity pBlockEntity, float partialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
		headRoot.xRot = (float) Math.PI;
		footRoot.xRot = (float) Math.PI;
        Level level = pBlockEntity.getLevel();
		if (level != null) {
			BlockState state = pBlockEntity.getBlockState();
			DoubleBlockCombiner.NeighborCombineResult<? extends SleepingBagBlockEntity> combineResult = DoubleBlockCombiner.combineWithNeigbour(EOTWBlockEntities.SLEEPING_BAG.get(), BedBlock::getBlockType, BedBlock::getConnectedDirection, ChestBlock.FACING, state, level, pBlockEntity.getBlockPos(), (a, b) -> false);
			int light = combineResult.apply(new BrightnessCombiner<SleepingBagBlockEntity>()).get(pPackedLight);
			this.renderPiece(pPoseStack, pBuffer, state.getValue(BedBlock.PART) == BedPart.HEAD ? this.headRoot : this.footRoot, state.getValue(BedBlock.FACING), light, pPackedOverlay, pBlockEntity);
		}
		else {
			this.renderPiece(pPoseStack, pBuffer, this.headRoot, Direction.SOUTH, pPackedLight, pPackedOverlay, pBlockEntity);
			this.renderPiece(pPoseStack, pBuffer, this.footRoot, Direction.SOUTH, pPackedLight, pPackedOverlay, pBlockEntity);
		}
	}


	private void renderPiece(PoseStack poseStack, MultiBufferSource bufferSource, ModelPart modelPart,
							 Direction direction, int packedLight, int packedOverlay, SleepingBagBlockEntity entity) {
		poseStack.pushPose();

		poseStack.translate(0.5D, -1.0D, 0.5D);

		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - direction.toYRot()));

		poseStack.translate(0, -0.5, 1);
		poseStack.mulPose(Axis.YP.rotationDegrees(0));
		SleepingBagBlock block = (SleepingBagBlock) entity.getBlockState().getBlock();

		String path = "textures/entity/sleeping_bag/" + block.getColor() + "_sleeping_bag.png";
		ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(EssenceOfTheWildMod.MOD_ID, path);

		VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(texture));
		modelPart.render(poseStack, consumer, packedLight, packedOverlay);

		poseStack.popPose();
	}
}
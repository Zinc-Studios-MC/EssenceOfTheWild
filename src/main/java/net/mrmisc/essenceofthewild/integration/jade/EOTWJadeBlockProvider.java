package net.mrmisc.essenceofthewild.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.mrmisc.essenceofthewild.block.custom.cheesemaker.CheeseMakerBlock;
import net.mrmisc.essenceofthewild.block.custom.misc.MangoBlock;
import net.mrmisc.essenceofthewild.block.entity.custom.cheesemaker.CheeseMakerBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.freezer.WoodenFreezerBlockEntity;
import net.mrmisc.essenceofthewild.block.entity.custom.nest.NestBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EOTWJadeBlockProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();

        if (data.contains("NestEggs")) {
            int eggs = data.getInt("NestEggs");
            if (eggs <= 0) {
                tooltip.add(Component.translatable("tooltip.essenceofthewild.nest.empty"));
            } else {
                tooltip.add(Component.translatable("tooltip.essenceofthewild.nest.eggs", eggs));
                tooltip.add(Component.translatable("tooltip.essenceofthewild.nest.hatching", formatTicks(data.getInt("NestRemaining"))));
            }
            return;
        }

        if (data.contains("FreezerMilk")) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.wooden_freezer.milk", data.getInt("FreezerMilk"), WoodenFreezerBlockEntity.MAX_MILK_LEVEL));
            tooltip.add(Component.translatable("tooltip.essenceofthewild.wooden_freezer.ice", data.getInt("FreezerIce"), WoodenFreezerBlockEntity.MAX_ICE_LEVEL));

            if (data.getInt("FreezerProgress") > 0) {
                tooltip.add(Component.translatable("tooltip.essenceofthewild.wooden_freezer.progress", data.getInt("FreezerProgress"), data.getInt("FreezerMaxProgress")));
            }
            return;
        }

        if (data.contains("CheeseContent")) {
            int content = data.getInt("CheeseContent");
            if (content == CheeseMakerBlock.EMPTY) {
                tooltip.add(Component.translatable("tooltip.essenceofthewild.cheese_maker.empty"));
            } else if (content == CheeseMakerBlock.MILK) {
                tooltip.add(Component.translatable("tooltip.essenceofthewild.cheese_maker.milk", formatTicks(data.getInt("CheeseRemaining"))));
            } else if (content == CheeseMakerBlock.CHEESE) {
                tooltip.add(Component.translatable("tooltip.essenceofthewild.cheese_maker.ready"));
            }
            return;
        }

        if (accessor.getBlockState().getBlock() instanceof MangoBlock) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.mango"));
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        BlockEntity blockEntity = accessor.getBlockEntity();

        if (blockEntity instanceof NestBlockEntity nest) {
            data.putInt("NestEggs", nest.getEggCount());
            data.putInt("NestRemaining", nest.getRemainingHatchTicks());
            data.putInt("NestProgress", nest.getHatchProgress());
            data.putInt("NestMaxProgress", nest.getMaxHatchTicks());

            ResourceLocation hatchEntityId = nest.getHatchEntityId();
            if (hatchEntityId != null) {
                data.putString("NestEntity", hatchEntityId.toString());
            }
        } else if (blockEntity instanceof WoodenFreezerBlockEntity freezer) {
            data.putInt("FreezerMilk", freezer.getMilkLevel());
            data.putInt("FreezerIce", freezer.getIceLevel());
            data.putInt("FreezerProgress", freezer.getProgress());
            data.putInt("FreezerMaxProgress", freezer.getMaxProgress());
        } else if (blockEntity instanceof CheeseMakerBlockEntity cheeseMaker) {
            data.putInt("CheeseContent", cheeseMaker.getContent());
            data.putInt("CheeseRemaining", Math.max(0, cheeseMaker.getMaxProgress() - cheeseMaker.getProgress()));
            data.putInt("CheeseProgress", cheeseMaker.getProgress());
            data.putInt("CheeseMaxProgress", cheeseMaker.getMaxProgress());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return EOTWJadePlugin.BLOCKS;
    }

    static String formatTicks(int ticks) {
        int seconds = Math.max(0, ticks + 19) / 20;
        int minutes = seconds / 60;
        int remainder = seconds % 60;
        return minutes + ":" + (remainder < 10 ? "0" : "") + remainder;
    }
}

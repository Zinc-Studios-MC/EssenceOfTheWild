package net.mrmisc.essenceofthewild.block.custom.crops;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class RedOnionCropBlock extends BeetrootBlock {
    public RedOnionCropBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return super.getAgeProperty();
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return super.getBaseSeedId();
    }
}

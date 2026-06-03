package net.mrmisc.essenceofthewild.entity.misc.arrow;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.item.EOTWItems;

public class UnderwaterArrow extends Arrow {
    public UnderwaterArrow(EntityType<? extends UnderwaterArrow> type, Level level) {
        super(type, level);
    }

    public UnderwaterArrow(Level level, LivingEntity shooter) {
        super(level, shooter);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(EOTWItems.UNDERWATER_ARROW.get());
    }

    @Override
    protected float getWaterInertia() {
        return 1.0f;
    }
}

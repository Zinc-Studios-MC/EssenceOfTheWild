package net.mrmisc.essenceofthewild.item.custom.tools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.misc.arrow.UnderwaterArrow;

public class UnderwaterArrowItem extends ArrowItem {
    public UnderwaterArrowItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
        return new UnderwaterArrow(pLevel, pShooter);
    }
}

package net.mrmisc.essenceofthewild.item.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EffectIceCream extends IceCream {

    MobEffect mobEffect;

    public EffectIceCream(MobEffect effect) {
        super();
        this.mobEffect = effect;
    }

    public MobEffect getMobEffect() {
        return mobEffect;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        pLivingEntity.addEffect(new MobEffectInstance(mobEffect, 100, 1, true, true, true));
        pStack.shrink(1);
        return pStack;
    }
}

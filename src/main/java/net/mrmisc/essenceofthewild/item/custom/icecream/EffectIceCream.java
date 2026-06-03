package net.mrmisc.essenceofthewild.item.custom.icecream;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EffectIceCream extends IceCream {

    MobEffect mobEffect;

    public EffectIceCream(MobEffect effect) {
        super();
        this.mobEffect = effect;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("Effect: " + getMobEffect().getDisplayName().getString() + " II (0:20)").withStyle(ChatFormatting.BLUE));
    }

    public MobEffect getMobEffect() {
        return mobEffect;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        pLivingEntity.addEffect(new MobEffectInstance(mobEffect, 400, 1, true, true, true));
        pStack.shrink(1);
        return pStack;
    }
}

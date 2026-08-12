package net.mrmisc.essenceofthewild.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class WebbedEffect extends MobEffect {

    private static final String SPEED_MODIFIER = "6ffb0dcb-1f7c-4e6a-9c60-2d1cf3a4b7e2";
    private static final double SPEED_PENALTY = -0.45D;

    public WebbedEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER, SPEED_PENALTY,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}

package net.mrmisc.essenceofthewild.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mrmisc.essenceofthewild.damagesource.EOTWDamageSources;
import net.mrmisc.essenceofthewild.effect.EOTWEffects;
import net.mrmisc.essenceofthewild.item.EOTWItems;

import java.util.List;

public class RabiesEffect extends MobEffect {


    //TODO: Change * 1 to * 6
    private static int TOTAL_DURATION = 20 * 60 * 1;
    private static int NAUSEA_INTERVAL = 20 * 45;


    public RabiesEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return List.of(new ItemStack(EOTWItems.VANILLA_ICECREAM.get()));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        MobEffectInstance instance = entity.getEffect(EOTWEffects.RABIES.get());
        if (instance == null) return;

        int remaining = TOTAL_DURATION - instance.getDuration();
        if (entity.isInWater() && remaining % 20 == 0) {
            entity.hurt(EOTWDamageSources.rabiesWaterDamage(entity), 1);
        }
        if (remaining % NAUSEA_INTERVAL == 0 && remaining > 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));
        }
        if (TOTAL_DURATION - remaining <= 2) {
            entity.hurt(EOTWDamageSources.rabiesFinalDamage(entity), 1000f);
        }
    }

}

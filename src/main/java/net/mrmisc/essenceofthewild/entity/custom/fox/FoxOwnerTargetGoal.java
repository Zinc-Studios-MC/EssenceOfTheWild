package net.mrmisc.essenceofthewild.entity.custom.fox;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

class FoxOwnerTargetGoal extends TargetGoal {
    private final FoxEntity fox;
    private final boolean defend;
    private LivingEntity target;
    private int timestamp;

    FoxOwnerTargetGoal(FoxEntity fox, boolean defend) {
        super(fox, false);
        this.fox = fox;
        this.defend = defend;
        setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = fox.getOwner();
        if (!fox.readyToHunt() || owner == null) {
            return false;
        }
        target = defend ? owner.getLastHurtByMob() : owner.getLastHurtMob();
        int time = defend ? owner.getLastHurtByMobTimestamp() : owner.getLastHurtMobTimestamp();
        return time != timestamp && canAttack(target, TargetingConditions.DEFAULT);
    }

    @Override
    public void start() {
        fox.setTarget(target);
        LivingEntity owner = fox.getOwner();
        timestamp = defend ? owner.getLastHurtByMobTimestamp() : owner.getLastHurtMobTimestamp();
        super.start();
    }
}

package net.mrmisc.essenceofthewild.entity.custom.ferret;

import java.util.List;

import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;

public class AttackRabbitGoal extends MeleeAttackGoal{

    private final FerretEntity entity;
    private Rabbit target = null;

    public AttackRabbitGoal(FerretEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = pMob;
    }
    @Override
    public boolean canUse() {
        return this.entity.getHealth()<(this.entity.getMaxHealth()/2) && !this.entity.level().getEntitiesOfClass(Rabbit.class, this.entity.getBoundingBox().inflate(10)).isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getHealth()<(this.entity.getMaxHealth()/2) && !this.entity.level().getEntitiesOfClass(Rabbit.class, this.entity.getBoundingBox().inflate(10)).isEmpty();
    }

    @Override
    public void start() {
        super.start();
        List<Rabbit> rabbits =
        this.entity.level().getEntitiesOfClass(Rabbit.class, this.entity.getBoundingBox().inflate(10));
        if(!rabbits.isEmpty()){
            target = rabbits.get(0);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.entity.setRunning(true);
        if(this.entity.distanceTo(target)>1){
            this.entity.getNavigation().moveTo(target, 1);
        }else{
            this.target.kill();
            this.entity.setHealth(this.entity.getHealth()+(float)1.5);
            List<ItemEntity> rabbits = this.entity.level().getEntitiesOfClass(ItemEntity.class, this.entity.getBoundingBox().inflate(1), (e)->{
                return e.getItem().is(Items.RABBIT);
            });
            if(!rabbits.isEmpty()){
                for(ItemEntity ie : rabbits){
                    ie.remove(RemovalReason.DISCARDED);
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setRunning(false);
    }
    
}

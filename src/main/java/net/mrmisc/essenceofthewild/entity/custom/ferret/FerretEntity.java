package net.mrmisc.essenceofthewild.entity.custom.ferret;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;

public class FerretEntity extends TamableAnimal{

    public FerretEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
        .add(Attributes.MAX_HEALTH, 14)
        .add(Attributes.MOVEMENT_SPEED, 0.5)
        .add(Attributes.FOLLOW_RANGE, 25);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return EOTWEntities.FERRET.get().create(pLevel);
    }

    @Override
    public boolean canBreed() {
        //false cause no baby
        return false;
    }
}
package net.mrmisc.essenceofthewild.entity.custom.ferret;

import net.minecraft.world.entity.ai.goal.Goal;

//blank cause i'm still planning
public class FindOrDigBurrowsGoal extends Goal{

    private final FerretEntity entity;

    public FindOrDigBurrowsGoal(FerretEntity pEntity){
        this.entity = pEntity;
    }

    @Override
    public boolean canUse() {
        return this.entity.level().isNight() && !this.entity.isBaby();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void stop() {
        super.stop();
    }
}

/*

locate closest usable burrow,

if there is none, make on yourself,

while making one keep checking if a new one was created and is usable,

if so move to it, if not go into own burrow

--------------------------------------------

start:
get every burrow in a +-10 block area,

remove blocks if they are preoccupied,

save the remaining into another list,

if list is empty then set "hasToDigOwn" flag, if not then pick one,

tick:
if has to dig own then keep checking if there are any other new free burrows,
if not, keep digging, if yes, move to said burrow,

if not has to dig own, move to burrow,
if close enough to the burrow then check if it's still usable, 
if yes go in, if not check for other burrows, if there are any, go in, if not make own
*/
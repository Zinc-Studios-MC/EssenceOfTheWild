package net.mrmisc.essenceofthewild.entity.custom.sheep;

import net.minecraft.world.entity.ai.goal.EatBlockGoal;

public class SheepEatBlockGoal extends EatBlockGoal {
    private final SheepEntity sheep;

    public SheepEatBlockGoal(SheepEntity sheep) {
        super(sheep);
        this.sheep = sheep;
    }

    @Override
    public void start() {
        super.start();
        this.sheep.setEating(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.sheep.setEating(false);
    }
}
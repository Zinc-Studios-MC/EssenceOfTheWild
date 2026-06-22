package net.mrmisc.essenceofthewild.entity.custom.ferret;

import net.minecraft.world.entity.ai.goal.PanicGoal;

public class FerretPanicGoal extends PanicGoal{

    private final FerretEntity en;

    public FerretPanicGoal(FerretEntity pMob, double pSpeedModifier) {
        super(pMob, pSpeedModifier);
        en = pMob;
    }

    @Override
    public void start() {
        super.start();
        en.setRunning(true);
    }
    
    @Override
    public void stop() {
        super.stop();
        en.setRunning(false);
    }
}
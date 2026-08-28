package net.mrmisc.essenceofthewild.entity.misc.arrow;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.item.EOTWItems;

public class UnderwaterArrow extends Arrow {
    private static final double BUBBLES_PER_BLOCK = 4.0d;
    private static final double BUBBLE_SPREAD = 0.1d;

    private boolean ignoreWater;

    public UnderwaterArrow(EntityType<? extends UnderwaterArrow> type, Level level) {
        super(type, level);
    }

    public UnderwaterArrow(Level level, LivingEntity shooter) {
        super(EOTWEntities.UNDERWATER_ARROW.get(), level);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1d, shooter.getZ());
        this.setOwner(shooter);
        if (shooter instanceof Player) {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
    }

    @Override
    public void tick() {
        Vec3 from = this.position();

        this.ignoreWater = true;
        try {
            super.tick();
        } finally {
            this.ignoreWater = false;
        }

        if (this.level().isClientSide && this.isInWater()) {
            this.spawnBubbleTrail(from, this.position());
        }
    }

    private void spawnBubbleTrail(Vec3 from, Vec3 to) {
        Vec3 step = to.subtract(from);
        double length = step.length();
        if (length < 1.0e-4d) {
            return;
        }

        int count = Mth.clamp(Mth.ceil(length * BUBBLES_PER_BLOCK), 1, 24);
        for (int i = 0; i < count; ++i) {
            double t = (i + this.random.nextDouble()) / count;
            this.level().addParticle(ParticleTypes.BUBBLE,
                    from.x + step.x * t + this.spread(),
                    from.y + step.y * t + this.spread(),
                    from.z + step.z * t + this.spread(),
                    0.0d, 0.0d, 0.0d);
        }
    }

    private double spread() {
        return (this.random.nextDouble() - 0.5d) * BUBBLE_SPREAD;
    }

    @Override
    public boolean isInWater() {
        return !this.ignoreWater && super.isInWater();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(EOTWItems.UNDERWATER_ARROW.get());
    }
}

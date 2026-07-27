package net.mrmisc.essenceofthewild.entity.misc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.mrmisc.essenceofthewild.entity.EOTWEntities;
import net.mrmisc.essenceofthewild.entity.custom.duck.DuckEntity;
import net.mrmisc.essenceofthewild.item.EOTWItems;

/**
 * A thrown duck egg. Behaves like a vanilla thrown chicken egg: harmless on hit, breaks on impact,
 * and has a small chance to hatch a duckling (rarely a small brood). Any duckling that hatches
 * imprints on whoever threw the egg.
 */
public class ThrownDuckEgg extends ThrowableItemProjectile {

    public ThrownDuckEgg(EntityType<? extends ThrownDuckEgg> type, Level level) {
        super(type, level);
    }

    public ThrownDuckEgg(Level level, LivingEntity shooter) {
        super(EOTWEntities.THROWN_DUCK_EGG.get(), shooter, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            if (this.random.nextInt(8) == 0) {
                int count = 1;
                if (this.random.nextInt(32) == 0) {
                    count = 4;
                }
                for (int i = 0; i < count; i++) {
                    DuckEntity duckling = EOTWEntities.DUCK.get().create(this.level());
                    if (duckling == null) {
                        continue;
                    }
                    duckling.setAge(-24000);
                    duckling.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    if (this.getOwner() instanceof LivingEntity shooter) {
                        duckling.imprintOn(shooter);
                    }
                    this.level().addFreshEntity(duckling);
                }
            }

            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return EOTWItems.DUCK_EGG.get();
    }
}

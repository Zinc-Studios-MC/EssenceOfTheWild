package net.mrmisc.essenceofthewild.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.mrmisc.essenceofthewild.entity.misc.SilkBall;

import java.util.List;
import java.util.function.Predicate;

public class WebCannonItem extends ProjectileWeaponItem {
    public WebCannonItem(Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.is(Items.STRING);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return InteractionResultHolder.fail(stack);
        }
        boolean ammo = player.getProjectile(stack).is(Items.STRING) || player.getAbilities().instabuild;
        InteractionResultHolder<ItemStack> result = ForgeEventFactory.onArrowNock(stack, level, player, hand, ammo);
        if (result != null) {
            return result;
        }
        if (!ammo) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        if (!(shooter instanceof Player player) || level.isClientSide || stack.getDamageValue() >= stack.getMaxDamage()) {
            return;
        }
        ItemStack ammo = player.getProjectile(stack);
        boolean creative = player.getAbilities().instabuild;
        int charge = ForgeEventFactory.onArrowLoose(stack, level, player, getUseDuration(stack) - timeLeft,
                creative || ammo.is(Items.STRING));
        if (charge < 0 || !creative && !ammo.is(Items.STRING)) {
            return;
        }
        float power = BowItem.getPowerForTime(charge);
        if (power < 0.1F) {
            return;
        }
        SilkBall ball = new SilkBall(level, player, 1F + power * 2F);
        ball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, power * 3F, 1F);
        if (!level.addFreshEntity(ball)) {
            return;
        }
        if (!creative) {
            ammo.shrink(1);
            if (ammo.isEmpty()) {
                player.getInventory().removeItem(ammo);
            }
            stack.setDamageValue(stack.getDamageValue() + 1);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_SHOOT,
                SoundSource.PLAYERS, 1F, 1F / (level.random.nextFloat() * 0.4F + 1.2F) + power * 0.5F);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack material) {
        return material.is(Items.IRON_INGOT);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            tooltip.add(Component.translatable("item.essenceofthewild.web_cannon.broken").withStyle(ChatFormatting.RED));
        }
    }
}

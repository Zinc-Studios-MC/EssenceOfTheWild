package net.mrmisc.essenceofthewild.mixin;

import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.mrmisc.essenceofthewild.entity.custom.fox.FoxEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Fox.class)
public class FoxEatingMixin {
    @Inject(method = "canEat", at = @At("HEAD"), cancellable = true)
    private void keepDelivery(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof FoxEntity fox && fox.isTame()) {
            cir.setReturnValue(false);
        }
    }
}

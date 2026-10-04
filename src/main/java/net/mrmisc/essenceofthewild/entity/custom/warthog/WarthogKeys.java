package net.mrmisc.essenceofthewild.entity.custom.warthog;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, value = Dist.CLIENT)
public class WarthogKeys {
    private static final KeyMapping CHARGE = new KeyMapping("key.essenceofthewild.warthog_charge", KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.essenceofthewild");

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft client = Minecraft.getInstance();
        while (CHARGE.consumeClick()) {
            if (client.screen == null && client.player != null && client.player.getVehicle() instanceof WarthogEntity) {
                WarthogChargePacket.send();
            }
        }
    }

    @Mod.EventBusSubscriber(modid = EssenceOfTheWildMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Registration {
        @SubscribeEvent
        public static void register(RegisterKeyMappingsEvent event) {
            event.register(CHARGE);
        }
    }
}

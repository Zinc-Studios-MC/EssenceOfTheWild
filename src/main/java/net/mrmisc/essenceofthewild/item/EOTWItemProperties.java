package net.mrmisc.essenceofthewild.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.mrmisc.essenceofthewild.util.EOTWUtils;

public class EOTWItemProperties {

    public static void register() {
        ItemProperties.register(Items.BOW, EOTWUtils.getLoc("underwater_arrow"),
                (stack, level, entity, seed) -> {
                    if (!(entity instanceof Player player) || !entity.isUsingItem() || entity.getUseItem() != stack) {
                        return 0.0f;
                    }
                    return player.getProjectile(stack).is(EOTWItems.UNDERWATER_ARROW.get()) ? 1.0f : 0.0f;
                });
    }
}

package net.mrmisc.essenceofthewild.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.mrmisc.essenceofthewild.util.EOTWUtils;

// client only, keep this behind Dist.CLIENT callers
public class EOTWItemProperties {

    // feeds the extra overrides in assets/minecraft/models/item/bow.json so a bow pulled with an
    // underwater arrow shows that instead of a normal one, only works for your own player since you
    // dont get other people's inventories, so their bows keep the vanilla pulling textures
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

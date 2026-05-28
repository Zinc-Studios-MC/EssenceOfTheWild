package net.mrmisc.essenceofthewild.menu;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.menu.freezer.WoodenFreezerMenu;

public class EOTWMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, EssenceOfTheWildMod.MOD_ID);

    public static final RegistryObject<MenuType<WoodenFreezerMenu>> WOODEN_FREEZER =
            MENU_TYPES.register("wooden_freezer", () -> IForgeMenuType.create(WoodenFreezerMenu::new));
}

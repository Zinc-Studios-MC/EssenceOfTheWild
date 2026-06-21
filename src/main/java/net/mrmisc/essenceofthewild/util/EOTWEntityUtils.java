package net.mrmisc.essenceofthewild.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretEntity;

public class EOTWEntityUtils {

    private static String key = "ClickedFerret";

    public static void setFerretClicked(FerretEntity fe, Player p){
        CompoundTag tag = p.getPersistentData();
        tag.putString(key, fe.getStringUUID());
    }
    public static String getPlayerClicked(Player p){
        return p.getPersistentData().getString(key);
    }
    public static void removeFerretClicked(Player p){
        p.getPersistentData().remove(key);
    }
}
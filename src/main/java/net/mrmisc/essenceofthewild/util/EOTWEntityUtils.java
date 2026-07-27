package net.mrmisc.essenceofthewild.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretEntity;
import net.mrmisc.essenceofthewild.entity.custom.rat.RatEntity;

public class EOTWEntityUtils {

    private static String key = "ClickedFerret";
    private static String ratKey = "ClickedRat";

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

    public static void setRatClicked(RatEntity re, Player p){
        p.getPersistentData().putString(ratKey, re.getStringUUID());
    }
    public static String getRatClicked(Player p){
        return p.getPersistentData().getString(ratKey);
    }
    public static void removeRatClicked(Player p){
        p.getPersistentData().remove(ratKey);
    }
}
package net.mrmisc.essenceofthewild.util;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import net.mrmisc.essenceofthewild.entity.custom.ferret.FerretEntity;

public class EOTWEntityUtils {

    public static final EntityDataAccessor<String> CLICKED = SynchedEntityData.defineId(Player.class, EntityDataSerializers.STRING);

    private static boolean defined = false;

    public static void setFerretClicked(FerretEntity fe, Player p){
        SynchedEntityData enData = p.getEntityData();
        if(!defined){
            enData.define(CLICKED, "");
            defined = true;
        }
        enData.set(CLICKED, fe.getStringUUID());
        enData.packDirty();
    }
    public static String getPlayerClicked(Player p){
        return p.getEntityData().get(CLICKED);
    }
}
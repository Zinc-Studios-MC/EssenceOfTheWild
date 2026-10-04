package net.mrmisc.essenceofthewild.entity.custom.warthog;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.mrmisc.essenceofthewild.util.EOTWUtils;

public class WarthogChargePacket {
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(EOTWUtils.getLoc("warthog"),
            () -> "1", "1"::equals, "1"::equals);

    public static void register() {
        CHANNEL.messageBuilder(WarthogChargePacket.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .encoder((packet, buf) -> {})
                .decoder(WarthogChargePacket::new)
                .consumerMainThread((packet, context) -> {
                    ServerPlayer player = context.get().getSender();
                    if (player != null && player.getVehicle() instanceof WarthogEntity warthog) {
                        warthog.charge(player);
                    }
                    context.get().setPacketHandled(true);
                }).add();
    }

    public WarthogChargePacket() {
    }

    private WarthogChargePacket(FriendlyByteBuf buf) {
    }

    public static void send() {
        CHANNEL.sendToServer(new WarthogChargePacket());
    }
}

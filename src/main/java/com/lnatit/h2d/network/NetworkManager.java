package com.lnatit.h2d.network;

import com.lnatit.h2d.capability.HistoryProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static com.lnatit.h2d.Hard2Dig.MOD_ID;

public class NetworkManager
{
    public static final String CHANNEL_ID = MOD_ID;
    private static final String CHANNEL_VER = "d0ink6";

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CHANNEL_ID, "channel"),
            () -> CHANNEL_VER,
            CHANNEL_VER::equals,
            CHANNEL_VER::equals
    );

    public static void register()
    {
        CHANNEL.registerMessage(0, HistorySyncPacket.class,
                                HistorySyncPacket::encode,
                                HistorySyncPacket::new,
                                HistorySyncPacket::handle,
                                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }

    public static void syncHistoryTo(ServerPlayer player, HistorySyncPacket packet)
    {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}

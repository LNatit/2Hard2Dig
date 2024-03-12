package com.lnatit.h2d.network;

import com.lnatit.h2d.capability.HistoryProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HistorySyncPacket
{
    public final int digCount, digCooling;
    public final BlockPos lastDigPos;

    public HistorySyncPacket(int digCount, int digCooling, BlockPos lastDigPos)
    {
        this.digCount = digCount;
        this.digCooling = digCooling;
        this.lastDigPos = lastDigPos;
    }

    public HistorySyncPacket(FriendlyByteBuf buf)
    {
        this.digCount = buf.readInt();
        this.digCooling = buf.readInt();
        this.lastDigPos = buf.readNullable(FriendlyByteBuf::readBlockPos);
    }

    public void encode(FriendlyByteBuf buf)
    {
        buf.writeInt(this.digCount);
        buf.writeInt(this.digCooling);
        buf.writeNullable(this.lastDigPos, FriendlyByteBuf::writeBlockPos);
    }

    public static void handle(HistorySyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null)
                player.getCapability(HistoryProvider.HISTORY).ifPresent(cap -> cap.syncFrom(packet));
        });
        context.setPacketHandled(true);
    }
}

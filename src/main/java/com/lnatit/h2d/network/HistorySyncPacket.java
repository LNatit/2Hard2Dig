package com.lnatit.h2d.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
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
        this.lastDigPos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf)
    {
        buf.writeInt(this.digCount);
        buf.writeInt(this.digCooling);
        buf.writeBlockPos(this.lastDigPos);
    }

    public static void handle(HistorySyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> );
    }
}

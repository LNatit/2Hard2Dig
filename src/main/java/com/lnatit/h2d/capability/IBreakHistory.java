package com.lnatit.h2d.capability;

import com.lnatit.h2d.network.HistorySyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public interface IBreakHistory
{
    IBreakHistory update(BlockPos pos);
    float getSpeed(float original, float minimum);
    void tick();
    void clear();
    // Synchronization Methods
    void sync(ServerPlayer player);
    void syncFrom(HistorySyncPacket packet);
    HistorySyncPacket toPacket();
}

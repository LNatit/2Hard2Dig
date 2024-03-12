package com.lnatit.h2d.capability;

import com.lnatit.h2d.network.HistorySyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IBreakHistory
{
    IBreakHistory update(BlockPos pos);
    float getSpeed(float original, float minimum);
    void tick();
    void clear();
    void sync(ServerPlayer player);
    void syncFrom(HistorySyncPacket packet);
    HistorySyncPacket toPacket();
}

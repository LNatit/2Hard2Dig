package com.lnatit.h2d.capability;

import com.lnatit.h2d.network.HistorySyncPacket;
import com.lnatit.h2d.network.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.common.util.INBTSerializable;

public class PlayerHistory implements IBreakHistory, INBTSerializable<CompoundTag>
{
    private int digCount = MIN_COUNT;
    public static final String TAG_COUNT = "dig_count";
    private int digCooling = 0;
    public static final String TAG_COOLING = "dig_cooling";
    private BlockPos lastDigPos;
    public static final String TAG_POS = "last_pos";
    public static final int MIN_COUNT = -12;
    public static final int MAX_COUNT = 4;
    public static final int MAX_COOLING = 800;
    public static final int IDLE_COOLING = 200;
    public static final double POS_DISTANCE = 10.0;

    // TODO sync
    @Override
    public IBreakHistory update(BlockPos pos)
    {
        if (this.lastDigPos == null)
        {
            this.digCount++;
            this.digCooling = MAX_COOLING;
        }
        else if (this.lastDigPos.closerThan(pos, POS_DISTANCE))
        {
            if (!this.lastDigPos.equals(pos))
            {
                this.digCount++;
                this.digCooling = MAX_COOLING;
            }
        }
        else
        {
            int dy = Math.abs(this.lastDigPos.getY() - pos.getY());
            if (dy > POS_DISTANCE)
            {
                this.digCount = MIN_COUNT;
            }
            else
            {
                this.digCount = MIN_COUNT / 2;
                this.digCooling = IDLE_COOLING;
            }
        }

        this.lastDigPos = pos;
        // debug codes
//        LOGGER.info("Current count: " + digCount);
//        LOGGER.info("Current cooling: " + digCooling);
//        LOGGER.info("Current pos: " + lastDigPos.toString());
        return this;
    }

    @Override
    public float getSpeed(float original, float minimum)
    {
        if (this.digCount > 0)
        {
            float p = (float) digCount / MAX_COUNT;
            return Mth.lerp(p > 1 ? 1 : p, original, minimum);
        }
        return original;
    }

    @Override
    public void tick()
    {
        if (this.digCooling > 0)
        {
            this.digCooling--;
        }
        else
        {
            if (this.digCount > 0)
                this.digCooling = IDLE_COOLING;
            if (this.digCount > MIN_COUNT)
                this.digCount--;
        }
    }

    @Override
    public void clear()
    {
        this.digCount = MIN_COUNT;
        this.digCooling = 0;
        this.lastDigPos = null;
    }

    @Override
    public void sync(ServerPlayer player)
    {
        NetworkManager.syncHistoryTo(player, this.toPacket());
    }

    @Override
    public void syncFrom(HistorySyncPacket packet)
    {
        this.digCount = packet.digCount;
        this.digCooling = packet.digCooling;
        this.lastDigPos = packet.lastDigPos;
    }

    @Override
    public HistorySyncPacket toPacket()
    {
        return new HistorySyncPacket(this.digCount, this.digCooling, this.lastDigPos);
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(TAG_COUNT, digCount);
        tag.putInt(TAG_COOLING, digCooling);
        if (this.lastDigPos != null)
            tag.put(TAG_POS, fromPos(this.lastDigPos));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        this.digCount = nbt.getInt(TAG_COUNT);
        this.digCooling = nbt.getInt(TAG_COOLING);
        if (nbt.contains(TAG_POS, Tag.TAG_COMPOUND))
            this.lastDigPos = fromTag(nbt.getCompound(TAG_POS));
    }

    private static CompoundTag fromPos(BlockPos pos)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        return tag;
    }

    private static BlockPos fromTag(CompoundTag tag)
    {
        int x = tag.getInt("x");
        int y = tag.getInt("y");
        int z = tag.getInt("z");
        return new BlockPos(x, y, z);
    }
}

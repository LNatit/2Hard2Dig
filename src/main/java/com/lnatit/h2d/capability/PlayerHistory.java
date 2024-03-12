package com.lnatit.h2d.capability;

import com.lnatit.h2d.network.HistorySyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

import static com.mojang.text2speech.Narrator.LOGGER;

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
        LOGGER.info("Current count: " + digCount);
        LOGGER.info("Current cooling: " + digCooling);
        LOGGER.info("Current pos: " + lastDigPos.toString());
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
            tag.put(TAG_POS, from(this.lastDigPos));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        this.digCount = nbt.getInt(TAG_COUNT);
        this.digCooling = nbt.getInt(TAG_COOLING);
        if (nbt.contains(TAG_POS, Tag.TAG_COMPOUND))
            this.lastDigPos = from(nbt.getCompound(TAG_POS));
    }

    private static CompoundTag from(BlockPos pos)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        return tag;
    }

    private static BlockPos from(CompoundTag tag)
    {
        int x = tag.getInt("x");
        int y = tag.getInt("y");
        int z = tag.getInt("z");
        return new BlockPos(x, y, z);
    }
//
//    public static class BreakEntry implements INBTSerializable<CompoundTag> {
//        BlockPos pos;
//        public static final String TAG_POS = "pos";
//        Block block;
//        public static final String TAG_BLOCK = "block";
//
//        @Override
//        public CompoundTag serializeNBT() {
//            CompoundTag tag = new CompoundTag();
//            CompoundTag temp = new CompoundTag();
//            temp.putInt("x", pos.getX());
//            temp.putInt("y", pos.getY());
//            temp.putInt("z", pos.getZ());
//            tag.put(TAG_POS, temp);
//            ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(block);
//            if (rl == null) {
//                LOGGER.warn("Wtf???");
//                rl = new ResourceLocation("air");
//            }
//            temp.putString(TAG_BLOCK, rl.toString());
//            return tag;
//        }
//
//        @Override
//        public void deserializeNBT(CompoundTag nbt) {
//            CompoundTag temp = nbt.getCompound(TAG_POS);
//            int x = temp.getInt("x");
//            int y = temp.getInt("y");
//            int z = temp.getInt("z");
//            this.pos = new BlockPos(x, y, z);
//            this.block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString(TAG_BLOCK)));
//        }
//    }
}

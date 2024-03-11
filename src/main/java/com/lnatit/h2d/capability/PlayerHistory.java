package com.lnatit.h2d.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mojang.text2speech.Narrator.LOGGER;

public class PlayerHistory implements IBreakHistory, INBTSerializable<CompoundTag> {
    private int digCount = 0;
    public static final String TAG_COUNT = "dig_count";
    private int digCooling = 0;
    public static final String TAG_COOLING = "dig_cooling";
    private BlockPos lastDigPos;
    public static final String TAG_POS = "last_pos";
    // TODO add dim info
//    private ResourceLocation lastDigLevel;
    public static final int MIN_COUNT = -16;
    public static final int MAX_COUNT = 4;
    public static final int MAX_COOLING = 1000;
    public static final double POS_DISTANCE = 10.0;


    @Override
    public IBreakHistory update(Level level, BlockPos pos) {
        if (this.lastDigPos == null || this.lastDigPos.closerThan(pos, POS_DISTANCE)) {
            this.digCount++;
        } else if (!this.lastDigPos.equals(pos)) {
            this.digCount = MIN_COUNT;
        }
        this.digCooling = MAX_COOLING;

        this.lastDigPos = pos;
        // TODO impl
//        this.lastDigLevel = level.dimension()
        LOGGER.info("Current count: " + digCount);
        LOGGER.info("Current cooling: " + digCooling);
        LOGGER.info("Current pos: " + lastDigPos.toString());
        return this;
    }

    @Override
    public float getSpeed(float original, float minimum) {
        if (this.digCount > 0) {
            float p = (float) digCount / MAX_COUNT;
            return Mth.lerp(p, minimum, original);
        }
        return original;
    }

    @Override
    public void tick() {
        if (this.digCooling > 0) {
            this.digCooling--;
        } else {
            this.digCooling = 0;
            if (this.digCount > MIN_COUNT)
                this.digCount--;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(TAG_COUNT, digCount);
        tag.putInt(TAG_COOLING, digCooling);
        if (this.lastDigPos != null)
            tag.put(TAG_POS, from(this.lastDigPos));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
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

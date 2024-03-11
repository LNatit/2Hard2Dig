package com.lnatit.h2d.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Queue;

import static com.mojang.text2speech.Narrator.LOGGER;

public class PlayerContext implements IBreakContext, INBTSerializable<CompoundTag>
{


    @Override
    public CompoundTag serializeNBT()
    {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {

    }

    public static class BreakEntry implements INBTSerializable<CompoundTag>
    {
        BlockPos pos;
        public static final String TAG_POS = "pos";
        Block block;
        public static final String TAG_BLOCK = "block";

        @Override
        public CompoundTag serializeNBT()
        {
            CompoundTag tag = new CompoundTag();
            CompoundTag temp = new CompoundTag();
            temp.putInt("x", pos.getX());
            temp.putInt("y", pos.getY());
            temp.putInt("z", pos.getZ());
            tag.put(TAG_POS, temp);
            ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(block);
            if (rl == null)
            {
                LOGGER.warn("Wtf???");
                rl = new ResourceLocation("air");
            }
            temp.putString(TAG_BLOCK, rl.toString());
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt)
        {
            CompoundTag temp = nbt.getCompound(TAG_POS);
            int x = temp.getInt("x");
            int y = temp.getInt("y");
            int z = temp.getInt("z");
            this.pos = new BlockPos(x, y, z);
            this.block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString(TAG_BLOCK)));
        }
    }
}

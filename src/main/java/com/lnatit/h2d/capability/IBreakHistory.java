package com.lnatit.h2d.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IBreakHistory
{
    IBreakHistory update(Level level, BlockPos pos);
    float getSpeed(float original, float minimum);
    void tick();
}

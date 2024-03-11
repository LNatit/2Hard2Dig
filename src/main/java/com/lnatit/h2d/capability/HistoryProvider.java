package com.lnatit.h2d.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HistoryProvider extends CapabilityProvider<HistoryProvider> implements ICapabilitySerializable<CompoundTag>
{
    public static Capability<IBreakHistory> HISTORY = CapabilityManager.get(new CapabilityToken<>()
    {
    });

    LazyOptional<PlayerHistory> handler = LazyOptional.of(PlayerHistory::new);

    protected HistoryProvider()
    {
        super(HistoryProvider.class);
    }

    @Override
    public CompoundTag serializeNBT()
    {
        return handler.resolve().get().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        handler.ifPresent(cap -> cap.deserializeNBT(nbt));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return HISTORY.orEmpty(cap, handler.cast());
    }
}

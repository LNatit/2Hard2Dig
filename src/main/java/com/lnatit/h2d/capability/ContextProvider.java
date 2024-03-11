package com.lnatit.h2d.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

public class ContextProvider extends CapabilityProvider<ContextProvider> implements ICapabilitySerializable<CompoundTag>
{
    public static Capability<IBreakContext> CONTEXT = CapabilityManager.get(new CapabilityToken<>()
    {
    });

    LazyOptional<PlayerContext> handler = LazyOptional.of(PlayerContext::new);

    protected ContextProvider()
    {
        super(ContextProvider.class);
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
}

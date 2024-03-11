package com.lnatit.h2d.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lnatit.h2d.Hard2Dig.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityAttachment
{
    @SubscribeEvent()
    public static void onAttachToPlayer(AttachCapabilitiesEvent<Entity> evt)
    {
        if (evt.getObject() instanceof Player)
            evt.addCapability(new ResourceLocation(MOD_ID, "break_history"), new HistoryProvider());
    }
}

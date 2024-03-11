package com.lnatit.h2d;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static com.lnatit.h2d.Hard2Dig.MOD_ID;

@Mod(MOD_ID)
public class Hard2Dig
{
    public static final String MOD_ID = "h2d";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Hard2Dig()
    {
        MinecraftForge.EVENT_BUS.addListener(Hard2Dig::onCalcBreakSpeed);
    }

    public static void onCalcBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (event.getPosition().isEmpty())
            return;

        int y = event.getPosition().get().getY();
        if (y > 64)
            return;

        float speed = Math.min(event.getOriginalSpeed(), event.getNewSpeed());
        event.setNewSpeed(speed / (1 << ((64 - y) >> 5)));
    }
}

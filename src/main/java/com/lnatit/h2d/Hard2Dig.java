package com.lnatit.h2d;

import com.lnatit.h2d.capability.HistoryProvider;
import com.lnatit.h2d.capability.IBreakHistory;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static com.lnatit.h2d.Hard2Dig.MOD_ID;

@Mod(MOD_ID)
public class Hard2Dig {
    public static final String MOD_ID = "h2d";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Hard2Dig() {
        MinecraftForge.EVENT_BUS.addListener(Hard2Dig::onPlayerTick);
        MinecraftForge.EVENT_BUS.addListener(Hard2Dig::onCalcBreakSpeed);
    }

    public static void onCalcBreakSpeed(PlayerEvent.BreakSpeed event) {
        var pPos = event.getPosition();
        if (pPos.isEmpty())
            return;

        Player player = event.getEntity();
        Level level = player.level();
        BlockPos pos = pPos.get();
        BlockState state = level.getBlockState(pos);
        // Bypass Ores
        if (state.is(Tags.Blocks.ORES))
            return;
        // Only take stones into count
        else if (!state.is(Tags.Blocks.STONE))
            return;

        // Get cap
        IBreakHistory history = player.getCapability(HistoryProvider.HISTORY)
                .orElseThrow(() -> new RuntimeException("Failed to get Capability: [h2d:break_history]!!!"));

        int y = pos.getY();
        // TODO get from level:biome
        int yMax = 64;
        if (y > yMax)
            return;

        float speed = Math.min(event.getOriginalSpeed(), event.getNewSpeed());
        float minSpeedAtCurrentDepth =
                getMinDigSpeed(y,
                        level.getMinBuildHeight(),
                        yMax,
                        speed,
                        // TODO relate to config settings
                        0.0625f
                );
        float newSpeed = history.update(level, pos)
                .getSpeed(speed, minSpeedAtCurrentDepth);
        event.setNewSpeed(newSpeed);
    }

    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        event.player.getCapability(HistoryProvider.HISTORY).ifPresent(IBreakHistory::tick);
    }


    public static float getMinDigSpeed(int y, int yMin, int yMax, float maxSpeed, float speedOnBedrock)
    {
        return Mth.lerp((float) (y - yMin) / (yMax - yMin), speedOnBedrock, maxSpeed);
    }
}

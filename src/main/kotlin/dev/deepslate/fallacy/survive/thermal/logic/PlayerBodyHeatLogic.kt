package dev.deepslate.fallacy.survive.thermal.logic

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.thermal.entity.HumanBodyHeat
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.PlayerTickEvent

@EventBusSubscriber(modid = TheMod.ID)
object PlayerBodyHeatLogic {
    @SubscribeEvent
    fun onServerPlayerTick(event: PlayerTickEvent.Post) {
        if (!TickCollector.checkServerTickInterval(HumanBodyHeat.TICK_INTERVAL)) return

        val player = event.entity as? ServerPlayer ?: return
        val cap = player.getCapability(ModCapabilities.BODY_HEAT)!!
        cap.tick()
//        TheMod.LOGGER.debug(
//            "{}: {}, env: {}",
//            player.name,
//            cap.heat,
//            ThermodynamicsEngine.getHeat(player.level(), player.blockPosition())
//        )
    }
}
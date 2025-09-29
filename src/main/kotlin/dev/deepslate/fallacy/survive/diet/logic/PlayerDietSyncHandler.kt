package dev.deepslate.fallacy.survive.diet.logic

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.entity.LivingEntityDiet
import dev.deepslate.fallacy.utils.seconds2Ticks
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.PlayerTickEvent

//@EventBusSubscriber(modid = TheMod.ID)
//object PlayerDietSyncHandler {
//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    fun onPlayerTickLast(event: PlayerTickEvent.Post) {
//        if (!TickCollector.checkServerTickInterval(seconds2Ticks(20))) return
//
//        val player = event.entity as? ServerPlayer ?: return
//        val diet = player.getCapability(ModCapabilities.DIET) as? LivingEntityDiet ?: return
//
//        if (diet.changed) diet.synchronize()
//    }
//}
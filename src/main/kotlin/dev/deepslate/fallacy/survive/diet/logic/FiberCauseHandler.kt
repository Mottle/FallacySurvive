package dev.deepslate.fallacy.survive.diet.logic

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.survive.ModAttributes
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.ModNutritionTypes
import dev.deepslate.fallacy.survive.diet.entity.cause
import dev.deepslate.fallacy.survive.diet.entity.contains
import dev.deepslate.fallacy.survive.diet.logic.FatCauseHandler.COLD_HEAT_DET
import dev.deepslate.fallacy.survive.effect.ModEffects
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import dev.deepslate.fallacy.utils.checkInvulnerable
import dev.deepslate.fallacy.utils.seconds2Ticks
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.PlayerTickEvent

@EventBusSubscriber(modid = TheMod.ID)
object FiberCauseHandler {

    const val HOT_HEAT_DET = 20f

    @SubscribeEvent
    fun onServerPlayerTick(event: PlayerTickEvent.Post) {
        if (!TickCollector.checkServerTickInterval(seconds2Ticks(10))) return
        if (event.entity == null || event.entity !is ServerPlayer) return

        val player = event.entity as ServerPlayer
        val diet = player.getCapability(ModCapabilities.DIET)!!

        if (checkInvulnerable(player)) return
        if (!diet.contains(ModNutritionTypes.FIBER)) return

        val fiber = diet.nutrition[ModNutritionTypes.FIBER]

        if (fiber <= 20f) {
            val effect = MobEffectInstance(ModEffects.LOW_FIBER, seconds2Ticks(20))
            player.addEffect(effect)
        }

        val envHeat = ThermodynamicsEngine.getHeat(player.level(), player.blockPosition())
        val defaultBodyHeat = player.getAttributeValue(ModAttributes.DEFAULT_BODY_HEAT)
        val det = envHeat.toDouble() - defaultBodyHeat

        if (det > HOT_HEAT_DET) {
            val rate = det / COLD_HEAT_DET
            val cause = rate * (1f / 6f)
            diet.cause(ModNutritionTypes.FAT, cause.toFloat(), player)
        } else {
            diet.cause(ModNutritionTypes.FAT, 1f / 24f, player)
        }
    }
}
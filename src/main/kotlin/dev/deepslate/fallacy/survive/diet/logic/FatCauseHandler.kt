package dev.deepslate.fallacy.survive.diet.logic

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.survive.ModAttributes
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.ModNutritionTypes
import dev.deepslate.fallacy.survive.diet.entity.cause
import dev.deepslate.fallacy.survive.diet.entity.contains
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
object FatCauseHandler {
    const val COLD_HEAT_DET = 20f

    @SubscribeEvent
    fun onServerPlayerTick(event: PlayerTickEvent.Post) {
        if (!TickCollector.checkServerTickInterval(seconds2Ticks(10))) return
        val player = event.entity as? ServerPlayer ?: return
        val diet = player.getCapability(ModCapabilities.DIET)!!

        if (checkInvulnerable(player)) return
        if (!diet.contains(ModNutritionTypes.FAT)) return

        val fat = diet.nutrition[ModNutritionTypes.FAT]

        if (fat <= 20f) {
            val effect = MobEffectInstance(ModEffects.LOW_FAT, seconds2Ticks(20))
            player.addEffect(effect)
        }

        val envHeat = ThermodynamicsEngine.getHeat(player.level(), player.blockPosition()).toDouble()
        val defaultBodyHeat = player.getAttributeValue(ModAttributes.DEFAULT_BODY_HEAT)
        val det = defaultBodyHeat - envHeat

        if (det > COLD_HEAT_DET) {
            val rate = det / COLD_HEAT_DET
            val cause = rate * (1f / 6f)
            diet.cause(ModNutritionTypes.FAT, cause.toFloat(), player)
        } else {
            diet.cause(ModNutritionTypes.FAT, 1f / 24f, player)
        }
    }
}
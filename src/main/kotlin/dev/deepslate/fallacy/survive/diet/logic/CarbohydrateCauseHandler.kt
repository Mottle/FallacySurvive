package dev.deepslate.fallacy.survive.diet.logic

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.ModNutritionTypes
import dev.deepslate.fallacy.survive.diet.entity.cause
import dev.deepslate.fallacy.survive.diet.entity.contains
import dev.deepslate.fallacy.survive.effect.ModEffects
import dev.deepslate.fallacy.utils.checkInvulnerable
import dev.deepslate.fallacy.utils.seconds2Ticks
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.PlayerTickEvent

@EventBusSubscriber(modid = TheMod.ID)
object CarbohydrateCauseHandler {

    fun createLowCarbohydrateDebuffs() = MobEffectInstance(ModEffects.LOW_CARBOHYDRATE, seconds2Ticks(20))

    @SubscribeEvent
    fun onServerPlayerTick(event: PlayerTickEvent.Post) {
        if (!TickCollector.checkServerTickInterval(seconds2Ticks(10))) return

        val player = event.entity as? ServerPlayer ?: return
        val diet = player.getCapability(ModCapabilities.DIET)!!

        if (checkInvulnerable(player)) return
        if (!diet.contains(ModNutritionTypes.CARBOHYDRATE)) return
        if (diet.nutrition[ModNutritionTypes.CARBOHYDRATE] <= 30f) {
            val effectInstances = createLowCarbohydrateDebuffs()
            player.addEffect(effectInstances)
        }

        diet.cause(ModNutritionTypes.CARBOHYDRATE, 1f / 6f, player)
    }
}
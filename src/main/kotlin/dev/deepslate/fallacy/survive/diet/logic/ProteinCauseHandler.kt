package dev.deepslate.fallacy.survive.diet.logic

import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.ModNutritionTypes
import dev.deepslate.fallacy.survive.diet.entity.cause
import dev.deepslate.fallacy.survive.effect.ModEffects
import dev.deepslate.fallacy.utils.checkInvulnerable
import dev.deepslate.fallacy.utils.seconds2Ticks
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent

@EventBusSubscriber(modid = TheMod.ID)
object ProteinCauseHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onPlayerDamage(event: LivingDamageEvent.Post) {
        val player = event.entity as? ServerPlayer ?: return
        val diet = player.getCapability(ModCapabilities.DIET)!!
        val nutritionState = diet.nutrition

        if (checkInvulnerable(player)) return
        if (ModNutritionTypes.PROTEIN !in nutritionState) return

        val protein = diet.nutrition[ModNutritionTypes.PROTEIN]

        if (protein <= 20f) {
            val effect = MobEffectInstance(ModEffects.LOW_PROTEIN, seconds2Ticks(20))
            player.addEffect(effect)
        }

//        val maxProtein = diet.nutrition[ModNutritionTypes.PROTEIN]
        val finalDamage = event.newDamage
        val cause = (finalDamage / 20f).coerceIn(0f, 0.5f)

        diet.cause(ModNutritionTypes.PROTEIN, cause, player)
    }
}
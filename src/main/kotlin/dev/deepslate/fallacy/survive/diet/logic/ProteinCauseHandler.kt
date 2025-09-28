package dev.deepslate.fallacy.survive.diet.logic

import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.ModNutritionTypes
import dev.deepslate.fallacy.survive.diet.entity.cause
import dev.deepslate.fallacy.utils.checkInvulnerable
import net.minecraft.server.level.ServerPlayer
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

//        val maxProtein = diet.nutrition[ModNutritionTypes.PROTEIN]
        val finalDamage = event.newDamage
        val cause = finalDamage.coerceIn(0f, 1f)

        diet.cause(ModNutritionTypes.PROTEIN, cause, player)
    }
}
package dev.deepslate.fallacy.survive.foodrework

import dev.deepslate.fallacy.survive.TheMod
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent

//抵消伤害导致的饱食度下降
@EventBusSubscriber(modid = TheMod.ID)
object PlayerHurtHandler {

    @SubscribeEvent
    fun onPostDamage(event: LivingDamageEvent.Post) {
        if (event.entity !is Player) return

        val player = event.entity as Player
        val damage = event.source

        player.foodData.addExhaustion(-damage.foodExhaustion)
    }
}
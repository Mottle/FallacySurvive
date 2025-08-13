package dev.deepslate.fallacy.survive.diet.entity

import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.capabilities.ICapabilityProvider

class PlayerDietProvider : ICapabilityProvider<Player, Void?, Diet<*>> {
    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun getCapability(`object`: Player, context: Void?): Diet<Player> = LivingEntityDiet(`object`)
}
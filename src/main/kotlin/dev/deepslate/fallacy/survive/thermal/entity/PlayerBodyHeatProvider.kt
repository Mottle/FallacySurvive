package dev.deepslate.fallacy.survive.thermal.entity

import dev.deepslate.fallacy.survive.thermal.HeatSensitive
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.capabilities.ICapabilityProvider

object PlayerBodyHeatProvider : ICapabilityProvider<Player, Void?, HeatSensitive> {
    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun getCapability(player: Player, context: Void?): HeatSensitive {
        for (d in distributors) {
            val cap = d(player)
            if (cap != null) return cap
        }

        throw IllegalStateException("No BodyHeat capability found for player $player")
    }

    private val distributors =
        mutableListOf<Distributor>(Distributor { player: Player -> HumanBodyHeat(player) })

    fun addDistributor(distributor: Distributor) {
        distributors.addFirst(distributor)
    }

    fun interface Distributor {
        operator fun invoke(player: Player): HeatSensitive?
    }
}
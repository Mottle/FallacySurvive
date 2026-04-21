package dev.deepslate.fallacy.survive.network.handler

import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.hydration.logic.DrinkHelper
import dev.deepslate.fallacy.survive.network.packet.DrinkInWorldPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.neoforged.neoforge.network.handling.IPayloadContext

object DrinkInWorldHandler {
    @JvmStatic
    fun handle(data: DrinkInWorldPacket, context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player()
            val serverPlayer = player as? ServerPlayer

            if (serverPlayer == null) {
                TheMod.LOGGER.warn("Ignored DrinkInWorldPacket: expected ServerPlayer, got {}", player::class.java.name)
                return@enqueueWork
            }

            val result = DrinkHelper.attemptDrink(serverPlayer.level(), serverPlayer)
            if (result.shouldSwing()) serverPlayer.swing(InteractionHand.MAIN_HAND, true)
        }
    }
}

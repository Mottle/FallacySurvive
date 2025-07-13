package dev.deepslate.fallacy.survive.network.handler

import dev.deepslate.fallacy.survive.hydration.logic.DrinkHelper
import dev.deepslate.fallacy.survive.network.packet.DrinkInWorldPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.neoforged.neoforge.network.handling.IPayloadContext

object DrinkInWorldHandler {
    @JvmStatic
    fun handle(data: DrinkInWorldPacket, context: IPayloadContext) {
        val player = context.player() as ServerPlayer
        val result = DrinkHelper.attemptDrink(player.level(), player)
        if (result.shouldSwing()) player.swing(InteractionHand.MAIN_HAND, true)
    }
}
package dev.deepslate.fallacy.survive.network.handler

import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.network.packet.FoodHistorySyncPacket
import net.neoforged.neoforge.network.handling.IPayloadContext

//client
object FoodHistorySyncHandler {
    @JvmStatic
    fun handle(data: FoodHistorySyncPacket, context: IPayloadContext) {
        context.player().setData(ModAttachments.FOOD_HISTORY, data.history)
        TheMod.LOGGER.info("Syncing food history")
    }
}
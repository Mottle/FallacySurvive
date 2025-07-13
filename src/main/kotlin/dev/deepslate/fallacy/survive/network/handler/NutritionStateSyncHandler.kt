package dev.deepslate.fallacy.survive.network.handler

import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.network.packet.NutritionStateSyncPacket
import net.neoforged.neoforge.network.handling.IPayloadContext

//client
object NutritionStateSyncHandler {
    @JvmStatic
    fun handle(data: NutritionStateSyncPacket, context: IPayloadContext) {
        val player = context.player()
        player.setData(ModAttachments.NUTRITION_STATE, data.state)
        TheMod.LOGGER.info("Syncing nutrition data")
    }
}
package dev.deepslate.fallacy.survive.network.handler

import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.network.packet.BodyHeatSyncPacket
import net.neoforged.neoforge.network.handling.IPayloadContext

//client
object BodyHeatSyncHandler {
    fun handle(data: BodyHeatSyncPacket, context: IPayloadContext) {
        val player = context.player()
        player.setData(ModAttachments.HEAT, data.heat)
        TheMod.LOGGER.info("Syncing body heat")
    }
}
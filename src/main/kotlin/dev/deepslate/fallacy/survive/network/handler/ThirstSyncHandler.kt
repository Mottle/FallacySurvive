package dev.deepslate.fallacy.survive.network.handler

import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.network.packet.ThirstSyncPacket
import net.neoforged.neoforge.network.handling.IPayloadContext

//Client
object ThirstSyncHandler {
    @JvmStatic
    fun handle(data: ThirstSyncPacket, context: IPayloadContext) {
        context.player().getCapability(ModCapabilities.THIRST)!!.value = data.value
        TheMod.LOGGER.info("Received ThirstSyncPacket with value: ${data.value}")
    }
}
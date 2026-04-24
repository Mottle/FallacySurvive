package dev.deepslate.fallacy.survive.network.handler

import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.network.packet.ThirstSyncPacket
import net.neoforged.neoforge.network.handling.IPayloadContext

//Client
object ThirstSyncHandler {
    @JvmStatic
    fun handle(data: ThirstSyncPacket, context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player()
            val cap = player.getCapability(ModCapabilities.THIRST)
            if (cap == null) {
                TheMod.LOGGER.warn(
                    "Ignored ThirstSyncPacket: THIRST capability missing for player {}",
                    player.scoreboardName
                )
                return@enqueueWork
            }

            cap.value = data.value
            TheMod.LOGGER.debug("Received ThirstSyncPacket with value: {}", data.value)
        }
    }
}

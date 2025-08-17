package dev.deepslate.fallacy.survive.network.handler

import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.network.packet.*
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

@EventBusSubscriber(modid = TheMod.ID)
object RegisterHandler {

    const val NETWORK_PROTOCOL_VERSION = "MONKEY"

    @SubscribeEvent
    fun onPayloadRegister(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar(NETWORK_PROTOCOL_VERSION)

        registrar.playToClient(
            ThirstSyncPacket.TYPE, ThirstSyncPacket.STREAM_CODEC,
            ThirstSyncHandler::handle
        )


        registrar.playToServer(
            DrinkInWorldPacket.TYPE,
            DrinkInWorldPacket.STREAM_CODEC,
            DrinkInWorldHandler::handle
        )

        registrar.playToClient(
            NutritionStateSyncPacket.TYPE,
            NutritionStateSyncPacket.STREAM_CODEC,
            NutritionStateSyncHandler::handle
        )

        registrar.playToClient(
            FoodHistorySyncPacket.TYPE,
            FoodHistorySyncPacket.STREAM_CODEC,
            FoodHistorySyncHandler::handle
        )

        registrar.playToClient(
            DisplayDietPacket.TYPE,
            DisplayDietPacket.STREAM_CODEC,
            DisplayDietHandler::handle
        )

        registrar.playToClient(
            BodyHeatSyncPacket.TYPE,
            BodyHeatSyncPacket.STREAM_CODEC,
            BodyHeatSyncHandler::handle
        )
    }
}
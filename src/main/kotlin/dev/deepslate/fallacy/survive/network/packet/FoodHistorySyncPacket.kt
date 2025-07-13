package dev.deepslate.fallacy.survive.network.packet

import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.entity.FoodHistory
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class FoodHistorySyncPacket(val history: FoodHistory) : CustomPacketPayload {

    companion object {
        @JvmStatic
        val TYPE = CustomPacketPayload.Type<FoodHistorySyncPacket>(TheMod.withID("food_history_sync_packet"))

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, FoodHistorySyncPacket> =
            StreamCodec.composite(FoodHistory.STREAM_CODEC, FoodHistorySyncPacket::history, ::FoodHistorySyncPacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
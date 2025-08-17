package dev.deepslate.fallacy.survive.network.packet

import dev.deepslate.fallacy.survive.TheMod
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class BodyHeatSyncPacket(val heat: Float) : CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<BodyHeatSyncPacket>(TheMod.withID("body_heat_sync_packet"))

        val STREAM_CODEC: StreamCodec<ByteBuf, BodyHeatSyncPacket> =
            StreamCodec.composite(ByteBufCodecs.FLOAT, BodyHeatSyncPacket::heat, ::BodyHeatSyncPacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
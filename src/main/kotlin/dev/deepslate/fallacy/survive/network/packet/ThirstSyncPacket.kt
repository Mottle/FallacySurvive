package dev.deepslate.fallacy.survive.network.packet

import dev.deepslate.fallacy.survive.TheMod
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class ThirstSyncPacket(val value: Float) : CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<ThirstSyncPacket>(TheMod.withID("thirst_sync_packet"))

        val STREAM_CODEC: StreamCodec<ByteBuf, ThirstSyncPacket> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ThirstSyncPacket::value, ::ThirstSyncPacket
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

}
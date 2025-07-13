package dev.deepslate.fallacy.survive.network.packet

import dev.deepslate.fallacy.survive.TheMod
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class DrinkInWorldPacket : CustomPacketPayload {

    companion object {
        @JvmStatic
        val TYPE = CustomPacketPayload.Type<DrinkInWorldPacket>(TheMod.withID("drink_in_world_packet"))

        @JvmStatic
        val PACKET = DrinkInWorldPacket()

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, DrinkInWorldPacket> =
            StreamCodec.unit<ByteBuf, DrinkInWorldPacket>(PACKET)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
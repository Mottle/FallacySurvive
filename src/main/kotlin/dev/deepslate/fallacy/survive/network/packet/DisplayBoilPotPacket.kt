package dev.deepslate.fallacy.survive.network.packet

import dev.deepslate.fallacy.survive.TheMod
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class DisplayBoilPotPacket(val pos: BlockPos) : CustomPacketPayload {

    companion object {
        @JvmStatic
        val TYPE = CustomPacketPayload.Type<DisplayBoilPotPacket>(TheMod.withID("display_boil_pot_packet"))

        @JvmStatic
        val STREAM_CODEC: StreamCodec<ByteBuf, DisplayBoilPotPacket> = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,
            { packet -> packet.pos.asLong() },
            { rawPos -> DisplayBoilPotPacket(BlockPos.of(rawPos)) }
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}

package dev.deepslate.fallacy.survive.network.handler

import dev.deepslate.fallacy.base.client.screen.component.ExtendedUIRender
import dev.deepslate.fallacy.survive.client.screen.BoilPotUI
import dev.deepslate.fallacy.survive.network.packet.DisplayBoilPotPacket
import net.minecraft.client.Minecraft
import net.neoforged.neoforge.network.handling.IPayloadContext

object DisplayBoilPotHandler {
    fun handle(data: DisplayBoilPotPacket, context: IPayloadContext) {
        context.enqueueWork {
            Minecraft.getInstance().setScreen(ExtendedUIRender(BoilPotUI(data.pos), isPause = false))
        }
    }
}

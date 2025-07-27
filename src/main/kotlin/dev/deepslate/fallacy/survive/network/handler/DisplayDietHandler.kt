package dev.deepslate.fallacy.survive.network.handler

import dev.deepslate.fallacy.base.client.screen.component.ExtendedUIRender
import dev.deepslate.fallacy.survive.client.screen.DietUI
import dev.deepslate.fallacy.survive.network.packet.DisplayDietPacket
import net.minecraft.client.Minecraft
import net.neoforged.neoforge.network.handling.IPayloadContext

//client side
object DisplayDietHandler {
    fun handle(data: DisplayDietPacket, context: IPayloadContext) {
        val state = data.nutrition
        val ui = DietUI().withExistedNutritionState(state)
        Minecraft.getInstance().setScreen(ExtendedUIRender(ui, isPause = false))
    }
}
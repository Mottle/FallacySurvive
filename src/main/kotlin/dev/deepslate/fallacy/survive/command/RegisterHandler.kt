package dev.deepslate.fallacy.survive.command

import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.utils.command.registerAll
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent

@EventBusSubscriber(modid = TheMod.ID)
object RegisterHandler {
    @SubscribeEvent
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        registerAll(event.dispatcher, commands)
    }

    @JvmStatic
    val commands = setOf(ThirstSet)
}
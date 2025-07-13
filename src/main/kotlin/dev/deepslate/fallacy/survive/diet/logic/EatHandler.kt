package dev.deepslate.fallacy.survive.diet.logic

import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import net.minecraft.client.Minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import kotlin.math.max

@EventBusSubscriber(modid = TheMod.ID)
object EatHandler {
    @SubscribeEvent
    fun onEatStart(event: LivingEntityUseItemEvent.Start) {
        val entity = event.entity as? Player ?: return
        val item = event.item

        if (!item.has(DataComponents.FOOD)) return

        val diet = entity.getCapability(ModCapabilities.DIET)!!
        val eatenCount = diet.history.countFood(item)
        val multiple = diet.getEatDurationMultiple(item)
        val fixedMultiple = max(1f, multiple)

//        with(entity as? ServerPlayer) {
//            val connection = this?.connection
//            when (eatenCount) {
//                in 0..2 -> {}
//                in 3..4 -> connection?.send(ClientboundSetSubtitleTextPacket(Component.translatable("msg.fallacy_survive.eat3_4")))
//                in 5..6 -> connection?.send(ClientboundSetSubtitleTextPacket(Component.translatable("msg.fallacy_survive.eat5_6")))
//                in 7..8 -> connection?.send(ClientboundSetSubtitleTextPacket(Component.translatable("msg.fallacy_survive.eat7_8")))
//                else -> connection?.send(ClientboundSetSubtitleTextPacket(Component.translatable("msg.fallacy_survive.eat_else")))
//            }
//        }

        if (entity.level().isClientSide) {
            val minecraft = Minecraft.getInstance()
            val gui = minecraft.gui
            when (eatenCount) {
                in 0..2 -> {}
                in 3..4 -> gui.setSubtitle(Component.translatable("msg.fallacy_survive.eat3_4"))
                in 5..6 -> gui.setSubtitle(Component.translatable("msg.fallacy_survive.eat5_6"))
                in 7..8 -> gui.setSubtitle(Component.translatable("msg.fallacy_survive.eat7_8"))
                else -> gui.setSubtitle(Component.translatable("msg.fallacy_survive.eat_else"))
            }
        }

        event.duration = (event.duration * fixedMultiple).toInt()
    }

    @SubscribeEvent
    fun onEatFinished(event: LivingEntityUseItemEvent.Finish) {
        val entity = event.entity
        val item = event.item

        if (entity !is Player) return
        if (!item.has(DataComponents.FOOD)) return

        val diet = entity.getCapability(ModCapabilities.DIET)!!
        diet.eat(item)
    }
}
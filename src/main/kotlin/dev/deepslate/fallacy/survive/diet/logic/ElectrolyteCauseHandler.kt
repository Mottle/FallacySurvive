package dev.deepslate.fallacy.survive.diet.logic

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.survive.ModCapabilities
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.ModNutritionTypes
import dev.deepslate.fallacy.survive.diet.entity.cause
import dev.deepslate.fallacy.survive.diet.entity.contains
import dev.deepslate.fallacy.survive.inject.recordmovement.MovementRecord
import dev.deepslate.fallacy.utils.seconds2Ticks
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingEvent
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import kotlin.math.absoluteValue

@EventBusSubscriber(modid = TheMod.ID)
object ElectrolyteCauseHandler {
    //记录玩家奔跑水平距离
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onServerPlayerSprint(event: PlayerTickEvent.Post) {
        val player = event.entity as? ServerPlayer ?: return

        if (!player.isSprinting) return

        val dis = (player.walkDist - player.walkDistO).absoluteValue
        val movementRecord = player as MovementRecord

        movementRecord.`fallacy$recordSprint`(movementRecord.`fallacy$getSprintDistance`() + dis)
    }

    @SubscribeEvent
    fun onServerPlayerTick(event: PlayerTickEvent.Post) {
        if (!TickCollector.checkServerTickInterval(seconds2Ticks(5))) return

        val player = event.entity as? ServerPlayer ?: return
        val movementRecord = player as MovementRecord
        val sprintDistance5s = movementRecord.`fallacy$getAndResetSprintDistance`()
        val diet = player.getCapability(ModCapabilities.DIET)!!

        if (!diet.contains(ModNutritionTypes.ELECTROLYTE)) return

        diet.cause(ModNutritionTypes.ELECTROLYTE, sprintDistance5s * 0.01f, player)
    }

    @SubscribeEvent
    fun onPlayerAttack(event: AttackEntityEvent) {
        val player = event.entity as? ServerPlayer ?: return
        val diet = player.getCapability(ModCapabilities.DIET)!!
        diet.cause(ModNutritionTypes.ELECTROLYTE, 0.08f, player)
    }

    @JvmStatic
    private val defaultGravity = 0.08

    @SubscribeEvent
    fun onPlayerJumping(event: LivingEvent.LivingJumpEvent) {
        val player = event.entity as? ServerPlayer ?: return
        val diet = player.getCapability(ModCapabilities.DIET)!!
        val playerGravity = player.getAttributeValue(Attributes.GRAVITY)

        if (playerGravity <= 0.01) return

        val cause = ((playerGravity / defaultGravity) * 0.1).toFloat()

        diet.cause(ModNutritionTypes.ELECTROLYTE, cause, player)
    }
}
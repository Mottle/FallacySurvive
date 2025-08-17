package dev.deepslate.fallacy.survive.thermal.entity

import dev.deepslate.fallacy.base.capability.Synchronous
import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.ModAttributes
import dev.deepslate.fallacy.survive.network.packet.BodyHeatSyncPacket
import dev.deepslate.fallacy.survive.thermal.HeatSensitive
import dev.deepslate.fallacy.survive.thermal.dh
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.absoluteValue
import kotlin.math.sign

class HumanBodyHeat(val player: Player) : HeatSensitive, Synchronous {

    companion object {
        const val TICK_INTERVAL = 30

        const val MIN_RELATIVE_HEAT = 20

        val COMFORTABLE_HEAT = ThermodynamicsEngine.fromFreezingPoint(25)
    }

    override var conductivity: Float
        get() = player.getAttribute(ModAttributes.CONDUCTIVITY)!!.value.toFloat()
        set(value) {
            player.getAttribute(ModAttributes.CONDUCTIVITY)!!.baseValue = value.toDouble()
        }

    override val k: Float = 2.7e-5f * 2

    override var heat: Float
        get() = player.getData(ModAttachments.HEAT)
        set(value) {
            val fixed = value.coerceIn(0f, ThermodynamicsEngine.MAX_HEAT.toFloat())
            player.setData(ModAttachments.HEAT, fixed)
        }

    init {
        if (!player.hasData(ModAttachments.HEAT)) {
            val defaultHeat = player.getAttribute(ModAttributes.DEFAULT_BODY_HEAT)!!.value
            player.setData(ModAttachments.HEAT, defaultHeat.toFloat())
        }
    }

    override fun synchronize() {
        if (player !is ServerPlayer) return
        PacketDistributor.sendToPlayer(player, BodyHeatSyncPacket(heat))
    }

    override fun tick() {
        if (player.isInvulnerable) return
        val blockPos = player.blockPosition()
        val localHeat = ThermodynamicsEngine.getHeat(player.level(), blockPos)

        if ((localHeat - COMFORTABLE_HEAT).absoluteValue * conductivity < MIN_RELATIVE_HEAT) {
            val defaultBodyHeat = player.getAttribute(ModAttributes.DEFAULT_BODY_HEAT)!!.value.toFloat()
            val det = defaultBodyHeat - heat
            if (det.absoluteValue < 0.2f) {
                heat = defaultBodyHeat
            } else {
                heat += 0.1f * det.sign
            }
            synchronize()
            return
        }

        val dh = dh(localHeat, tickIntervalFixConstant = TICK_INTERVAL)

        heat += dh
        synchronize()
    }
}
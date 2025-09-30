package dev.deepslate.fallacy.survive.thermal.entity

import dev.deepslate.fallacy.base.capability.Synchronous
import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.ModAttributes
import dev.deepslate.fallacy.survive.effect.ModEffects
import dev.deepslate.fallacy.survive.network.packet.BodyHeatSyncPacket
import dev.deepslate.fallacy.survive.thermal.HeatSensitive
import dev.deepslate.fallacy.survive.thermal.dh
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import dev.deepslate.fallacy.utils.checkInvulnerable
import dev.deepslate.fallacy.utils.seconds2Ticks
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.absoluteValue
import kotlin.math.sign

class HumanBodyHeat(val player: Player) : HeatSensitive, Synchronous {

    companion object {
        const val TICK_INTERVAL = 30

        const val MIN_RELATIVE_HEAT = 20

        @JvmStatic
        val COMFORTABLE_HEAT = ThermodynamicsEngine.fromFreezingPoint(25)
    }

    override var conductivity: Float
        get() = player.getAttribute(ModAttributes.CONDUCTIVITY)!!.value.toFloat()
        set(value) {
            player.getAttribute(ModAttributes.CONDUCTIVITY)!!.baseValue = value.toDouble()
        }

    override val k: Float = 2.7e-5f

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
        if (checkInvulnerable(player)) return

        val blockPos = player.blockPosition()
        val localHeat = ThermodynamicsEngine.getHeat(player.level(), blockPos)
        val defaultBodyHeat = player.getAttribute(ModAttributes.DEFAULT_BODY_HEAT)!!.value.toFloat()

        tickOverBodyHeat(defaultBodyHeat)

        val localComfortableDet = localHeat - COMFORTABLE_HEAT
        val relativeHeat =
            if ((localComfortableDet > 1 && player.hasEffect(ModEffects.LOW_FIBER))
                || (localComfortableDet < -1 && player.hasEffect(ModEffects.LOW_FAT))
            ) 0.5 * MIN_RELATIVE_HEAT
            else MIN_RELATIVE_HEAT

        //在舒适温度范围内
        if (localComfortableDet.absoluteValue * conductivity < relativeHeat.toFloat()) {
            val det = defaultBodyHeat - heat

            if (det.absoluteValue < 0.2f) {
                heat = defaultBodyHeat
            } else {
                heat += 0.25f * det.sign * (det.absoluteValue / 5f).coerceAtLeast(1f)
            }

            synchronize()
            return
        }

        val dh = dh(localHeat, tickIntervalFixConstant = TICK_INTERVAL)
        //温度回正修正
//        val fixed =
//            if (heat > defaultBodyHeat && localHeat < heat) 4f else if (heat < defaultBodyHeat && localHeat > heat) 4f else 1f

//        heat += dh * fixed
        heat += dh
        synchronize()
    }

    private fun tickOverBodyHeat(defaultBodyHeat: Float) {
        when (val detHeat = (defaultBodyHeat - heat).absoluteValue) {
            in 0f..2f -> {}
            in 2f..5f -> {
                val effect = MobEffectInstance(MobEffects.WEAKNESS, seconds2Ticks(10), 1)
                player.addEffect(effect)
            }

            else -> {
                val effect = MobEffectInstance(MobEffects.WEAKNESS, seconds2Ticks(10), 3)
                player.addEffect(effect)
                val source =
                    if (detHeat.sign > 0) player.level().damageSources().onFire() else player.level().damageSources()
                        .freeze()
                player.hurt(source, 2f)
            }
        }
    }
}
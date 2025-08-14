package dev.deepslate.fallacy.survive.thermal.entity

import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.ModAttributes
import dev.deepslate.fallacy.survive.thermal.HeatSensitive
import dev.deepslate.fallacy.survive.thermal.dh
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import net.minecraft.world.entity.LivingEntity
import kotlin.math.absoluteValue
import kotlin.math.sign

class HumanBodyHeat(val livingEntity: LivingEntity) : HeatSensitive {

    companion object {
        const val TICK_INTERVAL = 30

        const val MIN_RELATIVE_HEAT = 20

        val COMFORTABLE_HEAT = ThermodynamicsEngine.fromFreezingPoint(25)
    }

    override var conductivity: Float
        get() = livingEntity.getAttribute(ModAttributes.CONDUCTIVITY)!!.value.toFloat()
        set(value) {
            livingEntity.getAttribute(ModAttributes.CONDUCTIVITY)!!.baseValue = value.toDouble()
        }

    override val k: Float = 2.7e-5f * 2

    override var heat: Float
        get() = livingEntity.getData(ModAttachments.HEAT)
        set(value) {
            val fixed = value.coerceIn(0f, ThermodynamicsEngine.MAX_HEAT.toFloat())
            livingEntity.setData(ModAttachments.HEAT, fixed)
        }

    init {
        if (!livingEntity.hasData(ModAttachments.HEAT)) {
            val defaultHeat = livingEntity.getAttribute(ModAttributes.DEFAULT_BODY_HEAT)!!.value
            livingEntity.setData(ModAttachments.HEAT, defaultHeat.toFloat())
        }
    }

    override fun tick() {
        if (livingEntity.isInvulnerable) return
        val blockPos = livingEntity.blockPosition()
        val localHeat = ThermodynamicsEngine.getHeat(livingEntity.level(), blockPos)

        if ((localHeat - COMFORTABLE_HEAT).absoluteValue * conductivity < MIN_RELATIVE_HEAT) {
            val defaultBodyHeat = livingEntity.getAttribute(ModAttributes.DEFAULT_BODY_HEAT)!!.value.toFloat()
            val det = defaultBodyHeat - heat
            if (det.absoluteValue < 0.2f) {
                heat = defaultBodyHeat
            } else {
                heat += 0.1f * det.sign
            }
            return
        }

        val dh = dh(localHeat, tickIntervalFixConstant = TICK_INTERVAL)

        heat += dh
    }
}
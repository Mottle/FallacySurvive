package dev.deepslate.fallacy.survive.hydration.entity

import dev.deepslate.fallacy.base.TickCollector
import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.ModAttributes
import dev.deepslate.fallacy.survive.ModDamageTypes
import dev.deepslate.fallacy.survive.effect.ModEffects
import dev.deepslate.fallacy.survive.network.packet.ThirstSyncPacket
import dev.deepslate.fallacy.utils.checkInvulnerable
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.network.PacketDistributor
import java.lang.Math.clamp
import kotlin.math.min

class LivingEntityThirst(val entity: LivingEntity) : Thirst {
    companion object {
        private const val UPDATE_INTERVAL_TICKS = 20 * 60 * 2
    }

    private fun createEffect() = MobEffectInstance(ModEffects.DEHYDRATION, -1)

    override var value: Float
        get() = entity.getData(ModAttachments.THIRST)
        set(value) {
            entity.setData(ModAttachments.THIRST, value)
            if (entity is ServerPlayer) {
                PacketDistributor.sendToPlayer(entity, ThirstSyncPacket(this@LivingEntityThirst.value))
            }
        }

    override val max: Float
        get() = entity.getAttributeValue(ModAttributes.MAX_THIRST).toFloat()

    override fun drink(value: Float) {
        this.value = min(max, this.value + value)
    }

    private fun loss() = 1f

    override fun tick() {
        if (checkInvulnerable(entity)) return

//        val ticks = player.getData(FallacyAttachments.THIRST_TICKS)
        //TODO use per entity tick
        val tick = TickCollector.serverTickCount // entity.tickCount

        if (tick % UPDATE_INTERVAL_TICKS == 0) {
            value = clamp(value - loss(), 0f, max)
        }

        if (tick % 20 == 0 && value <= 0f) {
            val damage = damage(entity)
            entity.hurt(damage, 2f)
            if (!entity.hasEffect(ModEffects.DEHYDRATION)) entity.addEffect(createEffect())
        }

//        player.setData(FallacyAttachments.THIRST_TICKS, ticks + 1)
    }

    private fun damage(entity: LivingEntity): DamageSource =
        entity.level().damageSources().source(ModDamageTypes.DEHYDRATION)
}
package dev.deepslate.fallacy.survive.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory

class Dehydration : MobEffect(MobEffectCategory.HARMFUL, 0xffffff) {
//    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean = duration % 20 == 0
//
//    override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
//        val cap = livingEntity.getCapability(FallacyCapabilities.THIRST) ?: return false
//
//        //口渴值大于0f则消失
//        if (cap.value > 0f) return false
//        return true
//    }
}
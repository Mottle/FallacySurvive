package dev.deepslate.fallacy.survive.hydration.entity

import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.capabilities.ICapabilityProvider

class LivingEntityThirstProvider : ICapabilityProvider<LivingEntity, Void?, Thirst> {
    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun getCapability(`object`: LivingEntity, context: Void?): Thirst? = LivingEntityThirst(`object`)
}
package dev.deepslate.fallacy.survive.effect

import dev.deepslate.fallacy.survive.TheMod
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object ModEffects {
    @JvmStatic
    private val registry = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, TheMod.ID)

    @JvmStatic
    val DEHYDRATION: DeferredHolder<MobEffect, MobEffect> = registry.register("dehydration") { _ ->
        val id = TheMod.withID("effect.dehydration")
        Dehydration().addAttributeModifier(
            Attributes.ATTACK_SPEED, id, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ).addAttributeModifier(
            Attributes.MOVEMENT_SPEED, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        )
    }

    @JvmStatic
    val FULL: DeferredHolder<MobEffect, Full> = registry.register("full", ::Full)

    @EventBusSubscriber(modid = TheMod.ID)
    object RegisterHandler {
        @SubscribeEvent
        fun onModLoadComplete(event: FMLConstructModEvent) {
            event.enqueueWork {
                registry.register(MOD_BUS)
            }
        }
    }
}
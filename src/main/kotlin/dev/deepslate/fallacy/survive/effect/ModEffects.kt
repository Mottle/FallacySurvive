package dev.deepslate.fallacy.survive.effect

import dev.deepslate.fallacy.base.effect.GenericBeneficialEffect
import dev.deepslate.fallacy.base.effect.GenericHarmfulEffect
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
        GenericHarmfulEffect(0xffffff)
            .addAttributeModifier(Attributes.ATTACK_SPEED, id, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    }

    @JvmStatic
    val FULL: DeferredHolder<MobEffect, MobEffect> = registry.register("full", GenericBeneficialEffect.of(16262179))

    @JvmStatic
    val LOW_CARBOHYDRATE: DeferredHolder<MobEffect, MobEffect> = registry.register("low_carbohydrate") { _ ->
        val id = TheMod.withID("effect.low_carbohydrate")
        GenericHarmfulEffect(16262179)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, id, -0.4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, id, -0.8, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(
                Attributes.MINING_EFFICIENCY,
                id,
                -0.5,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
//            .addAttributeModifier(Attributes.SCALE, id, -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    }

    @JvmStatic
    val LOW_PROTEIN: DeferredHolder<MobEffect, MobEffect> = registry.register("low_protein") { _ ->
        val id = TheMod.withID("effect.low_protein")
        GenericHarmfulEffect(16262179)
            .addAttributeModifier(Attributes.SCALE, id, -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(
                Attributes.ATTACK_KNOCKBACK,
                id,
                -1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
            .addAttributeModifier(
                Attributes.BLOCK_BREAK_SPEED,
                id,
                -0.25,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
            .addAttributeModifier(
                Attributes.KNOCKBACK_RESISTANCE,
                id,
                -0.2,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
    }

    @JvmStatic
    val LOW_ELECTROLYTE: DeferredHolder<MobEffect, MobEffect> = registry.register("low_electrolyte") { _ ->
        GenericHarmfulEffect(16262179)
    }

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
package dev.deepslate.fallacy.survive

import dev.deepslate.fallacy.survive.thermal.entity.HumanBodyHeat
import dev.deepslate.fallacy.thermal.ThermodynamicsEngine
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object ModAttributes {
    @JvmStatic
    private val registry = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, TheMod.ID)

    @JvmStatic
    val MAX_HUNGER: DeferredHolder<Attribute, Attribute> =
        registry.register("generic.max_hunger") { _ ->
            RangedAttribute("fallacy_survive.attribute.name.generic.max_hunger", 20.0, 10.0, 1024.0)
                .setSyncable(true)
        }

    @JvmStatic
    val MAX_THIRST: DeferredHolder<Attribute, Attribute> = registry.register("generic.max_thirst") { _ ->
        RangedAttribute("fallacy_survive.attribute.name.generic.max_thirst", 20.0, 10.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    val MAX_CARBOHYDRATE: DeferredHolder<Attribute, Attribute> =
        registry.register("generic.max_carbohydrate") { _ ->
            RangedAttribute("fallacy_survive.attribute.name.generic.max_carbohydrates", 100.0, 0.0, 1024.0)
                .setSyncable(true)
        }

    @JvmStatic
    val MAX_PROTEIN: DeferredHolder<Attribute, Attribute> = registry.register("generic.max_protein") { _ ->
        RangedAttribute("fallacy_survive.attribute.name.generic.max_protein", 100.0, 0.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    val MAX_FAT: DeferredHolder<Attribute, Attribute> = registry.register("generic.max_fat") { _ ->
        RangedAttribute("fallacy_survive.attribute.name.generic.max_fat", 100.0, 0.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    val MAX_FIBER: DeferredHolder<Attribute, Attribute> = registry.register("generic.max_fiber") { _ ->
        RangedAttribute("fallacy_survive.attribute.name.generic.max_fiber", 100.0, 0.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    val MAX_ELECTROLYTE: DeferredHolder<Attribute, Attribute> =
        registry.register("generic.max_electrolyte") { _ ->
            RangedAttribute("fallacy_survive.attribute.name.generic.max_electrolyte", 100.0, 0.0, 1024.0)
                .setSyncable(true)
        }


    //默认体温
    @JvmStatic
    val DEFAULT_BODY_HEAT: DeferredHolder<Attribute, Attribute> = registry.register("generic.default_body_heat") { _ ->
        RangedAttribute(
            "fallacy_survive.attribute.name.generic.default_body_heat",
            HumanBodyHeat.DEFAULT_BODY_HEAT.toDouble(),
            ThermodynamicsEngine.MIN_HEAT.toDouble(),
            ThermodynamicsEngine.MAX_HEAT.toDouble()
        ).setSyncable(true)
    }

    //适宜温度 - 用于温度计算
    @JvmStatic
    val COMFORTABLE_BODY_HEAT: DeferredHolder<Attribute, Attribute> =
        registry.register("generic.comfortable_body_heat") { _ ->
            RangedAttribute(
                "fallacy_survive.attribute.name.generic.comfortable_body_heat",
                HumanBodyHeat.COMFORTABLE_HEAT.toDouble(),
                ThermodynamicsEngine.MIN_HEAT.toDouble(),
                ThermodynamicsEngine.MAX_HEAT.toDouble()
            ).setSyncable(true)
        }

    @JvmStatic
    val CONDUCTIVITY: DeferredHolder<Attribute, Attribute> = registry.register("generic.conductivity") { _ ->
        RangedAttribute(
            "fallacy_survive.attribute.name.generic.conductivity",
            1.0,
            0.0,
            1024.0
        ).setSyncable(true)
    }

    @EventBusSubscriber(modid = TheMod.ID)
    object RegisterHandler {
        @SubscribeEvent
        fun onModLoadCompleted(event: FMLConstructModEvent) {
            event.enqueueWork {
                registry.register(MOD_BUS)
            }
        }

        @SubscribeEvent
        fun onAttributeModification(event: EntityAttributeModificationEvent) {
            event.add(EntityType.PLAYER, MAX_HUNGER)
            event.add(EntityType.PLAYER, MAX_THIRST)
            event.add(EntityType.PLAYER, MAX_CARBOHYDRATE)
            event.add(EntityType.PLAYER, MAX_PROTEIN)
            event.add(EntityType.PLAYER, MAX_FAT)
            event.add(EntityType.PLAYER, MAX_FIBER)
            event.add(EntityType.PLAYER, MAX_ELECTROLYTE)
            event.add(EntityType.PLAYER, DEFAULT_BODY_HEAT)
            event.add(EntityType.PLAYER, COMFORTABLE_BODY_HEAT)
            event.add(EntityType.PLAYER, CONDUCTIVITY)
        }
    }
}
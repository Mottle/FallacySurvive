package dev.deepslate.fallacy.survive

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
    val MAX_HUNGER: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.max_hunger") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_hunger", 20.0, 10.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    val MAX_THIRST: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.max_thirst") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_thirst", 20.0, 10.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    val MAX_CARBOHYDRATE: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.max_carbohydrates") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_carbohydrates", 100.0, 0.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    val MAX_PROTEIN: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.max_protein") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_protein", 100.0, 0.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    val MAX_FAT: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.max_fat") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_fat", 100.0, 0.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    val MAX_FIBER: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.max_fiber") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_fiber", 100.0, 0.0, 1024.0).setSyncable(true)
    }

    @JvmStatic
    val MAX_ELECTROLYTE: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.max_electrolyte") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_electrolyte", 100.0, 0.0, 1024.0).setSyncable(true)
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
        }
    }
}
package dev.deepslate.fallacy.survive

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
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

    @EventBusSubscriber(modid = TheMod.ID)
    object RegisterHandler {
        @SubscribeEvent
        fun onModLoadCompleted(event: FMLCommonSetupEvent) {
            event.enqueueWork {
                registry.register(MOD_BUS)
            }
        }
    }
}
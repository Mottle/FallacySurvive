package dev.deepslate.fallacy.survive

import dev.deepslate.fallacy.survive.diet.entity.Diet
import dev.deepslate.fallacy.survive.diet.entity.PlayerDietProvider
import dev.deepslate.fallacy.survive.hydration.entity.LivingEntityThirstProvider
import dev.deepslate.fallacy.survive.hydration.entity.Thirst
import net.minecraft.world.entity.EntityType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

object ModCapabilities {
    val THIRST: EntityCapability<Thirst, Void?> =
        EntityCapability.createVoid(TheMod.withID("thirst"), Thirst::class.java)

    val DIET: EntityCapability<Diet<*>, Void?> = EntityCapability.createVoid(TheMod.withID("diet"), Diet::class.java)

    @EventBusSubscriber(modid = TheMod.ID)
    object RegisterHandler {
        @SubscribeEvent
        fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {
            event.registerEntity(THIRST, EntityType.PLAYER, LivingEntityThirstProvider())
            event.registerEntity(DIET, EntityType.PLAYER, PlayerDietProvider())
        }
    }
}
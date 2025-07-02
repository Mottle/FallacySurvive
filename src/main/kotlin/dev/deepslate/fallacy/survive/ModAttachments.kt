package dev.deepslate.fallacy.survive

import com.mojang.serialization.Codec
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object ModAttachments {
    @JvmStatic
    private val registry = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TheMod.ID)

    @JvmStatic
    val THIRST: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> = registry.register("thirst") { _ ->
        AttachmentType.builder { _ -> 20f }.serialize(Codec.FLOAT).build()
    }

    //客户端和服务端各自维护一个LAST_DRINK_TICK，不进行同步
    @JvmStatic
    val LAST_DRINK_TICK_STAMP: DeferredHolder<AttachmentType<*>, AttachmentType<Int>> =
        registry.register("last_drink_tick_stamp") { _ -> AttachmentType.builder { _ -> -1 }.build() }

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
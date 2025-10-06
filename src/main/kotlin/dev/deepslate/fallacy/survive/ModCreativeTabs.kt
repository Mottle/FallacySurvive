package dev.deepslate.fallacy.survive

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object ModCreativeTabs {
    @JvmStatic
    private val registry: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, TheMod.ID)

    internal fun register(bus: IEventBus) {
        registry.register(MOD_BUS)
        //Registrate.defaultCreativeModeTab默认指向Search，之后在item注册时使用tab会导致重复注册，在此将其置null避免此问题
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        TheMod.REGISTRATE.defaultCreativeTab(null as (ResourceKey<CreativeModeTab>)?)
    }

    private fun simpleRegister(name: String, factory: (CreativeModeTab.Builder) -> CreativeModeTab) =
        registry.register(name) { _ -> factory(CreativeModeTab.builder()) }

    @JvmStatic
    val SURVIVE: DeferredHolder<CreativeModeTab, CreativeModeTab> = simpleRegister("survive") { builder ->
        builder.title(TheMod.REGISTRATE.addLang("itemGroup", TheMod.withID("survive"), "Survive"))
            .icon { Items.APPLE.defaultInstance }.build()
    }
}
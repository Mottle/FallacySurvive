package dev.deepslate.fallacy.survive

import com.github.wintersteve25.tau.menu.TauMenuHolder
import dev.deepslate.fallacy.survive.menu.BoilPotUI
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.flag.FeatureFlags
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object ModMenus {
    @JvmStatic
    private val registry = DeferredRegister.create(BuiltInRegistries.MENU, TheMod.ID)

    @JvmStatic
    val BOIL_POT_MENU = TauMenuHolder(registry, ::BoilPotUI, "boil_pot", FeatureFlags.DEFAULT_FLAGS)

    @EventBusSubscriber(modid = TheMod.ID)
    object RegisterHandler {
        @SubscribeEvent
        fun onModConstructed(event: FMLConstructModEvent) {
            event.enqueueWork {
                registry.register(MOD_BUS)
            }
        }
    }
}

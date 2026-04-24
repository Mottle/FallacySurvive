package dev.deepslate.fallacy.survive.menu

import com.github.wintersteve25.tau.menu.TauMenuHolder
import dev.deepslate.fallacy.survive.TheMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.flag.FeatureFlags
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object ModMenus {
    @JvmStatic
    private val MENU_REGISTRY = DeferredRegister.create(Registries.MENU, TheMod.ID)

    @JvmStatic
    val BOIL_POT = TauMenuHolder(MENU_REGISTRY, { BoilPotUI }, "boil_pot", FeatureFlagSet.of(FeatureFlags.VANILLA))

    @EventBusSubscriber(modid = TheMod.ID)
    object RegisterHandler {
        @SubscribeEvent
        fun onConstruct(event: FMLConstructModEvent) {
            event.enqueueWork {
                MENU_REGISTRY.register(MOD_BUS)
            }
        }
    }
}

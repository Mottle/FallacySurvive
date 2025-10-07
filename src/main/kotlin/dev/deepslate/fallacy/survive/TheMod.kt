package dev.deepslate.fallacy.survive

import com.tterrag.registrate.Registrate
import dev.deepslate.fallacy.survive.block.ModBlocks
import dev.deepslate.fallacy.survive.client.Lang
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.common.Mod
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(TheMod.ID)
object TheMod {
    infix fun withID(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(ID, name)

    const val ID = "fallacy_survive"

    @JvmStatic
    val LOGGER: Logger = LoggerFactory.getLogger(ID)

    @JvmStatic
    val REGISTRATE: Registrate = Registrate.create(ID)

    init {
//        ModCreativeTabs.register(MOD_BUS)
        ModCreativeTabs
        ModBlocks
        ModDamageTypes
        Lang
    }
}

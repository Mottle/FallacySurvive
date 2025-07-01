package dev.deepslate.fallacy.survive

import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.common.Mod
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Mod(TheMod.ID)
object TheMod {
    infix fun withID(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(ID, name)

    const val ID = "survive"

    val LOGGER: Logger = LoggerFactory.getLogger(ID)
}

package dev.deepslate.fallacy.survive.block

import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.survive.ModCreativeTabs
import dev.deepslate.fallacy.survive.TheMod

object ModBlocks {
    @JvmStatic
    val COPPER_BOIL_POT: BlockEntry<BoilPot> =
        TheMod.REGISTRATE.block("copper_boil_pot", ::BoilPot).blockstate { ctx, prov ->
            val model = prov.models().getExistingFile(TheMod.withID("block/copper_boil_pot"))
            prov.simpleBlock(ctx.entry, model)
        }
            .item().tab(ModCreativeTabs.SURVIVE.key!!).build()
            .register()
}
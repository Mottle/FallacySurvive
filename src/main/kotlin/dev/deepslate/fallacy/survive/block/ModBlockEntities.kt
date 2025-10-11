package dev.deepslate.fallacy.survive.block

import com.tterrag.registrate.util.entry.BlockEntityEntry
import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.block.entity.BoilPotEntity

object ModBlockEntities {
    val BOIL_POT: BlockEntityEntry<BoilPotEntity> =
        TheMod.REGISTRATE.blockEntity("boil_pot", ::BoilPotEntity).validBlocks(ModBlocks.COPPER_BOIL_POT).register()
}
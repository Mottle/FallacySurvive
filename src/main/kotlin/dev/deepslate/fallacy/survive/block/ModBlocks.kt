package dev.deepslate.fallacy.survive.block

import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.survive.ModCreativeTabs
import dev.deepslate.fallacy.survive.TheMod
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.MapColor

object ModBlocks {
    @JvmStatic
    val COPPER_BOIL_POT: BlockEntry<BoilPotBlock> = TheMod.REGISTRATE.block("copper_boil_pot", ::BoilPotBlock)
        .properties { p ->
            p.mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.COPPER)
                .noOcclusion()
        }
        .blockstate { ctx, prov ->
            prov.simpleBlock(ctx.get(), prov.models().getExistingFile(TheMod.withID("block/copper_boil_pot")))
        }
        .item().tab(ModCreativeTabs.SURVIVE.key!!).build().register()
}
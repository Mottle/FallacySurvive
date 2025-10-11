package dev.deepslate.fallacy.survive.block

import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.survive.ModCreativeTabs
import dev.deepslate.fallacy.survive.TheMod
import net.minecraft.client.color.block.BlockColor
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import java.util.function.Supplier

object ModBlocks {
    @JvmStatic
    val COPPER_BOIL_POT: BlockEntry<BoilPotBlock> = TheMod.REGISTRATE.block("copper_boil_pot", ::BoilPotBlock)
        .properties { p ->
            p.mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.COPPER)
                .noOcclusion()
        }
        .blockstate { ctx, prov ->
            prov.getVariantBuilder(ctx.entry).forAllStates { state ->
                val filled = state.getValue(BoilPotBlock.FILLED)
                val unfilledModel = prov.models().getExistingFile(TheMod.withID("block/copper_boil_pot"))
                val filledModel = prov.models().getExistingFile(TheMod.withID("block/filled_copper_boil_pot"))
                val model = if (filled) filledModel else unfilledModel
                return@forAllStates ConfiguredModel.builder().modelFile(model).build()
            }
        }.color {
            Supplier {
                BlockColor { blockState, getter, pos, tintIndex ->
                    val filled = blockState.getValue(BoilPotBlock.FILLED)
                    if (getter != null && pos != null && filled)
                        BiomeColors.getAverageWaterColor(getter, pos)
                    else -1
                }
            }
        }.item().tab(ModCreativeTabs.SURVIVE.key!!).build().register()
}
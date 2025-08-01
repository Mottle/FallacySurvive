package dev.deepslate.fallacy.survive.client

import dev.deepslate.fallacy.survive.diet.ModNutritionTypes
import dev.deepslate.fallacy.survive.diet.NutrientType
import dev.deepslate.fallacy.utils.RGB
import net.minecraft.core.Holder
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(value = Dist.CLIENT)
object NutrientTypeColor {
    @JvmStatic
    val DEFAULT = RGB.from(0xff4500)

    @JvmStatic
    private val colorMap = hashMapOf<NutrientType, RGB>()

    infix operator fun get(type: NutrientType) = colorMap[type] ?: DEFAULT

    infix operator fun get(typeHolder: Holder<NutrientType>) = get(typeHolder.value())

    operator fun set(type: NutrientType, color: RGB) {
        colorMap[type] = color
    }

    operator fun set(typeHolder: Holder<NutrientType>, color: RGB) = set(typeHolder.value(), color)

    operator fun set(type: NutrientType, color: Int) {
        colorMap[type] = RGB.from(color)
    }

    operator fun set(typeHolder: Holder<NutrientType>, color: Int) = set(typeHolder.value(), color)

    infix operator fun contains(type: NutrientType) = colorMap.containsKey(type)

    infix operator fun contains(typeHolder: Holder<NutrientType>) = contains(typeHolder.value())

    init {
        set(ModNutritionTypes.CARBOHYDRATE, 0xff4500)
        set(ModNutritionTypes.PROTEIN, 0xffa500)
        set(ModNutritionTypes.FAT, 0x8b7e66)
        set(ModNutritionTypes.FIBER, 0x32cd32)
        set(ModNutritionTypes.ELECTROLYTE, 0x00ced1)
    }
}
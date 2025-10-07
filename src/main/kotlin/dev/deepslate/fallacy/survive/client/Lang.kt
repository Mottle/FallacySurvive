package dev.deepslate.fallacy.survive.client

import dev.deepslate.fallacy.survive.TheMod
import net.minecraft.network.chat.Component

object Lang {
    @JvmStatic
    val DIET_TITLE: Component = TheMod.REGISTRATE.addRawLang("gui.fallacy_survive.diet_ui.title", "Diet")

    @JvmStatic
    val FOOD_EAT_TIMES: Component =
        TheMod.REGISTRATE.addRawLang("item.tooltips.food_eat_times", "You've eaten %s times out of the last %s")

    @JvmStatic
    val FOOD_NEVER_EAT: Component =
        TheMod.REGISTRATE.addRawLang("item.tooltips.food_never_eat", "Haven't eaten recently")

    @JvmStatic
    val FULL_LEVEL_0: Component = TheMod.REGISTRATE.addRawLang("item.tooltips.full_level0", "Negligible")

    @JvmStatic
    val FULL_LEVEL_1: Component = TheMod.REGISTRATE.addRawLang("item.tooltips.full_level1", "Snack")

    @JvmStatic
    val FULL_LEVEL_2: Component = TheMod.REGISTRATE.addRawLang("item.tooltips.full_level2", "Light meal")

    @JvmStatic
    val FULL_LEVEL_3: Component = TheMod.REGISTRATE.addRawLang("item.tooltips.full_level3", "Full meal")

    @JvmStatic
    val FULL_LEVEL_4: Component = TheMod.REGISTRATE.addRawLang("item.tooltips.full_level4", "Feast")

    @JvmStatic
    val EAT_3_4: Component =
        TheMod.REGISTRATE.addLang("msg", TheMod.withID("eat3_4"), "Not really feeling like eating anymore")

    @JvmStatic
    val EAT_5_6: Component =
        TheMod.REGISTRATE.addLang("msg", TheMod.withID("eat5_6"), "You find it bland and unappetizing")

    @JvmStatic
    val EAT_7_8: Component = TheMod.REGISTRATE.addLang("msg", TheMod.withID("eat7_8"), "You can barely stomach it")

    @JvmStatic
    val EAT_ELSE: Component = TheMod.REGISTRATE.addLang("msg", TheMod.withID("eat_else"), "Bleh")

}
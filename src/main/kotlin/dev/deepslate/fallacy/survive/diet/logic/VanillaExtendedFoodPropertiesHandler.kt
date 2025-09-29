package dev.deepslate.fallacy.survive.diet.logic

import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.ModNutritionTypes
import dev.deepslate.fallacy.survive.diet.NutrientContainer
import dev.deepslate.fallacy.survive.diet.item.ExtendedFoodProperties
import dev.deepslate.fallacy.survive.diet.item.FoodNutrition
import dev.deepslate.fallacy.survive.inject.itemfoodproperties.ItemFoodPropertiesExtension
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

@EventBusSubscriber(modid = TheMod.ID)
object VanillaExtendedFoodPropertiesHandler {

    private val defaultNutrition = mapOf(
        Items.ENCHANTED_GOLDEN_APPLE to VanillaNutrient(
            carbohydrate = 5f,
            fat = 5f,
            protein = 5f,
            fiber = 5f,
            electrolyte = 5f
        ),
        Items.GOLDEN_APPLE to VanillaNutrient(
            carbohydrate = 1f,
            fat = 1f,
            protein = 1f,
            fiber = 1f,
            electrolyte = 1f
        ),
        Items.GOLDEN_CARROT to VanillaNutrient(carbohydrate = 0.4f, fiber = 0.8f),
        Items.COOKED_BEEF to VanillaNutrient(fat = 0.4f, protein = 1.5f),
        Items.COOKED_PORKCHOP to VanillaNutrient(fat = 1f, protein = 0.8f),
        Items.COOKED_MUTTON to VanillaNutrient(fat = 0.8f, protein = 0.8f),
        Items.COOKED_SALMON to VanillaNutrient(fat = 0.1f, protein = 0.5f),
        Items.BAKED_POTATO to VanillaNutrient(carbohydrate = 1.5f, fat = 0.1f),
        Items.BEETROOT to VanillaNutrient(carbohydrate = 1.5f, fiber = 0.3f),
        Items.BEETROOT_SOUP to VanillaNutrient(carbohydrate = 1.5f),
        Items.BREAD to VanillaNutrient(carbohydrate = 0.8f, fiber = 0.1f),
        Items.CARROT to VanillaNutrient(carbohydrate = 0.4f, fiber = 0.8f),
        Items.COOKED_CHICKEN to VanillaNutrient(fat = 0.7f, protein = 0.8f),
        Items.COOKED_COD to VanillaNutrient(protein = 1f),
        Items.COOKED_RABBIT to VanillaNutrient(protein = 1f),
        Items.RABBIT_STEW to VanillaNutrient(protein = 0.8f),
        Items.APPLE to VanillaNutrient(carbohydrate = 0.6f, fiber = 0.8f, electrolyte = 0.1f),
        Items.CHORUS_FRUIT to VanillaNutrient(electrolyte = 0.6f, fiber = 1f),
        Items.DRIED_KELP to VanillaNutrient(carbohydrate = 0.4f, fiber = 0.4f, electrolyte = 1f),
        Items.MELON_SLICE to VanillaNutrient(carbohydrate = 0.8f, electrolyte = 0.8f),
        Items.POTATO to VanillaNutrient(carbohydrate = 1.2f, fat = 0.1f),
        Items.PUMPKIN_PIE to VanillaNutrient(carbohydrate = 0.6f, fat = 0.3f),
        Items.BEEF to VanillaNutrient(fat = 0.4f, protein = 1f),
        Items.CHICKEN to VanillaNutrient(fat = 0.5f, protein = 0.8f),
        Items.MUTTON to VanillaNutrient(fat = 0.8f, protein = 0.6f),
        Items.PORKCHOP to VanillaNutrient(fat = 0.8f, protein = 0.8f),
        Items.RABBIT to VanillaNutrient(protein = 0.8f),
        Items.SWEET_BERRIES to VanillaNutrient(carbohydrate = 0.2f, electrolyte = 0.2f),
        Items.GLOW_BERRIES to VanillaNutrient(carbohydrate = 0.1f, electrolyte = 0.5f),
        Items.CAKE to VanillaNutrient(carbohydrate = 1f, fat = 0.5f),
        Items.HONEY_BOTTLE to VanillaNutrient(carbohydrate = 2f),
        Items.PUFFERFISH to VanillaNutrient(protein = 2f),
        Items.COD to VanillaNutrient(protein = 1f),
        Items.SALMON to VanillaNutrient(fat = 0.1f, protein = 0.5f),
        Items.TROPICAL_FISH to VanillaNutrient(protein = 1f),
        Items.COOKIE to VanillaNutrient(carbohydrate = 1.2f, fat = 1f),
        Items.MUSHROOM_STEW to VanillaNutrient(carbohydrate = 1f, fat = 0.4f, fiber = 0.5f)
    )

    private val defaultFullLevel = mapOf(
        Items.PUMPKIN_PIE to 1,
        Items.GLOW_BERRIES to 1,
        Items.SWEET_BERRIES to 1,
        Items.COOKIE to 1,
        Items.GOLDEN_APPLE to 3,
        Items.ENCHANTED_GOLDEN_APPLE to 4,
        Items.DRIED_KELP to 1
    )

    //不要修改时序: FMLCommonSetupEvent
    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            (defaultNutrition.keys + defaultFullLevel.keys).map { item ->
                val nutrition = defaultNutrition[item]
                val fullLevel = defaultFullLevel[item]
                val foodData = ExtendedFoodProperties.Builder()

                if (nutrition != null) {
                    foodData.withNutrition(nutrition.toFoodNutrition())
                }

                if (fullLevel != null) {
                    foodData.withFullLevel(fullLevel)
                }

                (item as ItemFoodPropertiesExtension).`fallacy$internalSetExtendedFoodProperties`(foodData.build())
            }
        }
    }

    data class VanillaNutrient(
        val carbohydrate: Float = 0f,
        val protein: Float = 0f,
        val fat: Float = 0f,
        val fiber: Float = 0f,
        val electrolyte: Float = 0f
    ) {
        //临时翻倍
        fun toFoodNutrition() = mapOf(
            ModNutritionTypes.CARBOHYDRATE to carbohydrate * 2,
            ModNutritionTypes.PROTEIN to protein * 2,
            ModNutritionTypes.FAT to fat * 2,
            ModNutritionTypes.FIBER to fiber * 2,
            ModNutritionTypes.ELECTROLYTE to electrolyte * 2
        ).mapKeys { (key, _) -> key.value() }.let(::NutrientContainer).let(::FoodNutrition)
    }
} 
package dev.deepslate.fallacy.survive.diet.logic

import dev.deepslate.fallacy.survive.TheMod
import dev.deepslate.fallacy.survive.diet.ModNutritionTypes
import dev.deepslate.fallacy.survive.diet.NutritionContainer
import dev.deepslate.fallacy.survive.diet.item.ExtendedFoodProperties
import dev.deepslate.fallacy.survive.diet.item.FoodNutrition
import dev.deepslate.fallacy.survive.inject.ItemFoodPropertiesExtension
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent

@EventBusSubscriber(modid = TheMod.ID)
object VanillaExtendedFoodPropertiesHandler {

    private val defaultNutrition = mapOf(
        Items.ENCHANTED_GOLDEN_APPLE to VanillaNutrition(
            carbohydrate = 5f,
            fat = 5f,
            protein = 5f,
            fiber = 5f,
            electrolyte = 5f
        ),
        Items.GOLDEN_APPLE to VanillaNutrition(
            carbohydrate = 1f,
            fat = 1f,
            protein = 1f,
            fiber = 1f,
            electrolyte = 1f
        ),
        Items.GOLDEN_CARROT to VanillaNutrition(carbohydrate = 0.4f, fiber = 0.8f),
        Items.COOKED_BEEF to VanillaNutrition(fat = 0.4f, protein = 1.5f),
        Items.COOKED_PORKCHOP to VanillaNutrition(fat = 1f, protein = 0.8f),
        Items.COOKED_MUTTON to VanillaNutrition(fat = 0.8f, protein = 0.8f),
        Items.COOKED_SALMON to VanillaNutrition(fat = 0.1f, protein = 0.5f),
        Items.BAKED_POTATO to VanillaNutrition(carbohydrate = 1.5f, fat = 0.1f),
        Items.BEETROOT to VanillaNutrition(carbohydrate = 1.5f, fiber = 0.3f),
        Items.BEETROOT_SOUP to VanillaNutrition(carbohydrate = 1.5f),
        Items.BREAD to VanillaNutrition(carbohydrate = 0.8f, fiber = 0.1f),
        Items.CARROT to VanillaNutrition(carbohydrate = 0.4f, fiber = 0.8f),
        Items.COOKED_CHICKEN to VanillaNutrition(fat = 0.7f, protein = 0.8f),
        Items.COOKED_COD to VanillaNutrition(protein = 1f),
        Items.COOKED_RABBIT to VanillaNutrition(protein = 1f),
        Items.RABBIT_STEW to VanillaNutrition(protein = 0.8f),
        Items.APPLE to VanillaNutrition(carbohydrate = 0.6f, fiber = 0.8f, electrolyte = 0.1f),
        Items.CHORUS_FRUIT to VanillaNutrition(electrolyte = 0.6f, fiber = 1f),
        Items.DRIED_KELP to VanillaNutrition(carbohydrate = 0.4f, fiber = 0.4f, electrolyte = 1f),
        Items.MELON_SLICE to VanillaNutrition(carbohydrate = 0.8f, electrolyte = 0.8f),
        Items.POTATO to VanillaNutrition(carbohydrate = 1.2f, fat = 0.1f),
        Items.PUMPKIN_PIE to VanillaNutrition(carbohydrate = 0.6f, fat = 0.3f),
        Items.BEEF to VanillaNutrition(fat = 0.4f, protein = 1f),
        Items.CHICKEN to VanillaNutrition(fat = 0.5f, protein = 0.8f),
        Items.MUTTON to VanillaNutrition(fat = 0.8f, protein = 0.6f),
        Items.PORKCHOP to VanillaNutrition(fat = 0.8f, protein = 0.8f),
        Items.RABBIT to VanillaNutrition(protein = 0.8f),
        Items.SWEET_BERRIES to VanillaNutrition(carbohydrate = 0.2f, electrolyte = 0.2f),
        Items.GLOW_BERRIES to VanillaNutrition(carbohydrate = 0.1f, electrolyte = 0.5f),
        Items.CAKE to VanillaNutrition(carbohydrate = 1f, fat = 0.5f),
        Items.HONEY_BOTTLE to VanillaNutrition(carbohydrate = 2f),
        Items.PUFFERFISH to VanillaNutrition(protein = 2f),
        Items.COD to VanillaNutrition(protein = 1f),
        Items.SALMON to VanillaNutrition(fat = 0.1f, protein = 0.5f),
        Items.TROPICAL_FISH to VanillaNutrition(protein = 1f),
        Items.COOKIE to VanillaNutrition(carbohydrate = 1.2f, fat = 1f),
        Items.MUSHROOM_STEW to VanillaNutrition(carbohydrate = 1f, fat = 0.4f, fiber = 0.5f)
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

    data class VanillaNutrition(
        val carbohydrate: Float = 0f,
        val protein: Float = 0f,
        val fat: Float = 0f,
        val fiber: Float = 0f,
        val electrolyte: Float = 0f
    ) {
        fun toFoodNutrition() = mapOf(
            ModNutritionTypes.CARBOHYDRATE to carbohydrate,
            ModNutritionTypes.PROTEIN to protein,
            ModNutritionTypes.FAT to fat,
            ModNutritionTypes.FIBER to fiber,
            ModNutritionTypes.ELECTROLYTE to electrolyte
        ).mapKeys { (key, _) -> key.value() }.let(::NutritionContainer).let(::FoodNutrition)
    }
} 
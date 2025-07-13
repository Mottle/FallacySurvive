package dev.deepslate.fallacy.survive

import dev.deepslate.fallacy.survive.diet.item.ExtendedFoodProperties
import dev.deepslate.fallacy.survive.inject.ItemFoodPropertiesExtension
import net.minecraft.world.item.Item

internal var Item.internalExtendedFoodProperties: ExtendedFoodProperties?
    get() = (this as ItemFoodPropertiesExtension).`fallacy$getExtendedFoodProperties`()
    set(value) {
        (this as ItemFoodPropertiesExtension).`fallacy$internalSetExtendedFoodProperties`(value)
    }

val Item.extendedFoodProperties: ExtendedFoodProperties?
    get() = internalExtendedFoodProperties
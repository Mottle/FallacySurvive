package dev.deepslate.fallacy.survive.inject.itemfoodproperties;

import dev.deepslate.fallacy.survive.diet.item.ExtendedFoodProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface ItemFoodPropertiesExtension {
    @ApiStatus.Internal
    default void fallacy$internalSetExtendedFoodProperties(ExtendedFoodProperties data) {
        throw new UnsupportedOperationException("This should not happen");
    }

    @Nullable
    default ExtendedFoodProperties fallacy$getExtendedFoodProperties() {
        throw new UnsupportedOperationException("This should not happen");
    }
}

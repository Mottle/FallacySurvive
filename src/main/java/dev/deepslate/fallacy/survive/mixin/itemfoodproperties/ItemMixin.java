package dev.deepslate.fallacy.survive.mixin.itemfoodproperties;

import dev.deepslate.fallacy.survive.diet.item.ExtendedFoodProperties;
import dev.deepslate.fallacy.survive.inject.itemfoodproperties.ItemFoodPropertiesExtension;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemFoodPropertiesExtension {

    @Unique
    protected ExtendedFoodProperties fallacy$extendedFoodProperties = null;

    @Unique
    private Item fallacy$self() {
        return (Item) (Object) this;
    }

    @Override
    public void fallacy$internalSetExtendedFoodProperties(ExtendedFoodProperties data) {
        fallacy$extendedFoodProperties = data;
    }

    @Nullable
    @Override
    public ExtendedFoodProperties fallacy$getExtendedFoodProperties() {
        return fallacy$extendedFoodProperties;
    }
}

package dev.deepslate.fallacy.survive

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType

object ModDamageTypes {
    @JvmStatic
    val DEHYDRATION: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, TheMod.withID("dehydration"))

    init {
        TheMod.REGISTRATE.addLang("death.attack", TheMod.withID("dehydration"), "%1\$s died of dehydration")
    }
}
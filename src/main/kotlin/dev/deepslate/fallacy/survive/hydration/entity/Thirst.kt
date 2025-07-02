package dev.deepslate.fallacy.survive.hydration.entity

interface Thirst {
    var value: Float

    val max: Float

    fun drink(value: Float)

    fun tick()
}
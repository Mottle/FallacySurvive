package dev.deepslate.fallacy.survive.thermal

interface HeatSensitive {
    var heat: Float

    var conductivity: Float

    val k: Float

    fun tick()
}

fun HeatSensitive.dh(
    localHeat: Int,
    conductivity: Float = this.conductivity,
    tickIntervalFixConstant: Number = 20
): Float {
    val absoluteDH = localHeat - heat
    val relativeDH = absoluteDH * conductivity
    val dh = k * relativeDH
    val dhPerTickInterval = dh * tickIntervalFixConstant.toFloat()

    return dhPerTickInterval
}

//fun HeatSensitive.dh(
//    localHeat: Int,
//    conductivity: Float = this.conductivity,
//    tickIntervalFixConstant: Number = 20,
//    minRelative: Number
//): Float {
//    val absoluteDH = localHeat - heat
//    val relativeDH = absoluteDH * conductivity
//
//    if (relativeDH < minRelative.toFloat()) return 0f
//
//    val dh = k * relativeDH
//    val dhPerTickInterval = dh * tickIntervalFixConstant.toFloat()
//
//    return dhPerTickInterval
//}
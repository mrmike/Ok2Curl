package com.moczul.ok2curl


import com.moczul.ok2curl.modifier.HeaderModifier

class Configuration @JvmOverloads constructor(
    val headerModifiers: List<HeaderModifier> = emptyList(),
    val components: List<CommandComponent> = CommandComponent.DEFAULT,
    val flags: Flags = Flags.EMPTY,
    val limit: Long = 1024L * 1024L,
    val delimiter: String = " "
)
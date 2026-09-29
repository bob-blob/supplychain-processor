package com.prewave.supplychain_processor.tree

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

enum class TreeFormat {
    NESTED,
    FLAT
}

@Component
class TreeFormatConverter : Converter<String, TreeFormat> {
    override fun convert(source: String): TreeFormat =
        TreeFormat.entries.firstOrNull { it.name.equals(source, ignoreCase = true) }
            ?: throw InvalidFormatException(source)
}
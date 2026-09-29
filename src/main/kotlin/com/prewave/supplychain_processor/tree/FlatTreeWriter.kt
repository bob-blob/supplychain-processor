package com.prewave.supplychain_processor.tree

import tools.jackson.core.JsonGenerator


class FlatTreeWriter(
    private val generator: JsonGenerator,
) {
    fun start() {
        generator.writeStartArray()
    }

    fun write(row: FlatTreeRow) {
        generator.writeStartObject()
        generator.writeNumberProperty("id", row.id)
        if (row.parentId == null) {
            generator.writeNullProperty("parentId")
        } else {
            generator.writeNumberProperty("parentId", row.parentId)
        }
        generator.writeNumberProperty("depth", row.depth)
        generator.writeEndObject()
    }

    fun finish() {
        generator.writeEndArray()
        generator.flush()
    }
}
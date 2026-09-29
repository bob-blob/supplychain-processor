package com.prewave.supplychain_processor.tree

import tools.jackson.core.JsonGenerator


class NestedTreeWriter(
    private val generator: JsonGenerator,
) {
    private var depth = -1;

    fun write(row: TreeRow) {
        while (depth >= row.depth) {
            closeNode()
        }
        generator.writeStartObject()
        generator.writeNumberProperty("id", row.id)
        generator.writeArrayPropertyStart("children")
        depth = row.depth
    }

    fun closeNode() {
        generator.writeEndArray()
        generator.writeEndObject()
        depth--
    }

    fun finish() {
        while (depth >= 0) {
            closeNode()
        }
        generator.flush()
    }
}
package com.prewave.supplychain_processor.edge

import jakarta.validation.constraints.NotNull;

data class Edge(
    val fromId: Int,
    val toId: Int,
)

data class EdgeRequest(
    @field:NotNull val fromId: Int,
    @field:NotNull val toId: Int,
) {
    fun toEdge() = Edge(fromId, toId)
}
package com.prewave.supplychain_processor.edge

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/edge")
class EdgeController(
    private val edgeService: EdgeService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: EdgeRequest): Edge? {
        return edgeService.create(request.toEdge())
    }

    @DeleteMapping("/{fromId}/{toId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable fromId: Int, @PathVariable toId: Int) {
        edgeService.delete(Edge(fromId, toId))
    }

}
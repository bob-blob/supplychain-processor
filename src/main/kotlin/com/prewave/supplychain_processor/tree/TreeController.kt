package com.prewave.supplychain_processor.tree

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

@RestController
@RequestMapping("/api/v1/tree")
class TreeController(
    private val treeService: TreeService,
) {

    @GetMapping("/{nodeId}")
    fun get(
        @PathVariable("nodeId") nodeId: Int,
        @RequestParam(defaultValue = "nested") format: TreeFormat,
    ): ResponseEntity<StreamingResponseBody> {
        if (!treeService.nodeExists(nodeId)) {
            throw NodeNotFoundException(nodeId)
        }
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(StreamingResponseBody { outputStream ->
                treeService.writeTree(nodeId, format, outputStream)
            })
    }
}
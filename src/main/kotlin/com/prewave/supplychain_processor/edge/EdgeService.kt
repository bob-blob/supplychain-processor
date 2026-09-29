package com.prewave.supplychain_processor.edge

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EdgeService(
    private val repository: EdgeRepository,
) {

    @Transactional
    fun create(edge: Edge): Edge? {
        if (edge.fromId == edge.toId) {
            throw EdgeLoopException(edge.fromId)
        }
        repository.lockWrites()
        if (repository.exists(edge)) {
            throw EdgeAlreadyExistsException(edge.fromId, edge.toId)
        }
        val parentNodeId = repository.findParent(edge.toId)
        if (parentNodeId != null) {
            throw ParentAlreadyExistsException(edge.toId, parentNodeId)
        }
        if (repository.isAncestor(edge.toId, edge.fromId)) {
            throw CycleDetectedException(edge.fromId, edge.toId)
        }
        repository.insert(edge)
        return edge
    }

    fun delete(edge: Edge) {
        if (repository.delete(edge) == 0) {
            throw EdgeNotFoundException(edge.fromId, edge.toId)
        }
    }
}
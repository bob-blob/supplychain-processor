package com.prewave.supplychain_processor.edge

import com.prewave.supplychain_processor.error.BadRequestException
import com.prewave.supplychain_processor.error.ConflictException
import com.prewave.supplychain_processor.error.NotFoundException

class EdgeAlreadyExistsException(fromId: Int, toId: Int) :
    ConflictException("The edge $fromId -> $toId already exists")

class CycleDetectedException(fromId: Int, toId: Int) :
    ConflictException("Adding edge $fromId -> $toId would create a cycle")

class ParentAlreadyExistsException(toId: Int, parentId: Int) :
    ConflictException("The parent for node $toId already exists: $parentId")

class EdgeLoopException(nodeId: Int) :
    BadRequestException("Can't link node $nodeId to itself")

class EdgeNotFoundException(fromId: Int, toId: Int) :
    NotFoundException("Edge $fromId -> $toId is not found")

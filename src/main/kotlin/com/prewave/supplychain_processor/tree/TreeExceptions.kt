package com.prewave.supplychain_processor.tree

import com.prewave.supplychain_processor.error.BadRequestException
import com.prewave.supplychain_processor.error.NotFoundException

class NodeNotFoundException(nodeId: Int) : NotFoundException("Node $nodeId not found")

class InvalidFormatException(format: String) :
    BadRequestException("Invalid tree response format: $format. Allowed valued: nested, flat.")
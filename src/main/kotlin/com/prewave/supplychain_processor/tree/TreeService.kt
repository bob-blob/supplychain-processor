package com.prewave.supplychain_processor.tree

import org.springframework.stereotype.Service
import tools.jackson.core.StreamWriteConstraints
import tools.jackson.core.json.JsonFactory
import tools.jackson.databind.json.JsonMapper
import java.io.OutputStream

@Service
class TreeService(
    private val treeRepository: TreeRepository,
) {

    private val treeMapper = JsonMapper.builder(
        JsonFactory.builder()
            .streamWriteConstraints(
                StreamWriteConstraints.builder()
                    .maxNestingDepth(Int.MAX_VALUE)
                    .build()
            ).build()
    ).build()

    fun nodeExists(nodeId: Int): Boolean = treeRepository.nodeExists(nodeId)

    fun writeTree(rootId: Int, format: TreeFormat, output: OutputStream) = when (format) {
        TreeFormat.NESTED -> writeNestedTree(rootId, output)
        TreeFormat.FLAT -> writeFlatTree(rootId, output)
    }

    fun writeNestedTree(rootId: Int, output: OutputStream) {
        val writer = NestedTreeWriter(treeMapper.createGenerator(output))
        treeRepository.streamNestedSubtree(rootId, writer::write)
        writer.finish()
    }

    private fun writeFlatTree(rootId: Int, output: OutputStream) {
        val writer = FlatTreeWriter(treeMapper.createGenerator(output))
        writer.start()
        treeRepository.streamFlatSubtree(rootId, writer::write)
        writer.finish()
    }

}
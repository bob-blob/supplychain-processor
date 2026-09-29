package com.prewave.supplychain_processor.tree

import com.prewave.supplychain_processor.jooq.Tables.EDGE
import org.jooq.DSLContext
import org.jooq.impl.DSL.*
import org.jooq.impl.SQLDataType.INTEGER
import org.springframework.stereotype.Repository

data class TreeRow(
    val id: Int,
    val depth: Int,
)

private val TREE = name("tree")
private val TREE_ID = field(name("tree", "id"), INTEGER)
private val TREE_DEPTH = field(name("tree", "depth"), INTEGER)
private val TREE_PATH = field(name("tree", "path"), INTEGER.array())

data class FlatTreeRow(
    val id: Int,
    val parentId: Int?,
    val depth: Int
)

private val TREE_PARENT_ID = field(name("tree", "parent_id"), INTEGER)


@Repository
class TreeRepository(
    private val dsl: DSLContext,
) {

    fun nodeExists(nodeId: Int) = dsl.fetchExists(EDGE, EDGE.FROM_ID.eq(nodeId).or(EDGE.TO_ID.eq(nodeId)))

    fun streamNestedSubtree(rootId: Int, writer: (TreeRow) -> Unit) {
        val tree = TREE.fields("id", "depth", "path").`as`(
            select(value(rootId), inline(0), value(arrayOf(rootId), INTEGER.array()))
                .unionAll(
                    select(EDGE.TO_ID, TREE_DEPTH.plus(1), arrayAppend(TREE_PATH, EDGE.TO_ID))
                        .from(EDGE)
                        .join(table(TREE))
                        .on(EDGE.FROM_ID.eq(TREE_ID))
                )
        )

        dsl.transaction { config ->
            using(config)
                .withRecursive(tree)
                .select(TREE_ID, TREE_DEPTH)
                .from(TREE)
                .orderBy(TREE_PATH)
                .fetchSize(10000)
                .fetchLazy()
                .use { cursor ->
                    cursor.forEach { writer(TreeRow(it.value1(), it.value2())) }
                }
        }
    }

    fun streamFlatSubtree(rootId: Int, writer: (FlatTreeRow) -> Unit) {
        val tree =
            TREE.fields("id", "parent_id", "depth").`as`(
                select(value(rootId), castNull(INTEGER), inline(0))
                    .unionAll(
                        select(EDGE.TO_ID, EDGE.FROM_ID, TREE_DEPTH.plus(1))
                            .from(EDGE)
                            .join(table(TREE))
                            .on(EDGE.FROM_ID.eq(TREE_ID))
                    )
            )

        dsl.transaction { config ->
            using(config)
                .withRecursive(tree)
                .select(TREE_ID, TREE_PARENT_ID, TREE_DEPTH)
                .from(tree)
                .fetchSize(10000)
                .fetchLazy()
                .use { cursor ->
                    cursor.forEach { writer(FlatTreeRow(it.value1(), it.value2(), it.value3())) }
                }
        }

    }
}
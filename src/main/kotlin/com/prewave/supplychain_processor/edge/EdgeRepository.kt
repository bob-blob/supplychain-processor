package com.prewave.supplychain_processor.edge

import com.prewave.supplychain_processor.jooq.Tables.EDGE
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType.INTEGER
import org.springframework.stereotype.Repository

@Repository
class EdgeRepository(
    private val dsl: DSLContext
) {

    fun lockWrites() {
        dsl.execute("lock table edge in share row exclusive mode")
    }

    fun exists(edge: Edge): Boolean = dsl.fetchExists(EDGE, EDGE.FROM_ID.eq(edge.fromId).and(EDGE.TO_ID.eq(edge.toId)))

    fun insert(edge: Edge) {
        dsl.insertInto(EDGE, EDGE.FROM_ID, EDGE.TO_ID)
            .values(edge.fromId, edge.toId)
            .execute()
    }

    fun findParent(nodeId: Int): Int? {
        return dsl.select(EDGE.FROM_ID)
            .from(EDGE)
            .where(EDGE.TO_ID.eq(nodeId))
            .fetchOne(EDGE.FROM_ID)
    }

    fun delete(edge: Edge): Int {
        return dsl.deleteFrom(EDGE)
            .where(EDGE.FROM_ID.eq(edge.fromId).and(EDGE.TO_ID.eq(edge.toId)))
            .execute()
    }

    fun isAncestor(ancestorId: Int, nodeId: Int): Boolean {
        val ancestors = DSL.name("ancestors")
        val ancestorsId = DSL.field(DSL.name("ancestors", "id"), INTEGER)

        val cte = ancestors.fields("id").`as`(
            DSL.select(EDGE.FROM_ID)
                .from(EDGE)
                .where(EDGE.TO_ID.eq(nodeId))
                .union(
                    DSL.select(EDGE.FROM_ID)
                        .from(EDGE)
                        .join(DSL.table(ancestors))
                        .on(EDGE.TO_ID.eq(ancestorsId))
                )
        )

        return dsl.fetchExists(
            DSL.withRecursive(cte)
                .selectOne()
                .from(cte)
                .where(ancestorsId.eq(ancestorId))
        )
    }
}
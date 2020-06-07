package com.s4n.deliverydrone.repository

interface CrudRepository<T : GenericEntity> {
    fun create(obj: T): T
    fun getById(id: Long): T?
    fun update(obj: T): T?
    fun delete(obj: T)
}

/**
 * Generic Repository to implement
 * */
open class CrudRepositoryImpl<T : GenericEntity> : CrudRepository<T> {

    val list: MutableList<T> = mutableListOf()

    override fun create(obj: T): T {
        list.add(obj)
        return obj
    }

    override fun getById(id: Long): T? {
        return list.find { it.id == id }
    }

    override fun update(obj: T): T? {
        val elementToRemove = list.find { it.id == obj.id }
        return if (list.remove(elementToRemove)) {
            list.add(obj)
            obj
        } else null
    }

    override fun delete(obj: T) {
        list.remove(obj)
    }
}

open class GenericEntity(
    open var id: Long
)
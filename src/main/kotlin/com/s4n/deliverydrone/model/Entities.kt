package com.s4n.deliverydrone.model

import com.s4n.deliverydrone.repository.GenericEntity
import com.s4n.deliverydrone.util.DronType
import java.time.LocalDateTime

/**
 * This file contains the objects that represents the entities
 * the route entity let to track all the trace of the day or more
 * */
data class Dron(override var id: Long, var position: Position, val dronType: DronType): GenericEntity(id)
data class Route(override var id: Long, val initialPosition:Position,
                 val finalPosition:Position, val date: LocalDateTime, val dronId:Long): GenericEntity(id)
package com.s4n.deliverydrone.service

import com.s4n.deliverydrone.drondriver.Command
import com.s4n.deliverydrone.model.Dron
import com.s4n.deliverydrone.model.Route
import com.s4n.deliverydrone.repository.DronRepository
import com.s4n.deliverydrone.repository.RouteRepository
import com.s4n.deliverydrone.util.DronType
import java.time.LocalDateTime
import java.util.logging.Logger

/**
 * Object responsible of manage Dron position
 */
object PositionService {

    /**
     * I know these declarations are not necessary is only to try to expose dependencies in a clear way
     */
    private val routeRepository = RouteRepository
    private val dronRepository: DronRepository = DronRepository
    private val log: Logger = Logger.getLogger(PositionService::javaClass.name)

    fun moveDron(command: Command, dron: Dron): Boolean {
        val dronExecuter = DronType.values().find { it == dron.dronType }?.dronExecutor
        return dronExecuter?.let {
            val finalPosition = dron.position.copy()
            command.execute(dronExecuter, finalPosition)
            // Once the command is execute is generated the new position, with this is necessary to create the trace in route entity
            // and the new position must be updated in dron entity
            if (dronRepository.getById(dron.id) == null) {
                dronRepository.create(dron)
            }
            val route = Route((0..100000).random().toLong(), dron.position, finalPosition, LocalDateTime.now(), dron.id)
            dron.position = finalPosition
            dronRepository.update(dron)
            routeRepository.create(route)
            true
        } ?: run {
            log.warning("the type of dron does not exist")
            false
        }
    }
}
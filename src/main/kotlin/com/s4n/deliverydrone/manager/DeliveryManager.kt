package com.s4n.deliverydrone.manager

import com.s4n.deliverydrone.model.DeliveryState
import com.s4n.deliverydrone.model.Dron
import com.s4n.deliverydrone.model.Position
import com.s4n.deliverydrone.reader.FileReaderImpl
import com.s4n.deliverydrone.repository.RouteRepository
import com.s4n.deliverydrone.service.PositionService
import com.s4n.deliverydrone.util.*
import java.io.File
import java.util.logging.Logger
import kotlin.math.sqrt

/**
 * this is the general manager of deliveries. Its purpose is manage the whole delivery process. Since read instructions
 * until write final report
 */
object DeliveryManager {

    private val log: Logger = Logger.getLogger(DeliveryManager::javaClass.name)

    private val positionService = PositionService
    private val instructionReader = FileReaderImpl
    private val routeRepository = RouteRepository


    fun executeDeliveries(): GenericResponse {
        val instructionsToExecute = instructionReader.readInstructions()
        return if (instructionsToExecute.size <= NUMBER_OF_DRONS) {
            val deliveriesToReport = mutableListOf<DeliveryState>()
            instructionsToExecute.forEach dronDelivery@{ instructionsPerDron ->
                val dron = Dron(instructionsPerDron.id, INITIAL_DEFAULT_POSITION, DronType.DEFAULT)
                instructionsPerDron.instructions.forEach { command ->
                    if (!positionService.moveDron(command, dron)) {
                        val message = "Dron delivery incomplete"
                        log.warning(message)
                        deliveriesToReport.add(DeliveryState(dron, message))
                        return@dronDelivery
                    }
                    if (!validateDistance(dron.position)) {
                        val message = "Dron is out of limits, It must back to home id ${dron.id}"
                        log.warning(message)
                        deliveriesToReport.add(DeliveryState(dron, message))
                        return@dronDelivery
                    }

                }
                deliveriesToReport.add(DeliveryState(dron, "done"))
            }
            createReports(deliveriesToReport)
            GenericResponse.Success("Done")
        } else {
            val message = "You are asking more than 20 Drons"
            log.warning(message)
            GenericResponse.Error(message)
        }
    }

    private fun createReports(deliveries: List<DeliveryState>) {

        val file = File(REPORT_PATH)
        if (!file.exists()) {
            file.mkdir()
        }
        deliveries.forEach { deliveryState ->

            val lines = mutableListOf(TITLE_REPORT, "", "")
            routeRepository.getRoutesByDronId(deliveryState.dron.id).sortedBy { it.date }.forEach { route ->
                lines.addAll(
                    listOf(
                        "(${route.initialPosition.x},${route.initialPosition.y}) direction ${route.initialPosition.direction.cardinalName}",
                        "",
                        ""
                    )
                )
            }
            lines.add("final status: ${deliveryState.message}")
            writeFileTxt("report_${deliveryState.dron.id}.txt", lines, REPORT_PATH)
        }
    }

    /**
     * This validation could be at the begin if there would be exist only one type of Dron or one type of change position
     */
    private fun validateDistance(position: Position): Boolean {
        val distance = sqrt((position.x * position.x + position.y * position.y).toDouble())
        return distance < MAXIMUM_NUMBER_BLOCKS
    }
}

sealed class GenericResponse(message: String) {
    class Success(message: String) : GenericResponse(message)
    class Error(message: String) : GenericResponse(message)
}
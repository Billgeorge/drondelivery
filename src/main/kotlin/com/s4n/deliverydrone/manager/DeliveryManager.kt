package com.s4n.deliverydrone.manager


import com.s4n.deliverydrone.drondriver.Command
import com.s4n.deliverydrone.drondriver.GoForwardCommand
import com.s4n.deliverydrone.drondriver.DeliveryCommand
import com.s4n.deliverydrone.drondriver.TurnLeftCommand
import com.s4n.deliverydrone.drondriver.TurnRightCommand
import com.s4n.deliverydrone.model.DeliveryState
import com.s4n.deliverydrone.model.Dron
import com.s4n.deliverydrone.model.Position
import com.s4n.deliverydrone.reader.DronInstruction
import com.s4n.deliverydrone.reader.FileReaderImpl
import com.s4n.deliverydrone.repository.RouteRepository
import com.s4n.deliverydrone.service.PositionService
import com.s4n.deliverydrone.util.*
import goForwardPosition
import org.apache.log4j.Logger
import turnPosition
import java.io.File
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
        return try {
            val instructionsToExecute = instructionReader.readInstructions()
            if (instructionsToExecute.size <= NUMBER_OF_DRONS) {
                val deliveriesToReport = mutableListOf<DeliveryState>()
                processDeliveries(instructionsToExecute, deliveriesToReport)
                createReports(deliveriesToReport)
                GenericResponse.Success("Done")
            } else {
                val message = "You are asking more than 20 Drons"
                log.warn(message)
                GenericResponse.Error(message)
            }
        } catch (e: Exception) {
            log.error(e)
            GenericResponse.Error(e.message!!)
        }
    }

    private fun processDeliveries(
        instructionsToExecute: List<DronInstruction>,
        deliveriesToReport: MutableList<DeliveryState>
    ) {
        instructionsToExecute.forEach dronDelivery@{ instructionsPerDron ->
            val dron = Dron(instructionsPerDron.id, INITIAL_DEFAULT_POSITION, DronType.DEFAULT)
            // in case that one or more deliveries are out of range, It would be ideal recalculate new routes, generating instructions
            val message = "One or more deliveries are out of limits for Dron ${dron.id}, " +
                    "please create new instructions"
            if (addErrorMessageToReport(dron, message, deliveriesToReport) {
                    areDeliveriesInsideLimits(
                        instructionsPerDron.instructions
                    )
                }) return@dronDelivery

            instructionsPerDron.instructions.forEach { command ->

                if (addErrorMessageToReport(
                        dron,
                        "Dron delivery incomplete",
                        deliveriesToReport
                    ) { positionService.moveDron(command, dron) }
                ) return@dronDelivery
            }
            deliveriesToReport.add(DeliveryState(dron, "done"))
        }
    }

    private fun addErrorMessageToReport(
        dron: Dron,
        message: String,
        deliveriesToReport: MutableList<DeliveryState>,
        booleanFunction: () -> Boolean
    ): Boolean {
        if (!booleanFunction()) {
            log.warn(message)
            deliveriesToReport.add(DeliveryState(dron, message))
            return true
        }
        return false
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

    private fun areDeliveriesInsideLimits(commands: List<Command>): Boolean {
        var finalPosition = INITIAL_DEFAULT_POSITION.copy()
        commands.forEach { command ->
            when (command) {
                TurnLeftCommand -> {
                    turnPosition(finalPosition, true)
                }
                TurnRightCommand -> {
                    turnPosition(finalPosition, false)
                }
                GoForwardCommand -> {
                    goForwardPosition(finalPosition)
                }
                DeliveryCommand -> {
                    if (!validateDistance(finalPosition)) {
                        return false
                    }
                }
            }
        }
        return true
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
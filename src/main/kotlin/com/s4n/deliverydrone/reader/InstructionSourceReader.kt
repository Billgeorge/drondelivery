package com.s4n.deliverydrone.reader

import com.s4n.deliverydrone.drondriver.*
import com.s4n.deliverydrone.util.Commands
import com.s4n.deliverydrone.util.INSTRUCTIONS_PATH
import com.s4n.deliverydrone.util.MAXIMUM_NUMBER_DELIVERIES
import java.io.File
import java.util.logging.Logger


/**
 * This interface is the responsible of decouple the source instructions. For example, for creating in future a controller to receive
 * instruction (thinking in expose a restful api)
 */
interface InstructionSourceReader {

    fun readInstructions(): List<DronInstruction>
}

object FileReaderImpl : InstructionSourceReader {

    private val log: Logger = Logger.getLogger(FileReaderImpl::javaClass.name)

    override fun readInstructions(): List<DronInstruction> {
        val instructionList = mutableListOf<DronInstruction>()

        File(INSTRUCTIONS_PATH).walk().forEach readingFiles@{ file ->
            log.info("reading file ${file.absoluteFile}")
            if (file.isFile) {
                val commandsPerDron = mutableListOf<Command>()
                val lines = file.readLines()
                if (lines.size > MAXIMUM_NUMBER_DELIVERIES) {
                    log.warning("One Dron can only have $MAXIMUM_NUMBER_DELIVERIES deliveries, file omitted")
                    return@readingFiles
                }
                lines.forEach { instruction ->
                    log.info("reading instruction $instruction")
                    val instructionWithoutSpaces = instruction.replace("\\s".toRegex(), "").trim()
                    if (instructionWithoutSpaces.isNotBlank()) {
                        convertToInstruction(instructionWithoutSpaces)?.let { dronInstruction ->
                            commandsPerDron.addAll(dronInstruction)
                        } ?: return@readingFiles
                    }
                }
                if (commandsPerDron.isNotEmpty()) {
                    val nextId = instructionList.maxBy { it.id }?.id?.plus(1) ?: 1
                    instructionList.add(DronInstruction(nextId, commandsPerDron))
                }
            }
        }
        return instructionList
    }

    private fun convertToInstruction(
        setOfInstructions: String
    ): List<Command>? {

        val instructionListPerDron = mutableListOf<Command>()
        setOfInstructions.toUpperCase().forEach { simpleinstruction ->
            val command = Commands.values().find { it.name == simpleinstruction.toString() }?.command ?: return null
            instructionListPerDron.add(command)
        }
        instructionListPerDron.add(DeliveryCommand)
        return instructionListPerDron
    }
}

data class DronInstruction(val id: Long, val instructions: List<Command>)

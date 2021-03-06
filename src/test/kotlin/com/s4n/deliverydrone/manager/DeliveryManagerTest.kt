package com.s4n.deliverydrone.manager

import com.s4n.deliverydrone.reader.CORRECT_LINE_INSTRUCTIONS
import com.s4n.deliverydrone.reader.CORRECT_LINE_INSTRUCTIONS_1
import com.s4n.deliverydrone.reader.CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1
import com.s4n.deliverydrone.reader.LINE_INSTRUCTIONS_EXCEED_DISTANCE
import com.s4n.deliverydrone.util.INSTRUCTIONS_PATH
import com.s4n.deliverydrone.util.REPORT_PATH
import com.s4n.deliverydrone.util.writeFileTxt
import org.junit.jupiter.api.Test
import java.io.File

internal class DeliveryManagerTest {

    @Test
    fun `happy execution with 20 files`() {
        createListOfCorrectFiles(20) {
            val response = DeliveryManager.executeDeliveries()
            assert(File(REPORT_PATH).listFiles().size==20)
            assert(response is GenericResponse.Success)
            validateContentReport()
        }
    }

    @Test
    fun `happy execution with 21 files`() {
        createListOfCorrectFiles(21) {
            val response = DeliveryManager.executeDeliveries()
            assert(response is GenericResponse.Error)
        }
    }

    @Test
    fun `happy execution with one file of 4 instructions`() {
        createListOfCorrectFiles(20,listOf(CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1, CORRECT_LINE_INSTRUCTIONS_1)) {
            val response = DeliveryManager.executeDeliveries()
            assert(File(REPORT_PATH).listFiles().size==19)
            assert(response is GenericResponse.Success)
            validateContentReport()
        }
    }

    @Test
    fun `happy execution with one file that exceeds the distance limit`() {
        //report 13 report Dron is out of limits, It must back to home id
        createListOfCorrectFiles(20, listOf(LINE_INSTRUCTIONS_EXCEED_DISTANCE)) {
            val response = DeliveryManager.executeDeliveries()
            assert(File(REPORT_PATH).listFiles().size==20)
            assert(response is GenericResponse.Success)
            val lines = File(REPORT_PATH,"report_13.txt").readLines()
            assert("final status: One or more deliveries are out of limits for Dron 13, please create new instructions"==lines[3])
        }
    }

    private fun validateContentReport() {
        val lines = File(REPORT_PATH, "report_1.txt").readLines()
        assert("(-3,2) direction West" == lines[3])
        assert("(-5,-1) direction South" == lines[6])
        assert("(-5,-8) direction South" == lines[9])
    }


    private fun createListOfCorrectFiles(
        numberFiles: Int,
        listOfInstructions: List<String> = listOf(CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1), testEvaluation: () -> Unit
    ) {
        File(INSTRUCTIONS_PATH).mkdir()
        writeFileTxt(
            "inst_1.txt", listOfInstructions + listOf(
                CORRECT_LINE_INSTRUCTIONS,
                CORRECT_LINE_INSTRUCTIONS_1
            ), INSTRUCTIONS_PATH
        )
        for (i in 2..numberFiles) {
            writeFileTxt(
                "inst_$i.txt", listOf(
                    CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1,
                    CORRECT_LINE_INSTRUCTIONS,
                    CORRECT_LINE_INSTRUCTIONS_1
                ),
                INSTRUCTIONS_PATH
            )
        }
        testEvaluation()
        File(INSTRUCTIONS_PATH).deleteRecursively()
        File(REPORT_PATH).deleteRecursively()
    }
}
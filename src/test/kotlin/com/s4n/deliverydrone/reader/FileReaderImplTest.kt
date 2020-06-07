package com.s4n.deliverydrone.reader

import com.s4n.deliverydrone.util.INSTRUCTIONS_PATH
import com.s4n.deliverydrone.util.writeFileTxt
import org.junit.jupiter.api.*
import java.io.File


const val CORRECT_LINE_INSTRUCTIONS = "AAIAADIA"
const val INCORRECT_LINE_INSTRUCTIONS = "AAIAADIAS"
const val CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1 = "AA  IAADIA    "
const val CORRECT_LINE_INSTRUCTIONS_1 = "AAAAADIAA"
const val LINE_INSTRUCTIONS_EXCEED_DISTANCE = "AAAAAAAAAA"

internal class FileReaderImplTest {

    @Test
    fun `reading a correct instructions set`() {
        executeGeneralTest(
            listOf(CORRECT_LINE_INSTRUCTIONS, CORRECT_LINE_INSTRUCTIONS_1, CORRECT_LINE_INSTRUCTIONS),
            listOf(CORRECT_LINE_INSTRUCTIONS_1, CORRECT_LINE_INSTRUCTIONS, CORRECT_LINE_INSTRUCTIONS_1)
        ) {
            val instructionList = FileReaderImpl.readInstructions()

            assert(instructionList.size == 2)
            assert(instructionList[0].id == 1.toLong())
            assert(instructionList[1].id == 2.toLong())
            assert(instructionList[0].instructions.size == (CORRECT_LINE_INSTRUCTIONS + CORRECT_LINE_INSTRUCTIONS_1 + CORRECT_LINE_INSTRUCTIONS).length + 3)
            assert(instructionList[1].instructions.size == (CORRECT_LINE_INSTRUCTIONS_1 + CORRECT_LINE_INSTRUCTIONS + CORRECT_LINE_INSTRUCTIONS_1).length + 3)
        }
    }

    @Test
    fun `reading one correct file and one wrong file`() {

        executeGeneralTest(
            listOf(CORRECT_LINE_INSTRUCTIONS, INCORRECT_LINE_INSTRUCTIONS, CORRECT_LINE_INSTRUCTIONS),
            listOf(CORRECT_LINE_INSTRUCTIONS_1, CORRECT_LINE_INSTRUCTIONS, CORRECT_LINE_INSTRUCTIONS_1)
        ) {
            val instructionList = FileReaderImpl.readInstructions()

            assert(instructionList.size == 1)
            assert(instructionList[0].id == 1.toLong())
            assert(instructionList[0].instructions.size == (CORRECT_LINE_INSTRUCTIONS_1 + CORRECT_LINE_INSTRUCTIONS + CORRECT_LINE_INSTRUCTIONS_1).length + 3)
        }
    }

    @Test
    fun `reading empty files `() {

        executeGeneralTest(
            emptyList(),
            emptyList()
        ) {
            val instructionList = FileReaderImpl.readInstructions()
            assert(instructionList.isEmpty())
        }
    }

    @Test
    fun `reading empty dir `() {

        val instructionList = FileReaderImpl.readInstructions()
        assert(instructionList.isEmpty())
    }

    @Test
    fun `reading without correct Files`() {

        executeGeneralTest(
            listOf(CORRECT_LINE_INSTRUCTIONS, INCORRECT_LINE_INSTRUCTIONS, CORRECT_LINE_INSTRUCTIONS),
            listOf(INCORRECT_LINE_INSTRUCTIONS, CORRECT_LINE_INSTRUCTIONS, CORRECT_LINE_INSTRUCTIONS_1)
        ) {
            val instructionList = FileReaderImpl.readInstructions()
            assert(instructionList.isEmpty())
        }
    }

    @Test
    fun `reading one correct file and one file with instructions with blank spaces`() {

        executeGeneralTest(
            listOf(
                CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1,
                CORRECT_LINE_INSTRUCTIONS,
                CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1
            ),
            listOf(CORRECT_LINE_INSTRUCTIONS_1, CORRECT_LINE_INSTRUCTIONS, CORRECT_LINE_INSTRUCTIONS_1)
        ) {

            val instructionList = FileReaderImpl.readInstructions()
            val instructionWithoutSpaces = CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1.replace("\\s".toRegex(), "").trim()
            assert(instructionList.size == 2)
            assert(instructionList[0].id == 1.toLong())
            assert(instructionList[0].instructions.size == (instructionWithoutSpaces + CORRECT_LINE_INSTRUCTIONS + instructionWithoutSpaces).length + 3)
            assert(instructionList[1].id == 2.toLong())
            assert(instructionList[1].instructions.size == (CORRECT_LINE_INSTRUCTIONS_1 + CORRECT_LINE_INSTRUCTIONS + CORRECT_LINE_INSTRUCTIONS_1).length + 3)
        }

    }

    @Test
    fun `reading one correct file and one file with 4 deliveries`() {

        executeGeneralTest(
            listOf(
                CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1,
                CORRECT_LINE_INSTRUCTIONS,
                CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1,
                CORRECT_LINE_INSTRUCTIONS_BLANK_SPACES_1
            ),
            listOf(CORRECT_LINE_INSTRUCTIONS_1, CORRECT_LINE_INSTRUCTIONS, CORRECT_LINE_INSTRUCTIONS_1)
        ) {

            val instructionList = FileReaderImpl.readInstructions()
            assert(instructionList.size == 1)
            assert(instructionList[0].id == 1.toLong())
            assert(instructionList[0].instructions.size == (CORRECT_LINE_INSTRUCTIONS_1 + CORRECT_LINE_INSTRUCTIONS + CORRECT_LINE_INSTRUCTIONS_1).length + 3)
        }

    }

    private fun executeGeneralTest(
        firstFileInstructions: List<String>,
        firstFileInstructions1: List<String>,
        testExecution: () -> Unit
    ) {
        File(INSTRUCTIONS_PATH).mkdir()
        val fileName = "ins_1.txt"
        val fileName1 = "ins_2.txt"
        writeFileTxt(fileName, firstFileInstructions, INSTRUCTIONS_PATH)
        writeFileTxt(fileName1, firstFileInstructions1, INSTRUCTIONS_PATH)
        testExecution()
        File( INSTRUCTIONS_PATH).deleteRecursively()
    }
}
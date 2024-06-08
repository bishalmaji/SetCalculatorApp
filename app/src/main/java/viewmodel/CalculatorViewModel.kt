package com.bishal.setcalculatorapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    var textFieldOne by mutableStateOf(TextFieldValue(""))
    var textFieldTwo by mutableStateOf(TextFieldValue(""))
    var textFieldThree by mutableStateOf(TextFieldValue(""))

    var resultIntersection by mutableStateOf("")
    var resultUnion by mutableStateOf("")
    var resultHighest by mutableStateOf("")

    fun calculateNumbers() {
        val numbersOne = processInput(textFieldOne.text).second
        val numbersTwo = processInput(textFieldTwo.text).second
        val numbersThree = processInput(textFieldThree.text).second

        val intersection = calculateIntersection(numbersOne, numbersTwo, numbersThree)
        val union = calculateUnion(numbersOne, numbersTwo, numbersThree)
        val highest = calculateHighest(numbersOne, numbersTwo, numbersThree)

        resultIntersection = intersection.joinToString(" ")
        resultUnion = union.joinToString(" ")
        resultHighest = highest.toString()
    }

    private fun processInput(input: String): Pair<String, List<Int>> {
        val sanitizedInput = input.filter { it.isDigit() || it == ',' }
        val newNumbers = sanitizedInput.split(',')
            .filter { it.isNotEmpty() }
            .mapNotNull { it.toIntOrNull() }

        val newText = if (newNumbers.isNotEmpty() && input.last().isDigit()) {
            newNumbers.joinToString(", ") + ", "
        } else {
            newNumbers.joinToString(", ")
        }
        return Pair(newText, newNumbers)
    }

    private fun calculateIntersection(set1: List<Int>, set2: List<Int>, set3: List<Int>): Set<Int> {
        val intersection12 = set1.toSet().intersect(set2.toSet())
        return intersection12.intersect(set3.toSet())
    }

    private fun calculateUnion(set1: List<Int>, set2: List<Int>, set3: List<Int>): Set<Int> {
        return set1.toSet().union(set2.toSet()).union(set3.toSet())
    }

    private fun calculateHighest(set1: List<Int>, set2: List<Int>, set3: List<Int>): Int {
        return (set1 + set2 + set3).maxOrNull() ?: 0
    }
}
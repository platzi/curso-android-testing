package com.juandgaines.testground.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.prefs.Preferences

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTestExample {

    private suspend fun delayedOperation(): Int {
        delay(1000) // Simulate a 1-second delay
        return 42
    }


    @Test
    fun givenDelayedOperation_whenUsingRunBlocking_thenWaitsForRealDelay() = runBlocking{
        // This test will actually wait for 1 second
         val result =  delayedOperation()
        assertEquals(42, result)
    }

    @Test
    fun givenDelayedOperation_whenUsingRunTest_thenWaitsForRealDelay() = runTest{
        // This test will actually wait for 1 second
        val result =  delayedOperation()
        assertEquals(42, result)
    }


    // Simple function that takes 1 second to complete
    private suspend fun waitOneSecond(): Long {
        val startTime = System.currentTimeMillis()
        delay(1000)
        return System.currentTimeMillis() - startTime
    }

    @Test
    fun runBlockingExample() = runBlocking {
        // This will actually wait for 1 second
        val elapsedTime = waitOneSecond()
        assertTrue("Real time should pass", elapsedTime >= 1000)
    }

    @Test
    fun runTestExample() = runTest {
        // This completes instantly, no real time passes
        val elapsedTime = waitOneSecond()
        assertTrue("No real time should pass", elapsedTime < 1000)
    }

    // Simple flow that emits numbers with delays
    private fun numberFlow() = flow {
        emit(1)
        delay(1000)
        emit(2)
        delay(1000)
        emit(3)
    }

    @Test
    fun flowWithoutTimeControl() = runTest {
        val numbers = mutableListOf<Int>()
        numberFlow().collect { numbers.add(it) }
        assertEquals(listOf(1, 2, 3), numbers)
    }

    @Test
    fun flowWithTimeControl() = runTest {
        val numbers = mutableListOf<Int>()
        
        // Start collecting
        val job = launch {
            numberFlow().collect { numbers.add(it) }
        }
        advanceTimeBy(500)
        // First emission is immediate
        assertEquals(listOf(1), numbers)

        // Advance time to get second emission
        advanceTimeBy(600)
        assertEquals(listOf(1, 2), numbers)

        // Advance time to get third emission
        advanceTimeBy(1000)
        assertEquals(listOf(1, 2, 3), numbers)

        job.cancel()
    }
} 
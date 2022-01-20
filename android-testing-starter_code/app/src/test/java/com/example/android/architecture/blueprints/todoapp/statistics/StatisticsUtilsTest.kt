package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsUtilsTest {
    // 100% active tasks and 0% completed tasks
    @Test
    //subjectUnderTest_actionOrInput_resultState
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {
        // GIVEN/ ARRANGE
        // Create an active tasks (the false makes this active)
        val tasks = listOf<Task>(
            Task("title", "desc", isCompleted = false)
        )
        // WHEN/ACT
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // THEN/ASSERT
        // Check the result
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(100f))

        assertEquals(100f, result.activeTasksPercent)
    }

    // 40% completed tasks and 60% active tasks
    @Test
    fun getActiveAndCompletedStats_both_returnsFortySixty() {

        // Create an active tasks (the false makes this active)
        val tasks = listOf<Task>(
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false)
        )
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZero() {

        // Create an active tasks (the false makes this active)
        val tasks = emptyList<Task>()
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_error_returnsZero() {

        // Create an active tasks (the false makes this active)
        val tasks = null
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }


}
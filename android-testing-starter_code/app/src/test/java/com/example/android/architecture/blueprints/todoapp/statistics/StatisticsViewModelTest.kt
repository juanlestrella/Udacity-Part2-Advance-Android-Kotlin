package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import getOrAwaitValue
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StatisticsViewModelTest {

    // execute each task synchronously
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    // set the main coroutines dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    // subject under test
    private lateinit var statisticsViewModel: StatisticsViewModel
    // fake repo to be injected into the viewmodel
    private lateinit var tasksRepository: FakeTestRepository


    @Before
    fun setupStatisticsViewModel() {
        // Init the repo with no tasks
        tasksRepository = FakeTestRepository()

        statisticsViewModel = StatisticsViewModel(tasksRepository)
    }

    @Test
    fun loadTasks_loading() {
        //  Pause dispatcher so you can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // load the task in the viewmodel
        statisticsViewModel.refresh()

        // then assert that the progress indicator is shown
        assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(true))

        // execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        // then assert that the progress indicator is hidden
        assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun loadStatisticsWhenTasksAreUnavailable_callErrorToDisplay() {
        // Make repo return errors
        tasksRepository.setReturnError(true)
        statisticsViewModel.refresh()

        // then empty and error are true
        assertThat(statisticsViewModel.empty.getOrAwaitValue(), `is`(true))
        assertThat(statisticsViewModel.error.getOrAwaitValue(), `is`(true))
    }
}
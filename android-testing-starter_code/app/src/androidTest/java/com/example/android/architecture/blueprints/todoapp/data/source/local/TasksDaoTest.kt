package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {
    // execute each tasks synchronously using architecture components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun initDb() {
        // inMemoryDatabase is stored here and disappears when process is killed
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ToDoDatabase::class.java
        ).allowMainThreadQueries().build()
    }
    @After
    fun closeDb() = database.close()

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        // GIVEN - Insert a task
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        // WHEN - Get the task by id from the database
        val loaded = database.taskDao().getTaskById(task.id)

        // THEN - The loaded data contains the expected values
        assertThat(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(task.id))
        assertThat(loaded.title, `is`(task.title))
        assertThat(loaded.description, `is`(task.description))
        assertThat(loaded.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun updateTaskAndGetById() = runBlockingTest {
        // INSERT a task into the DAO
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        // UPDATE the task by creating a new task with the same ID
        // different attributes.
        val newTask = Task("title2", "description2", true, task.id)
        database.taskDao().updateTask(newTask)
        val updated = database.taskDao().getTaskById(newTask.id)

            // CHECK that when you get the task by its ID, it has the updated values
        assertThat(updated as Task, notNullValue())
        assertThat(updated.id, `is`(newTask.id))
        assertThat(updated.title, `is`(newTask.title))
        assertThat(updated.description, `is`(newTask.description))
        assertThat(updated.isCompleted, `is`(true))
    }
}
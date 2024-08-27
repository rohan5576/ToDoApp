import com.example.todoapp.data.local.entity.ToDoEntity
import com.example.todoapp.data.repository.ToDoRepository
import com.example.todoapp.domain.usecases.AddToDoUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class AddToDoUseCaseTest {

    private lateinit var addToDoUseCase: AddToDoUseCase
    private val todoRepository: ToDoRepository = mock(ToDoRepository::class.java)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        addToDoUseCase = AddToDoUseCase(todoRepository)
    }

    @Test
    fun `execute should call addTodo on repository`() = runBlocking {
        val todo = ToDoEntity(id = 1, "Test Todo")

        addToDoUseCase.execute(todo)
        verify(todoRepository).addTodo(todo)
    }
}

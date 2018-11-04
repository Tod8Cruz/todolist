package sheep.todolist.domain;

import org.junit.Before;
import org.junit.Test;
import sheep.todolist.dto.ToDoDTO;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class ToDoTest {
    private final static Logger logger = Logger.getGlobal();
    private ToDo toDo;

    @Before
    public void setUp() throws Exception {
        toDo = ToDo.builder()
                .title("title")
                .content("content")
                .endDate(LocalDateTime.now())
                .priority(0)
                .build();
    }

    @Test
    public void update_성공() {
        ToDoDTO toDoDTO = ToDoDTO.builder()
                .title("title")
                .content("contentToDo")
                .endDate(LocalDateTime.now())
                .priority(0)
                .build();

        assertThat(toDo.update(toDoDTO).getContent()).isEqualTo(toDoDTO.getContent());
    }

    @Test
    public void changeToComplete_성공() {
        toDo.changeToComplete();
        assertThat(toDo.getIsCompleted()).isTrue();
    }
}

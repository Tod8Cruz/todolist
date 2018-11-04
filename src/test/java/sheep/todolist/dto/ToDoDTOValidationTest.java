package sheep.todolist.dto;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class ToDoDTOValidationTest {
    private final static Logger logger = Logger.getGlobal();
    private static Validator validator;
    private ToDoDTO toDoDTO;

    @Before
    public void setUp() throws Exception {
        toDoDTO = ToDoDTO.builder()
                .title("title")
                .content("content")
                .endDate(LocalDateTime.now())
                .priority(0)
                .build();
    }

    @BeforeClass
    public static void setup() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void titleAndContent_null() {
        toDoDTO.setTitle(null);
        toDoDTO.setContent(null);
        Set<ConstraintViolation<ToDoDTO>> constraintViolations = validator.validate(toDoDTO);
        assertThat(constraintViolations.size()).isEqualTo(2);
    }
}

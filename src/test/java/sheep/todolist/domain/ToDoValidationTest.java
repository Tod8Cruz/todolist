package sheep.todolist.domain;

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

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ToDoValidationTest {
    private final static Logger logger = Logger.getGlobal();
    private static Validator validator;
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

    @BeforeClass
    public static void setup() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void titleAndContent_null() {
        toDo.setTitle(null);
        toDo.setContent(null);
        Set<ConstraintViolation<ToDo>> constraintViolations = validator.validate(toDo);
        assertThat(constraintViolations.size()).isEqualTo(2);
    }
}

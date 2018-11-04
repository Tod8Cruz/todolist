package sheep.todolist.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.*;
import sheep.todolist.dto.ToDoDTO;
import sheep.todolist.utils.RestResponse;
import support.AcceptanceTest;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiToDoAcceptanceTest extends AcceptanceTest {

    private final static Logger logger = Logger.getGlobal();

    private ToDoDTO todoDTO;

    @Before
    public void setUp() throws Exception {
        todoDTO = ToDoDTO.builder()
                .title("title")
                .content("content")
                .priority(0)
                .endDate(LocalDateTime.now())
                .build();
    }

    // 예외 케이스s are in 단위 테스트
    @Test
    public void createToDo_성공() {
        ResponseEntity<RestResponse> response = template.postForEntity("/api/todo", todoDTO, RestResponse.class);
        logger.info("createTodo 성공 : " + response.getHeaders().getLocation().getPath());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }


    @Test
    public void getToDos_성공() {
        ResponseEntity<RestResponse> response = template.getForEntity("/api/todo", RestResponse.class);
        logger.info("getToDos 성공 : " + response.getBody().getData());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void changeTomComplete_성공() {
        ResponseEntity<Void> response = template.postForEntity("/api/todo", todoDTO, Void.class);
        logger.info("createTodo 성공 : " + response.getHeaders().getLocation().getPath());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath().split("/")[2];
        ResponseEntity<RestResponse> responseEntity = template.getForEntity("/api/todo/complete/" + location, RestResponse.class);

        logger.info("change 성공 : {}" + responseEntity.getBody().getData());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateToDo_성공() {
        ResponseEntity<Void> response = template.postForEntity("/api/todo", todoDTO, Void.class);
        logger.info("createTodo 성공 : " + response.getHeaders().getLocation().getPath());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath();

        todoDTO.setContent("changedContent");

        ResponseEntity<RestResponse> responseEntity = template.exchange("/api" + location, HttpMethod.PUT, createHttpEntity(todoDTO), RestResponse.class);
        logger.info("updateToDo 성공 : " + responseEntity.getBody().getData());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void deleteToDo_성공() {
        ResponseEntity<Void> response = template.postForEntity("/api/todo", todoDTO, Void.class);
        logger.info("createTodo 성공 : " + response.getHeaders().getLocation().getPath());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath();

        ResponseEntity<Void> responseEntity = template.exchange("/api" + location, HttpMethod.DELETE, null, Void.class);

        logger.info("delete 성공 : {}" + responseEntity);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}

package sheep.todolist.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sheep.todolist.dto.ToDoDTO;
import sheep.todolist.service.ToDoService;
import sheep.todolist.utils.RestResponse;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;

@RestController
@Slf4j
@RequestMapping("/api/todo")
public class ApiToDoController {

    @Resource(name = "toDoService")
    private ToDoService toDoService;

    @PostMapping
    public ResponseEntity<Void> createToDo(@Valid @RequestBody ToDoDTO toDoDTO) {
        return ResponseEntity.created(URI.create("/todo/" + toDoService.createToDO(toDoDTO).getId())).build();
    }

    @GetMapping
    public ResponseEntity<RestResponse> getToDos() {
        return ResponseEntity.ok(RestResponse.success(toDoService.getToDos()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse> updateToDo(@PathVariable Long id, @RequestBody ToDoDTO toDoDTO) {
        log.info("update controller : "+id);
        return ResponseEntity.ok(RestResponse.success(toDoService.updateToDO(id, toDoDTO)));
    }

    @DeleteMapping("/{id}")
    public String deleteToDo(@PathVariable Long id) {
        toDoService.deleteToDO(id);
        log.info("delete controller 출력 : "+String.valueOf(id));
        return String.valueOf(id);
    }

}

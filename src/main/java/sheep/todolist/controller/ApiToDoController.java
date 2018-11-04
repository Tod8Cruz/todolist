package sheep.todolist.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sheep.todolist.domain.ToDo;
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
    public ResponseEntity<RestResponse> createToDo(@Valid @RequestBody ToDoDTO toDoDTO) {
        ToDo toDo = toDoService.createToDO(toDoDTO);
        return ResponseEntity.created(URI.create("/todo/" + toDo.getId())).body(RestResponse.success(toDo));
    }

    @GetMapping
    public ResponseEntity<RestResponse> getToDos() {
        return ResponseEntity.ok(RestResponse.success(toDoService.getToDos()));
    }

    @GetMapping("/complete/{id}")
    public ResponseEntity<RestResponse> changeToComplete(@PathVariable Long id) {
        return ResponseEntity.ok(RestResponse.success(toDoService.changeToComplete(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse> updateToDo(@PathVariable Long id, @RequestBody ToDoDTO toDoDTO) {
        return ResponseEntity.ok(RestResponse.success(toDoService.updateToDO(id, toDoDTO)));
    }

    @DeleteMapping("/{id}")
    public String deleteToDo(@PathVariable Long id) {
        toDoService.deleteToDO(id);
        return String.valueOf(id);
    }

}

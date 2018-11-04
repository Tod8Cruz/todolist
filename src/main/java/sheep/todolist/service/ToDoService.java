package sheep.todolist.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sheep.todolist.domain.ToDo;
import sheep.todolist.domain.ToDoRepository;
import sheep.todolist.dto.ToDoDTO;
import sheep.todolist.exception.NotAllowedException;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
public class ToDoService {

    @Resource(name = "toDoRepository")
    private ToDoRepository toDoRepository;

    public ToDo createToDO(ToDoDTO toDoDTO) {
        return toDoRepository.save(toDoDTO.toToDo());
    }

    public List<ToDo> getToDos() {
        return toDoRepository.findAll();
    }

    public ToDo updateToDO(Long id, ToDoDTO toDoDTO) {
        ToDo toDo = toDoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if(toDoRepository.existsByIsCompletedFalseAndAndPriority(toDoDTO.getPriority())){
            throw new NotAllowedException("우선순위가 존재 합니다");
        }
        return toDoRepository.save(toDo.update(toDoDTO));
    }

    public void deleteToDO(Long id) {
        ToDo toDo = toDoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        toDoRepository.delete(toDo);
    }

    public ToDo changeToComplete(Long id) {
        ToDo toDo = toDoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        toDo.changeToComplete();
        return toDoRepository.save(toDo);
    }
}

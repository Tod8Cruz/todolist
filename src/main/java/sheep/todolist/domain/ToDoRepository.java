package sheep.todolist.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    boolean existsByIsCompletedFalseAndAndPriority(int priority);
}

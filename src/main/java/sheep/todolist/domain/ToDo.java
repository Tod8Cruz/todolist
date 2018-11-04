package sheep.todolist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import sheep.todolist.dto.ToDoDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String content;

    @Column
    private int priority;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column
    private LocalDateTime endDate;

    @Column
    private Boolean isCompleted = false;

    public ToDo update(ToDoDTO toDoDTO) {
        title = toDoDTO.getTitle();
        content = toDoDTO.getContent();
        priority = toDoDTO.getPriority();
        endDate = toDoDTO.getEndDate();
        isCompleted = toDoDTO.getIsCompleted();
        return this;
    }

    public void changeToComplete() {
        isCompleted = true;
    }
}

package sheep.todolist.dto;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import sheep.todolist.domain.ToDo;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ToDoDTO {

    @NotNull
    private String title;

    @NotNull
    private String content;

    private int priority;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    private Boolean isCompleted = false;

    public ToDo toToDo() {
        return ToDo.builder()
                .title(title)
                .content(content)
                .priority(priority)
                .endDate(endDate)
                .isCompleted(false)
                .build();
    }
}

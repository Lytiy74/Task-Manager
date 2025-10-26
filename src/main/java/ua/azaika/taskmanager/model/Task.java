package ua.azaika.taskmanager.model;

import lombok.*;

import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Integer id;
    private String title;
    private String description;
    private List<User> users;
}

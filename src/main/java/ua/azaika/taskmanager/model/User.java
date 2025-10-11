package ua.azaika.taskmanager.model;

import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String userName;
    private String email;
    private String password;
}

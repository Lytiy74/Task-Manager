package ua.azaika.taskmanager.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@ToString
@Getter
@Setter
public class User {
    private Integer id;
    private String userName;
    private String email;
    private String password;
}

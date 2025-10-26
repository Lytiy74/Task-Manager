package ua.azaika.taskmanager.mapper.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.azaika.taskmanager.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .userName(rs.getString("name"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .build();
    }
}


package ua.azaika.taskmanager.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.azaika.taskmanager.mapper.jdbc.UserRowMapper;
import ua.azaika.taskmanager.model.User;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    public static final String INSERT_USER_QUERY = """
            INSERT INTO users (name, email, password)
            VALUES (?,?,?)
            """;
    public static final String SELECT_FROM_USERS_WHERE_ID = """
            SELECT * FROM users
            WHERE id = ?
            """;
    public static final String DELETE_FROM_USERS_WHERE_ID = "DELETE FROM users WHERE id = ?";
    public static final String DELETE_USER_TASKS = "DELETE FROM user_task WHERE user_id = ?";
    public static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
    public static final String UPDATE_USERS_QUERY = """
            UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?
            """;

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_USER_QUERY, new String[]{"id"});
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    public List<User> getAll() {
        return jdbcTemplate.query(SELECT_ALL_USERS_QUERY, userRowMapper);
    }

    public User findById(Integer id) {
        return jdbcTemplate.queryForObject(SELECT_FROM_USERS_WHERE_ID,
                new Object[]{id}, new int[]{Types.INTEGER}, userRowMapper);
    }

    public User update(Integer id, User updates) {
        jdbcTemplate.update(
                UPDATE_USERS_QUERY,
                updates.getUserName(), updates.getEmail(), updates.getPassword(), id);
        return findById(id);
    }

    public void deleteById(Integer id) {
        jdbcTemplate.update(DELETE_USER_TASKS, id);
        jdbcTemplate.update(DELETE_FROM_USERS_WHERE_ID, id);
    }
}
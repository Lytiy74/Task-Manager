
package ua.azaika.taskmanager.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.azaika.taskmanager.model.Task;
import ua.azaika.taskmanager.model.User;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskRepository {
    public static final String SQL_INSERT_TASK = """
            INSERT INTO tasks (name, description)
            VALUES (?, ?)
            """;
    public static final String SQL_INSERT_TASK_USER = """
            INSERT INTO user_task (task_id, user_id)
            VALUES (?,?)
            """;
    public static final String SQL_DELETE_TASK_USERS = """
            DELETE FROM user_task WHERE task_id = ?
            """;
    public static final String SELECT_ALL_TASKS_QUERY = "SELECT * FROM tasks";
    public static final String SELECT_TASK_BY_ID_QUERY = "SELECT * FROM tasks WHERE tasks.id = ?";
    public static final String DELETE_TASK_QUERY = "DELETE FROM tasks WHERE tasks.id = ?";
    public static final String UPDATE_TASK_QUERY = "UPDATE tasks SET name = ?, description = ? WHERE tasks.id = ?";

    private final RowMapper<Task> taskRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public Task save(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    SQL_INSERT_TASK,
                    new String[]{"id"});
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            return ps;
        }, keyHolder);
        task.setId(keyHolder.getKey().intValue());

        if (task.getUsers() != null && !task.getUsers().isEmpty()) {
            for (User user : task.getUsers()) {
                jdbcTemplate.update(SQL_INSERT_TASK_USER, task.getId(), user.getId());
            }
        }
        return task;
    }

    public List<Task> getAll() {
        return jdbcTemplate.query(SELECT_ALL_TASKS_QUERY, taskRowMapper);
    }

    public Task getById(Integer id) {
        return jdbcTemplate.queryForObject(
                SELECT_TASK_BY_ID_QUERY,
                new Object[]{id}, new int[]{Types.INTEGER}, taskRowMapper
        );
    }

    public void deleteById(Integer id) {
        jdbcTemplate.update(SQL_DELETE_TASK_USERS, id);
        jdbcTemplate.update(DELETE_TASK_QUERY, id);
    }

    public Task update(Integer id, Task updates) {
        jdbcTemplate.update(
                UPDATE_TASK_QUERY,
                updates.getTitle(), updates.getDescription(), id);
        return getById(id);
    }
}
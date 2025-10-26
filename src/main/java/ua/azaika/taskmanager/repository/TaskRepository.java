package ua.azaika.taskmanager.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.azaika.taskmanager.mapper.jdbc.UserRowMapper;
import ua.azaika.taskmanager.model.Task;
import ua.azaika.taskmanager.model.User;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskRepository {
    public static final String SQL_INSERT_TASK = """
            INSERT INTO tasks (title, description)
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
    public static final String SELECT_TASK_BY_ID_QUERY = "SELECT * FROM tasks WHERE id = ?";
    public static final String SELECT_USERS_BY_TASK_ID = """
            SELECT u.* FROM users u
            INNER JOIN user_task ut ON u.id = ut.user_id
            WHERE ut.task_id = ?
            """;
    public static final String DELETE_TASK_QUERY = "DELETE FROM tasks WHERE id = ?";
    public static final String UPDATE_TASK_QUERY = "UPDATE tasks SET title = ?, description = ? WHERE id = ?";

    private final RowMapper<Task> taskRowMapper;
    private final UserRowMapper userRowMapper;
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
        return getById(task.getId());
    }

    public List<Task> getAll() {
        List<Task> tasks = jdbcTemplate.query(SELECT_ALL_TASKS_QUERY, taskRowMapper);
        tasks.forEach(this::loadUsers);
        return tasks;
    }

    public Task getById(Integer id) {
        Task task = jdbcTemplate.queryForObject(
                SELECT_TASK_BY_ID_QUERY,
                new Object[]{id},
                new int[]{Types.INTEGER},
                taskRowMapper
        );
        if (task != null) {
            loadUsers(task);
        }
        return task;
    }

    private void loadUsers(Task task) {
        List<User> users = jdbcTemplate.query(
                SELECT_USERS_BY_TASK_ID,
                new Object[]{task.getId()},
                new int[]{Types.INTEGER},
                userRowMapper
        );
        task.setUsers(users);
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
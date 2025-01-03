package org.example.dao;

import jakarta.validation.Valid;
import org.example.Entity.TodoEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private final String databaseUrl;
    private final String databaseUser;
    private final String databasePassword;

    public TaskDAO(String databaseUrl, String databaseUser, String databasePassword) {
        this.databaseUrl = databaseUrl;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
    }

    public List<TodoEntity> getAllTasks() throws SQLException {
        List<TodoEntity> tasks = new ArrayList<>();
        String query = "SELECT * FROM todo ";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {

                TodoEntity todo = new TodoEntity();
                todo.setId(resultSet.getInt("id"));
                todo.setName(resultSet.getString("name")); // Corrected casing for column name
                todo.setDescription(resultSet.getString("description")); // Corrected casing for column name
                todo.setStart(resultSet.getDate("Start").toLocalDate());
                todo.setEnd(resultSet.getDate("End").toLocalDate());
                todo.setStatus(resultSet.getInt("Status"));
                tasks.add(todo);
            }
            System.out.println("Tasks size: " + tasks.size()); // Updated debug statement for clarity
        }
        return tasks;
    }
    public TodoEntity getTaskById(int taskId) throws SQLException {
        TodoEntity todo = null;
        String query = "SELECT * FROM todo WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    todo = new TodoEntity();
                    todo.setId(resultSet.getInt("id"));
                    todo.setName(resultSet.getString("name"));
                    todo.setDescription(resultSet.getString("description"));
                    todo.setStart(resultSet.getDate("Start").toLocalDate());
                    todo.setEnd(resultSet.getDate("End").toLocalDate());
                    todo.setStatus(resultSet.getInt("Status"));
                }
            }
        }
        return todo;
    }

    public void save(TodoEntity task) throws SQLException {
        String query = "INSERT INTO todo (id,name, description, Start, End, Status) VALUES (?,?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, task.getid());
            statement.setString(2, task.getName());
            statement.setString(3, task.getDescription());
            statement.setDate(4, java.sql.Date.valueOf(task.getStart()));
            statement.setDate(5, java.sql.Date.valueOf(task.getEnd()));
            statement.setInt(6, 0);

            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");
        }
    }
    public void delete(int taskId) throws SQLException {
        String query = "DELETE FROM todo WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, taskId);

            int rowsDeleted = statement.executeUpdate();
            System.out.println(rowsDeleted + " row(s) deleted.");
        }
    }
    public void update(int taskId, TodoEntity updatedTask) throws SQLException {
        String query = "UPDATE todo SET name = ?, description = ?, Start = ?, End = ?, Status = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, updatedTask.getName());
            statement.setString(2, updatedTask.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(updatedTask.getStart()));
            statement.setDate(4, java.sql.Date.valueOf(updatedTask.getEnd()));
            statement.setInt(5, updatedTask.getStatus());
            statement.setInt(6, taskId);

            int rowsUpdated = statement.executeUpdate();
            System.out.println(rowsUpdated + " row(s) updated.");
        }
    }


}

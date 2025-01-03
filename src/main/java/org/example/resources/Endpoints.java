package org.example.resources;

import io.dropwizard.hibernate.UnitOfWork;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dao.TaskDAO;
import org.example.Entity.TodoEntity;

import java.sql.SQLException;
import java.util.List;

@Path("/todo")
@Produces(MediaType.APPLICATION_JSON)
public class Endpoints {
    private final TaskDAO TaskDAO;

    public Endpoints(TaskDAO TaskDAO) {
        this.TaskDAO = TaskDAO;
    }

    @GET
    @Path("/allTasks")
    @UnitOfWork
    public Response getAllTasks() {
        try {
            // Fetch all tasks from the database
            List<TodoEntity> tasks = TaskDAO.getAllTasks();

            // If no tasks are found, return a 200 OK with an empty list
            if (tasks.isEmpty()) {
                return Response.status(Response.Status.OK)
                        .entity("No tasks found")
                        .build();
            }

            // Return the tasks with a 200 OK response
            return Response.status(Response.Status.OK)
                    .entity(tasks)
                    .build();
        } catch (Exception e) {
            // Handle unexpected errors (e.g., database issues)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving tasks: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/task")
    @UnitOfWork
    public Response getATasks(@QueryParam("id") int id) {
        // Validate the ID parameter
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid task ID provided")
                    .build();
        }

        try {
            // Fetch the task from the database
            TodoEntity task = TaskDAO.getTaskById(id);

            // If no task is found, return a 404 Not Found response
            if (task == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Task with ID " + id + " not found")
                        .build();
            }

            // Return the task with a 200 OK response
            return Response.status(Response.Status.OK)
                    .entity(task)
                    .build();
        } catch (Exception e) {
            // Handle unexpected errors (e.g., database issues)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving task with ID " + id + ": " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/newTask")
    @UnitOfWork
    public Response createTask(@Valid TodoEntity todoEntity) {
        // Check if the TodoEntity is valid
        if (todoEntity == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Task data is missing or invalid")
                    .build();
        }

        try {
            // Save the new task to the database
            TaskDAO.save(todoEntity);

            // Return a successful response with the created entity
            return Response.status(Response.Status.CREATED)
                    .entity("Task created successfully with ID " + todoEntity.getid())
                    .build();
        } catch (Exception e) {
            // Handle any database or unexpected errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating task: " + e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Path("/updateTask/{id}")
    @UnitOfWork
    public Response updateATasks(@PathParam("id") int id, TodoEntity updatedEntity) throws SQLException {
        // Validate the ID and the updated entity input
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid task ID provided")
                    .build();
        }

        if (updatedEntity == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No data provided for update")
                    .build();
        }

        // Fetch the existing entity from the database
        TodoEntity presentEntity = TaskDAO.getTaskById(id);

        if (presentEntity == null) {
            // Return a 404 if the task with the given ID doesn't exist
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Task with ID " + id + " not found")
                    .build();
        }

        // Update only fields that are not null in the updated entity
        boolean updated = false;
        if (updatedEntity.getName() != null) {
            presentEntity.setName(updatedEntity.getName());
            updated = true;
        }
        if (updatedEntity.getDescription() != null) {
            presentEntity.setDescription(updatedEntity.getDescription());
            updated = true;
        }
        if (updatedEntity.getStart() != null) {
            presentEntity.setStart(updatedEntity.getStart());
            updated = true;
        }
        if (updatedEntity.getEnd() != null) {
            presentEntity.setEnd(updatedEntity.getEnd());
            updated = true;
        }
        if (updatedEntity.getStatus() != null) {
            presentEntity.setStatus(updatedEntity.getStatus());
            updated = true;
        }

        if (!updated) {
            // If no fields were updated, return a 400 Bad Request response
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No valid fields provided for update")
                    .build();
        }

        try {
            // Save the updated entity back to the database
            TaskDAO.update(id,presentEntity); // Update the existing entity, not the incoming one

            return Response.status(Response.Status.OK)
                    .entity("Task updated successfully")
                    .build();
        } catch (Exception e) {
            // Handle unexpected errors during the update operation
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating task with ID " + id + ": " + e.getMessage())
                    .build();
        }
    }


    @DELETE
    @Path("/removeTask")
    @UnitOfWork
    public Response removeTask(@QueryParam("id") int id) throws SQLException {
        // Check if the task exists before attempting to delete it
        TodoEntity presentEntity = TaskDAO.getTaskById(id);

        if (presentEntity == null) {
            // Return a 404 if the task with the given ID doesn't exist
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Task with ID " + id + " not found")
                    .build();
        }

        try {
            // Proceed with deletion
            TaskDAO.delete(id);

            // Return a success response
            return Response.status(Response.Status.OK)
                    .entity("Task with ID " + id + " has been successfully deleted")
                    .build();
        } catch (Exception e) {
            // Handle any errors that occur during the deletion process
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting task with ID " + id + ": " + e.getMessage())
                    .build();
        }
    }

}

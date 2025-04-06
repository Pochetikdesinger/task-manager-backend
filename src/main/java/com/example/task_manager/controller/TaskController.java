package com.example.task_manager.controller;

import com.example.task_manager.model.Task;
import com.example.task_manager.model.User;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para manejar las operaciones CRUD de tareas.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository; // Agregamos UserRepository para buscar el usuario

    /**
     * Crea una nueva tarea asociada al usuario autenticado.
     *
     * @param task           La tarea a crear.
     * @param authentication La autenticación del usuario.
     * @return ResponseEntity con la tarea creada o un error si la solicitud es inválida.
     */
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task, Authentication authentication) {
        // Validar la tarea
        if (task == null || task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Task title is required");
        }

        // Obtener el nombre de usuario del principal
        String username = authentication.getName(); // Esto devuelve el username (testuser2)

        // Buscar el usuario en la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }

    /**
     * Obtiene todas las tareas del usuario autenticado.
     *
     * @param authentication La autenticación del usuario.
     * @return ResponseEntity con la lista de tareas.
     */
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        List<Task> tasks = taskRepository.findByUser(user);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Actualiza una tarea existente.
     *
     * @param id             El ID de la tarea a actualizar.
     * @param taskDetails    Los detalles actualizados de la tarea.
     * @param authentication La autenticación del usuario.
     * @return ResponseEntity con la tarea actualizada o un error si no se encuentra o no está autorizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task taskDetails, Authentication authentication) {
        // Validar los detalles de la tarea
        if (taskDetails == null || taskDetails.getTitle() == null || taskDetails.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Task title is required");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        // Verificar que el usuario sea el propietario de la tarea
        if (!task.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this task");
        }

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());
        Task updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Elimina una tarea existente.
     *
     * @param id             El ID de la tarea a eliminar.
     * @param authentication La autenticación del usuario.
     * @return ResponseEntity con un mensaje de éxito o un error si no se encuentra o no está autorizado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        // Verificar que el usuario sea el propietario de la tarea
        if (!task.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this task");
        }

        taskRepository.delete(task);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
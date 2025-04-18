# 🗂️ Task Manager Backend

Este es el backend del sistema **Task Manager**, desarrollado con **Spring Boot**, orientado a la gestión de usuarios y tareas pendientes. Ideal para integrarse con un frontend web o móvil.

---

## 🚀 Tecnologías

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven

---

## 🛠️ Endpoints principales (ejemplo)

| Método | Endpoint         | Descripción                |
|--------|------------------|----------------------------|
| GET    | `/api/users`     | Listar todos los usuarios  |
| POST   | `/api/users`     | Crear un nuevo usuario     |
| GET    | `/api/tasks`     | Obtener todas las tareas   |
| POST   | `/api/tasks`     | Crear una nueva tarea      |

> Puedes probarlo con Postman o cualquier cliente REST.

---

## 🧰 Configuración

Asegúrate de tener tu base de datos MySQL corriendo y un archivo `application.properties` con tu configuración, por ejemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_manager_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
﻿# task-manager-backend

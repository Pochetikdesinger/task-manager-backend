package com.example.task_manager.controller;

/**
 * Excepción personalizada para manejar recursos no encontrados.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
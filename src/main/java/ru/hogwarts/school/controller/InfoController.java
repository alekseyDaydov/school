package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;

public interface InfoController {
    ResponseEntity<String> getPort();
}

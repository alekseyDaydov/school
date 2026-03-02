package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;

public interface InfoController {
    ResponseEntity<Integer> getPort();
}

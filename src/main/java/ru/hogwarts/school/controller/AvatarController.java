package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.AvatarService;

@RestController
@RequestMapping("avatar")
public class AvatarController {
    @Autowired
    private AvatarService avatarService;

    @GetMapping
    public String add() {
        return null;
    }
}

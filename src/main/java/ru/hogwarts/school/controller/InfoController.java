package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.AvatarService;

@RestController
public class InfoController {
    String CREATE_TEXT_INFO = "Was invoked method for {}";
    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @GetMapping("/port")
    public ResponseEntity<Integer> getPort(){
        logger.info(CREATE_TEXT_INFO, "getPort");
        Integer port = 8080;
        return ResponseEntity.ok(port);
    }
}

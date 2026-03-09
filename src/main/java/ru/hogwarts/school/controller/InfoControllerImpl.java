package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.AvatarService;

@RestController
public class InfoControllerImpl implements InfoController {
    @Value("${constant.text}")
    private String create_text;

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @GetMapping("/port")
    public ResponseEntity<String> getPort() {
        logger.info(create_text, "get port", serverPort, activeProfile);
        return ResponseEntity.ok(serverPort);
    }
}

package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.InfoService;

@RestController
public class InfoControllerImpl implements InfoController {
    private InfoService infoService;

    public InfoControllerImpl(InfoService infoService) {
        this.infoService = infoService;
    }
    @Value("${weather-forecast-service.url}")
    private String url;
    @Value("${constant.text}")
    private String create_text;
    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @GetMapping("/port")
    public ResponseEntity<Integer> getPort() {
        logger.info(create_text, "get port");
        Integer port = infoService.getPort();
        return ResponseEntity.ok(port);
    }
}

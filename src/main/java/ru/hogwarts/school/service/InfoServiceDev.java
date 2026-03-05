package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class InfoServiceDev implements  InfoService{
    @Value("${server.port}")
    private int port;
    @Override
    public Integer getPort() {
        return port;
    }
}

package ru.hogwarts.school.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("productive")
public class InfoServiceProd implements InfoService{
    @Override
    public Integer getPort() {
        return 0;
    }
}

package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.AvatarRepository;

import java.util.Collection;

@Service
public class AvatarService {
    private AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }
    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    String CREATE_TEXT_INFO = "Was invoked method for {}";
    public Collection<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize) {
        logger.info(CREATE_TEXT_INFO, "getAllAvatars" );
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}

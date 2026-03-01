package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.List;

@Service
public class FacultyService {
    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    String CREATE_TEXT_INFO = "Was invoked method for {}";
    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    //    Create
    public Faculty createFaculty(Faculty faculty) {
        logger.info(CREATE_TEXT_INFO, "createFaculty");
        return facultyRepository.save(faculty);
    }

    //    read
    public Faculty findFaculty(long id) {
        logger.info(CREATE_TEXT_INFO, "findFaculty");
        return facultyRepository.findById(id).orElse(null);
    }

    //update
    public Faculty updateFaculty(Faculty faculty) {
        logger.info(CREATE_TEXT_INFO, "updateFaculty");
        return facultyRepository.save(faculty);
    }

    //delete
    public void deleteFaculty(long id) {
        logger.info(CREATE_TEXT_INFO, "deleteFaculty");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> filterColor(String color) {
        logger.info(CREATE_TEXT_INFO, "filterColor");
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getAll() {
        logger.info(CREATE_TEXT_INFO, "getAll");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByNameContainsIgnoreCaseOrColorIgnoreCase(String find) {
        logger.info(CREATE_TEXT_INFO, "findByNameContainsIgnoreCaseOrColorIgnoreCase");
        return facultyRepository.findFacultyByNameContainingIgnoreCaseOrColorContainingIgnoreCase(find, find);
    }
}

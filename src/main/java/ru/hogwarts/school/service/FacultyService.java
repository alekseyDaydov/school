package ru.hogwarts.school.service;

import org.apache.logging.log4j.LogManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
@Service
@Slf4j
public class FacultyService {

    String CREATE_TEXT_INFO = "Was invoked method for {}";
    String CREATE_TEXT_ERROR = "There is not student with id = {}";

    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    //    Create
    public Faculty createFaculty(Faculty faculty) {
        log.info(CREATE_TEXT_INFO, "createFaculty");
        return facultyRepository.save(faculty);
    }

    //    read
    public Faculty findFaculty(long id) {
        log.info(CREATE_TEXT_INFO, "findFaculty");
        log.error(CREATE_TEXT_ERROR, id);
        return facultyRepository.findById(id).orElse(null);
    }

    //update
    public Faculty updateFaculty(Faculty faculty) {
        log.info(CREATE_TEXT_INFO, "updateFaculty");
        return facultyRepository.save(faculty);
    }

    //delete
    public void deleteFaculty(long id) {
        log.info(CREATE_TEXT_INFO, "deleteFaculty");
        log.error(CREATE_TEXT_ERROR, id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> filterColor(String color) {
        log.info(CREATE_TEXT_INFO, "filterColor");
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getAll() {
        log.info(CREATE_TEXT_INFO, "getAll");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByNameContainsIgnoreCaseOrColorIgnoreCase(String find) {
        log.info(CREATE_TEXT_INFO, "findByNameContainsIgnoreCaseOrColorIgnoreCase");
        return facultyRepository.findFacultyByNameContainingIgnoreCaseOrColorContainingIgnoreCase(find, find);
    }
}

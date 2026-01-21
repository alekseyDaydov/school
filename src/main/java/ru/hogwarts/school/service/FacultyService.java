package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.List;

@Service
public class FacultyService {
    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    //    Create
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    //    read
    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    //update
    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    //delete
    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> filterColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByNameContainsIgnoreCaseOrColorIgnoreCase(String find){
        return facultyRepository.findFacultyByNameContainingIgnoreCaseOrColorContainingIgnoreCase(find, find);
    }

    public Collection<Faculty> getById(long id){
        return facultyRepository.findFacultyById(id);
    }
}

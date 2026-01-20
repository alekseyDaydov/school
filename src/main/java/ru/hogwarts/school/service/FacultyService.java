package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    Map<Long, Faculty> faculties = new HashMap<>();
    private long lastId = 0;

    //    Create
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(lastId++);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    //    read
    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    //update
    public Faculty updateFaculty(Faculty faculty) {

        if (!faculties.containsKey(faculty.getId())) {
            return null;
        }
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    //delete
    public Faculty deleteFaculty(long id) {
        return  faculties.remove(id);
    }

    public Collection<Faculty> filterColor(String color) {
        return faculties.entrySet().stream()
                .filter(element -> element.getValue().getColor().equals(color))
                .map(element -> findFaculty(element.getKey()))
                .collect(Collectors.toList());
    }
}

package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
@RequestMapping("student")
@Transactional
public class StudentController {
    private final StudentService studentService;
    private final FacultyService facultyService;
    private int count;
    public Object flag = new Object();

    public StudentController(StudentService studentService, FacultyService facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    @GetMapping("/print-parallel")
    public ResponseEntity<Void> getPrintParallel() {
        StudentController studentController = new StudentController(studentService, facultyService);
        List<String> nameList = studentService.getAll()
                .stream()
                .map(Student::getName)
                .collect(Collectors.toList());

        studentController.printMessageParallel(nameList.get(0), 1);
        studentController.printMessageParallel(nameList.get(1), 2);

        new Thread(() -> {
            studentController.printMessageParallel(nameList.get(2), 3);
            studentController.printMessageParallel(nameList.get(3), 4);
        }).start();

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                studentController.printMessageParallel(nameList.get(4), 5);
                studentController.printMessageParallel(nameList.get(5), 6);
            }
        };
        thread2.start();
        return ResponseEntity.ok().build();
    }

    private void printMessageParallel(String name, int numberThread) {
        System.out.println("name: " + name + ", numberThread= " + numberThread);
        String s = "";
        for (int i = 0; i < 100_000; i++) {
            s += i;
        }
    }

    @GetMapping("/print-synchronized")
    public ResponseEntity<Void> getPrintSynchronized() {
        StudentController studentController = new StudentController(studentService, facultyService);
        List<String> nameList = studentService.getAll()
                .stream()
                .map(Student::getName)
                .collect(Collectors.toList());
        studentController.printMessageSynchronize(nameList.get(0), 1);
        studentController.printMessageSynchronize(nameList.get(1), 2);

        new Thread(() -> {
            studentController.printMessageSynchronize(nameList.get(2), 3);
            studentController.printMessageSynchronize(nameList.get(3), 4);
        }).start();
        Thread thread2 = new Thread() {
            @Override
            public void run() {
                studentController.printMessageSynchronize(nameList.get(4), 5);
                studentController.printMessageSynchronize(nameList.get(5), 6);
            }
        };
        thread2.start();
        return ResponseEntity.ok().build();
    }

    private void printMessageSynchronize(String name, int numberThread) {
        synchronized (flag) {
            System.out.println("name: " + name + ", numberThread= " + numberThread + ", count = " + count);
            count++;
        }
        ;
        String s = "";
        for (int i = 0; i < 100_000; i++) {
            s += i;
        }
    }

    @GetMapping("findSymbol/StreamApi/{symbol}")
    public ResponseEntity<List<String>> getNameSymbol(@PathVariable String symbol) {
        List<String> nameStudent =
                studentService.getAll()
                        .parallelStream()
                        .map(element -> element.getName().toUpperCase())
//                        .map(Student::getName)
//                        .map(String::toUpperCase)
                        .filter(element -> element.startsWith(symbol))
                        .sorted()
                        .collect(Collectors.toList());
        return ResponseEntity.ok(nameStudent);
    }

    @GetMapping(value = "/averageAge/StreamApi")
    public ResponseEntity<Double> getAverageAgeStudentStreamApi() {

        Double average = studentService.getAll()
                .parallelStream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
        return ResponseEntity.ok(average);
    }

    @PostMapping()
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable long id) {
        Student getStudent = studentService.findByStudent(id);
        if (getStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(getStudent);
    }

    @PutMapping()
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student editStudent = studentService.updateStudent(student);
        if (editStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(editStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<Collection<Student>> filterAge(@RequestParam(required = false) Integer age,
                                                         @RequestParam(required = false) Integer min,
                                                         @RequestParam(required = false) Integer max,
                                                         @RequestParam(required = false) Integer idFaculty) {
        if (idFaculty != null && idFaculty.longValue() > 0) {
            Faculty faculty = facultyService.findFaculty(idFaculty);
            faculty.getStudents();
            return ResponseEntity.ok(facultyService.findFaculty(idFaculty).getStudents());
        }
        if (age != null && age.intValue() > 0) {
            return ResponseEntity.ok(studentService.filterAge(age));
        }
        if (min != null && max != null && min.intValue() < max.intValue()) {
            return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
        }
        if (min == null || max == null || min.intValue() > max.intValue()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Student>> getAll() {
        Collection<Student> students = studentService.getAll();
        if (students.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(studentService.getAll());
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }

        studentService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = studentService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = studentService.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping(value = "/averageAge")
    public ResponseEntity<Float> getAverageAgeStudent() {
        Float average = studentService.getAverageAgeStudent();
        if (average == null) {
            average = 0.0F;
        }
        return ResponseEntity.ok(average);
    }

    @GetMapping(value = "/fiveStudent")
    public ResponseEntity<Collection<Student>> getLastFiveStudent() {
        return ResponseEntity.ok(studentService.getLastFiveStudent());
    }

    @GetMapping(value = "/getCount")
    public ResponseEntity<Long> getCountStudent() {
        return ResponseEntity.ok(studentService.getCountStudent());
    }
}

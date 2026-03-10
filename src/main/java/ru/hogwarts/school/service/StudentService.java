package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {
    Logger logger = LoggerFactory.getLogger(StudentService.class);
    String CREATE_TEXT_INFO = "Was invoked method for {}";
    String CREATE_TEXT_ERROR = "There is not student with id = {}";

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    //    Create
    public Student createStudent(Student student) {
        logger.info(CREATE_TEXT_INFO, "createStudent");

        return studentRepository.save(student);
    }

    //    read
    public Student findByStudent(long id) {
        logger.info(CREATE_TEXT_INFO, "findByStudent");
        logger.error(CREATE_TEXT_ERROR, id);
        return studentRepository.findById(id).orElse(null);
    }

    //update
    public Student updateStudent(Student student) {
        logger.info(CREATE_TEXT_INFO, "updateStudent");

        return studentRepository.save(student);
    }

    //delete
    public void deleteStudent(Long id) {
        logger.info(CREATE_TEXT_INFO, "deleteStudent");
        logger.error(CREATE_TEXT_ERROR, id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> filterAge(int age) {
        logger.info(CREATE_TEXT_INFO, "filterAge");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getAll() {
        logger.info(CREATE_TEXT_INFO, "getAll");
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info(CREATE_TEXT_INFO, "findByAgeBetween");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Student getById(int id) {
        logger.info(CREATE_TEXT_INFO, "getById");
        logger.error(CREATE_TEXT_ERROR, id);
        return studentRepository.findFacultyById(id).get();
    }

    public Avatar findAvatar(long studentId) {
        logger.info(CREATE_TEXT_INFO, "findAvatar");
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info(CREATE_TEXT_INFO, "uploadAvatar");
        Student student = findByStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Long getCountStudent() {
        logger.info(CREATE_TEXT_INFO, "getCountStudent");
        return studentRepository.getCountStudent();
    }

    public List<Student> getLastFiveStudent() {
        logger.info(CREATE_TEXT_INFO, "getLastFiveStudent");
        return studentRepository.getLastFiveStudent();
    }

    public Float getAverageAgeStudent() {
        logger.info(CREATE_TEXT_INFO, "getAverageAgeStudent");
        return studentRepository.getAverageAgeStudent();
    }
}

package ru.hogwarts.school.controller.testRestTemplate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("RestTemplate StudentController")
public class StudentControllerTest {
//    @LocalServerPort
//    int port;
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Autowired
//    private StudentController studentController;
//
//    @Autowired
//    private FacultyRepository facultyRepository;
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    @BeforeEach
//    void setUp() {
//        //очищаю базу перед каждым тестом
//        studentRepository.deleteAll();
//        facultyRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("Проверка что контроллер не пустой и запускается")
//    void contextLoads() throws Exception {
//        Assertions.assertThat(studentController).isNotNull();
//    }
//
//    @Test
//    @DisplayName("POST - Добавление студента")
//    void testCreateFaculty() throws Exception {
//        String url = "http://localhost:" + port + "/student";
//        Student student = new Student(0,"Pety", 24, null);
//
//        Student addStudent = this.testRestTemplate.postForObject(url, student, Student.class);
//        System.out.println(addStudent.getId());
////       Проверяю, что не добавлен пустой объект
//        Assertions
//                .assertThat(addStudent)
//                .isNotNull();
//
//        // Проверяем в базе, что факультет добавился
//        assertTrue(studentRepository.findById(addStudent.getId()).isPresent());
//    }
////
//    @Test
//    @DisplayName("GET Поиск по Id - студента")
//    void testGetStudentInfoById() throws Exception {
//
//        Student student = new Student(0,"Pety", 24, null);
//        //создаю запись
//        Student addStudent = studentController.createStudent(student);
//        String url = "http://localhost:" + port + "/student/" + addStudent.getId();
//
//        Student getStudent = this.testRestTemplate.getForObject(url, Student.class);
//
//        Assertions.assertThat(getStudent).isNotNull();
//        Assertions.assertThat(addStudent.getId()).isEqualTo(getStudent.getId());
//    }
//
//
//    @Test
//    @DisplayName("PUT Обновление данных по студенту")
//    void tesUpdateStudent() throws Exception {
//        String url = "http://localhost:" + port + "/student";
//        Student student = new Student(0,"Pety", 24, null);
//
//        //создаю запись
//        Student addStudent = studentController.createStudent(student);
//
////        обновляю данные по студенту
//        Student updateStudent = new Student( addStudent.getId(),"Vasy", 20,null);
//
//        // обновили данные через запрос в БД
//        testRestTemplate.put(url, updateStudent);
//
////      получил ответ и проверили
//        ResponseEntity<Student> response = testRestTemplate.getForEntity(url + "/" + addStudent.getId(), Student.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Assertions.assertThat(updateStudent.getName()).isEqualTo(response.getBody().getName());
//        Assertions.assertThat(updateStudent.getAge()).isEqualTo(response.getBody().getAge());
//
//    }
////
//    @Test
//    @DisplayName("Delete Удаление данных по студенту")
//    void testDeleteStudent() throws Exception {
//        //создаю запись
//        Student student = new Student(0,"Pety", 24, null);
//        Student addStudent = studentController.createStudent(student);
//
//        String url = "http://localhost:" + port + "/student/" + addStudent.getId();
//
////       выполнили запрос на удаление проверили ответ
//        ResponseEntity<Student> response = testRestTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Student.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
////        проверили, что записи нет
//        Student deleteStudent = this.testRestTemplate.getForObject(url, Student.class);
//        Assertions.assertThat(deleteStudent).isNull();
//    }
////
//    @Test
//    @DisplayName("Get- Поиск по возрасту")
//    void testFilterAgeByAge() throws Exception {
//
////        создали 2 записи
//        Student student = new Student(0,"Pety", 24, null);
//        Student student1 = new Student(0,"Vasy", 24, null);
//        Student addStudent = studentController.createStudent(student);
//        Student addStudent1 = studentController.createStudent(student1);
//
//       String  url = "http://localhost:" + port + "/student?age=" + student.getAge();
//
////       Запрос поиска возраста
//        ResponseEntity<Collection<Student>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {
//        });
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Collection<Student> jsonObjects = response.getBody();
//        Assertions.assertThat(jsonObjects)
//                .isNotNull()
//                .isNotEmpty()
//                .isEqualTo(List.of(addStudent,addStudent1));
//    }
//
//    @Test
//    @DisplayName("Get- Поиск по max и min возрасту")
//    void testFilterAgeByMinAndMaxAge() throws Exception {
//        //        создали 2 записи в БД
//        Student student = new Student(0,"Pety", 24, null);
//        Student student1 = new Student(0,"Vasy", 27, null);
//        Student addStudent = studentController.createStudent(student);
//        Student addStudent1 = studentController.createStudent(student1);
//
//        String  url = "http://localhost:" + port + "/student?min=" + student.getAge() + "&max=" + student1.getAge();
//
//        ResponseEntity<Collection<Student>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {
//        });
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Collection<Student> jsonObjects = response.getBody();
//        Assertions.assertThat(jsonObjects)
//                .isNotNull()
//                .isNotEmpty()
//                .isEqualTo(List.of(addStudent,addStudent1));
//    }

//    @Test
//    @DisplayName("Get- Поиск студентов по id факультету")
//    void testFilterColorIdStudent() throws Exception {
//        Faculty faculty = new Faculty(0,"It", "blue",new ArrayList<>());
//        Student student = new Student(0,"Pety", 25, null);
//
//        Faculty savedFaculty = facultyRepository.save(faculty);
//        savedFaculty.getStudents().add(student);
//
//        Student createdStudent = studentRepository.save(student);  // Каскад работает!
//        createdStudent.setFaculty(savedFaculty);
//
//        String url = "http://localhost:" + port + "/student?idFaculty=" + savedFaculty.getId();
//        ResponseEntity<List<Student>> response = this.testRestTemplate.exchange(url, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<Student>>() {});
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Assertions.assertThat(response.getBody())
//                .isNotNull()
//                .isEqualTo(List.of(createdStudent));
//
//    }
}

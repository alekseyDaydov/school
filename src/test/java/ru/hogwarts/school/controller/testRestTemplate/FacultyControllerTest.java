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
import org.springframework.web.bind.annotation.RequestParam;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("RestTemplate FacultyController")
public class FacultyControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        //очищаю базу перед каждым тестом
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    @DisplayName("Проверка что контроллер не пустой и запускается")
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    @DisplayName("POST - Добавление факультета")
    void testCreateFaculty() throws Exception {
        String url = "http://localhost:" + port + "/faculty";
        Faculty faculty = new Faculty("It", "red");
//        faculty.setName("it");
//        faculty.setColor("red");

        Faculty addFaculty = this.testRestTemplate.postForObject(url, faculty, Faculty.class);

//       Проверяю, что не добавлен пустой объект
        Assertions
                .assertThat(addFaculty)
                .isNotNull();

        // Проверяем в базе, что факультет добавился
        assertTrue(facultyRepository.findById(addFaculty.getId()).isPresent());
    }

    @Test
    @DisplayName("GET Поиск факультета по Id -факультета")
    void testGetFacultyInfoByIdFaculty() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setName("it");
        faculty.setColor("red");

        //создаю запись
        Faculty addFaculty = facultyController.createFaculty(faculty);
        String url = "http://localhost:" + port + "/faculty/" + addFaculty.getId();

        Faculty getFaculty = this.testRestTemplate.getForObject(url, Faculty.class);

        Assertions.assertThat(getFaculty).isNotNull();
        Assertions.assertThat(addFaculty.getId()).isEqualTo(getFaculty.getId());
    }


    @Test
    @DisplayName("PUT Обновление данных по факультету")
    void tesUpdateFaculty() throws Exception {
        String url = "http://localhost:" + port + "/faculty";

//        Faculty faculty = new Faculty(0, "ItFaculty", "blue", null);
        Faculty faculty = new Faculty("ItFaculty", "blue");

        Faculty createdFaculty = testRestTemplate.postForObject(url, faculty, Faculty.class);

//        обновляю данные факультета
//        Faculty updateFaculty = new Faculty(createdFaculty.getId(), "OIT", "red", null);
        Faculty updateFaculty = new Faculty( );
        updateFaculty.setId(createdFaculty.getId());
        updateFaculty.setName("OIT");
        updateFaculty.setColor("red");
        // обновили данные через запрос в БД
        testRestTemplate.put(url, updateFaculty);

//      получил ответ и проверили
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(url + "/" + createdFaculty.getId(), Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertThat(updateFaculty.getName()).isEqualTo(response.getBody().getName());
        Assertions.assertThat(updateFaculty.getColor()).isEqualTo(response.getBody().getColor());

    }

    @Test
    @DisplayName("Delete Удаление данных по факультету")
    void testDeleteFaculty() throws Exception {
        String url = "http://localhost:" + port + "/faculty";
        Faculty faculty = new Faculty( "ItFaculty", "blue");
//        создали
        Faculty createdFaculty = testRestTemplate.postForObject(url, faculty, Faculty.class);
        url = "http://localhost:" + port + "/faculty/" + createdFaculty.getId();

//       выполнили запрос на удаление проверили ответ
        ResponseEntity<Faculty> response = testRestTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

//        проверили, что записи нет
        Faculty deleteFaculty = this.testRestTemplate.getForObject(url, Faculty.class);
        Assertions.assertThat(deleteFaculty).isNull();
    }

    @Test
    @DisplayName("Get- Поиск по цвету")
    void testFilterColorByColor() throws Exception {

//        создали 2 записи в БД
        Faculty faculty = new Faculty( "ItFaculty", "blue");
        Faculty faculty2 = new Faculty( "OIT", "blue");
        String url = "http://localhost:" + port + "/faculty";
        // добавили
        Faculty createdFaculty = testRestTemplate.postForObject(url, faculty, Faculty.class);
        Faculty createdFaculty2 = testRestTemplate.postForObject(url, faculty2, Faculty.class);

        url = "http://localhost:" + port + "/faculty?color=" + createdFaculty.getColor();

//       Запрос помска цвета
        ResponseEntity<Collection<Faculty>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Faculty>>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Collection<Faculty> jsonObjects = response.getBody();
        Assertions.assertThat(jsonObjects)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(List.of(createdFaculty, createdFaculty2));
    }

    @Test
    @DisplayName("Get- Поиск по имени или цвету")
    void testFilterColorByColorOrName() throws Exception {
        //        создали 2 записи в БД
        final String FIND_ORANGE = "orange";
        Faculty faculty = new Faculty("ItOrange", "blue");
        Faculty faculty2 = new Faculty( "Blue", "orange");
        String url = "http://localhost:" + port + "/faculty";

        // добавили
        Faculty createdFaculty = testRestTemplate.postForObject(url, faculty, Faculty.class);
        Faculty createdFaculty2 = testRestTemplate.postForObject(url, faculty2, Faculty.class);

        url = "http://localhost:" + port + "/faculty?find=" + FIND_ORANGE;

//       Запрос помска цвета
        ResponseEntity<Collection<Faculty>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Faculty>>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Collection<Faculty> jsonObjects = response.getBody();
        Assertions.assertThat(jsonObjects)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(List.of(createdFaculty, createdFaculty2));
    }

    @Test
    @DisplayName("Get- Поиск по id студента")
    void testFilterColorIdStudent() throws Exception {
        Student student = new Student("Gtnz", 25);  // Без id и faculty!
        Faculty faculty = new Faculty("123", "234");

        Faculty savedFaculty = facultyRepository.save(faculty);

        savedFaculty.addStudent(student);  // Синхронизирует ОБЕ стороны!

        Student createdStudent = studentRepository.save(student);  // Каскад работает!

// Проверка foreign key
        Assertions.assertThat(createdStudent.getFaculty()).isEqualTo(savedFaculty);

// Ваш REST-эндпоинт
        String url1 = "http://localhost:" + port + "/faculty?idStudent=" + createdStudent.getId();
//        ResponseEntity<List<Faculty>> response = this.testRestTemplate.exchange(url1, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<Faculty>>() {});
//        System.out.println(response.getBody());



//        Assertions.assertThat(response.getBody()).containsExactly(savedFaculty);
//
//        //        создали записи в БД
//        Student student = new Student(0, "Gtnz", 25, null);
//        Faculty faculty = new Faculty(0, "123", "234", new ArrayList<>());
//// сохраняем факультет
//        Faculty createFaculty = facultyRepository.save(faculty);
//
//        // синхронизация
//        student.setFaculty(createFaculty);
//        createFaculty.getStudents().add(student);
//
//        Student createdStudent =  studentRepository.save(student);
//
//        Assertions.assertThat(createdStudent.getFaculty().getId()).isEqualTo(createFaculty.getId());
//        createdStudent.setFaculty(createFaculty);
//
////        String url = "http://localhost:" + port + "/student";
////        Student createdStudent = this.testRestTemplate.postForObject(url, student, Student.class);
////        Assertions.assertThat(createdStudent).isNotNull();
//
//        String url1 = "http://localhost:" + port + "/faculty?idStudent=" + createdStudent.getId();
//        ResponseEntity<List<Faculty>> response = this.testRestTemplate.exchange(url1, HttpMethod.GET, null, new ParameterizedTypeReference<List<Faculty>>() {
//        });
//
//        System.out.println(response.getBody());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Assertions.assertThat(response.getBody())
//                .isNotNull()
//                .isEqualTo(List.of(createFaculty));

    }
}



package ru.hogwarts.school.controller.testRestTemplate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

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

//    @BeforeEach
//    void setUp() {
//        //очищаю базу перед каждым тестом
//        studentRepository.deleteAll();
//        facultyRepository.deleteAll();
//    }

    @Test
    @DisplayName("Проверка что контроллер не пустой и запускается")
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    @DisplayName("POST - Добавление факультета")
    void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("it");
        faculty.setColor("red");
        String url = "http://localhost:" + port + "/faculty";
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
       Faculty addFaculty =  facultyController.createFaculty(faculty);

        String url = "http://localhost:" + port + "/faculty/" + addFaculty.getId();
        Faculty getFaculty = this.testRestTemplate.getForObject(url, Faculty.class);

        Assertions.assertThat(getFaculty).isNotNull();
        Assertions.assertThat(addFaculty.getId()).isEqualTo(getFaculty.getId());
    }

    @Test
    @DisplayName("PUT Обновление данных по факультету")
    void tesUpdateFaculty() throws  Exception{
        Faculty faculty = new Faculty(0,"ItFaculty", "blue", null);

        //создаю  и сохраняю запись
        Faculty addFaculty =  facultyController.createFaculty(faculty);
        //        вношу изменения в факультет
        addFaculty.setName("OIT");
        addFaculty.setColor("red");

        String url = "http://localhost:" + port + "/faculty";
        Faculty putFaculty =
                , Faculty.class);
        Assertions
                .assertThat(this.testRestTemplate.exchange(url, HttpMethod.PUT,new HttpEntity<>(addFaculty)).isNotNull();
        Assertions.assertThat(putFaculty.getId()).isEqualTo(addFaculty.getId());
        Assertions.assertThat(putFaculty.getColor()).isEqualTo(addFaculty.getColor());
        Assertions.assertThat(putFaculty.getName()).isEqualTo(addFaculty.getName());
//
//        addFaculty.setColor("black");
//        Faculty editFaculty = facultyController.updateFaculty(faculty).getBody();
//
//        String url = "http://localhost:" + port + "/faculty";
//        Faculty getFaculty = this.testRestTemplate.getForObject(url, Faculty.class);
//
//        Assertions.assertThat(getFaculty).isNotNull();
//        Assertions.assertThat(addFaculty.getId()).isEqualTo(getFaculty.getId());
    }
}

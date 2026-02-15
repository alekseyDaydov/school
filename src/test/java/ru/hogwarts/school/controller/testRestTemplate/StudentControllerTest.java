package ru.hogwarts.school.controller.testRestTemplate;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("tests")
@DisplayName("RestTemplate StudentController")
public class StudentControllerTest {
}

package ru.hogwarts.school.controller.testWebMvc;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Arrays;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(StudentController.class)
public class StudentControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    @DisplayName("Контроллеры запускаются")
    void contextLoads() throws Exception {
        Assertions.assertThat(mockMvc).isNotNull();
    }

    private Faculty faculty;
    private Student student1;
    private Student student2;
    private String COLOR_BLACK = "Black";
    private String COLOR_RED = "RED";
    private String NAME_STUDENT_VASY = "Vasy";
    private String NAME_STUDENT_PETY = "Pety";
    private String NAME_FACULTY_IT = "IT";
    private String NAME_FACULTY_OIT = "OIT";
    @BeforeEach
        // Выполняется перед каждым тестом
    void setUp() {
        faculty = new Faculty(1L,NAME_FACULTY_IT, COLOR_BLACK,new ArrayList<>());
        student1 = new Student(1L,NAME_STUDENT_VASY, 30,faculty);
        student2 = new Student(2L,NAME_STUDENT_PETY, 25,faculty);
    }
    @Test
    @DisplayName("Get - Найти Студента по Id")
    void testGetStudentInfo() throws Exception {

        when(studentService.findByStudent(1L)).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(NAME_STUDENT_VASY))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    @DisplayName("GET Найти Студента по Id - не найден")
    void testGetStudentInfoNotFound() throws Exception {
        when(studentService.findByStudent(1L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST Добавление Студента")
    void testCreateStudent() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(NAME_STUDENT_VASY))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    @DisplayName("PUT Редактирование Студента")
    void testEditStudent() throws Exception {
        when(studentService.updateStudent(any(Student.class))).thenReturn(student2);

        mockMvc.perform(MockMvcRequestBuilders.put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value(NAME_STUDENT_PETY))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    @DisplayName("PUT - Редактирование Студента - не найден")
    void testEditStudentNotFound() throws Exception {
             when(studentService.updateStudent(any(Student.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student2)))
                .andExpect(status().isBadRequest());
    }
//
    @Test
    @DisplayName("DELETE - Удаление Студента по Id")
    void testDeleteStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/student/1"))
                .andExpect(status().isOk());
    }
//
    @Test
    @DisplayName("GET - Поиск студента по возрасту")
    void testFindStudentsAge() throws Exception {
        when(studentService.filterAge(30)).thenReturn(Arrays.asList(student1));

        mockMvc.perform(MockMvcRequestBuilders.get("/student")
                        .param("age", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].age").value(30));

    }

    @Test
    @DisplayName("GET Поиск студентов в диапазоне возрастов")
    void testFindStudentsByAgeRange() throws Exception {
        when(studentService.findByAgeBetween(20, 35)).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(MockMvcRequestBuilders.get("/student")
                        .param("min", "20")
                        .param("max", "35"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].age").value(25));
    }

    @Test
    @DisplayName("Название Факультета Студента")
    void testGetStudentFaculty() throws Exception {
        when(studentService.findByStudent(1L)).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders.get("/student")
                .param("idFaculty", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(NAME_FACULTY_IT))
                .andExpect(jsonPath("$.color").value(COLOR_BLACK));
    }

}

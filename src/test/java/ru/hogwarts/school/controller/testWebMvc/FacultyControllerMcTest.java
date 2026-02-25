package ru.hogwarts.school.controller.testWebMvc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(FacultyController.class)
public class FacultyControllerMcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacultyService facultyService;

    @MockitoBean
    private StudentService studentService;

    private Faculty faculty;
    private Student student1;
    private Student student2;
    private String COLOR_BLACK = "Black";
    private String COLOR_RED = "RED";
    private String NAME_STUDENT_VASY = "Vasy";
    private String NAME_STUDENT_PETY = "Pety";
    private String NAME_FACULTY_IT = "IT";
    private String NAME_FACULTY_OIT = "OIT";
    @BeforeEach             // Выполняется перед каждым тестом
    void setUp() {
        faculty = new Faculty(1L,NAME_FACULTY_IT, COLOR_BLACK,new ArrayList<>());
        student1 = new Student(1L,NAME_STUDENT_VASY, 30,faculty);
        student2 = new Student(2L,NAME_STUDENT_PETY, 25,faculty);
    }

    @Test
    @DisplayName("GET - Найти факультет по Id")
    void testGetFacultyInfo() throws Exception {
        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(NAME_FACULTY_IT))
                .andExpect(jsonPath("$.color").value(COLOR_BLACK));

        verify(facultyService).findFaculty(1L);
    }
//
    @Test
    @DisplayName("GET Найти факультет по несуществующему Id")
    void testGetFacultyInfoNotFound() throws Exception {
        when(facultyService.findFaculty(10L)).thenReturn(null);

        mockMvc.perform(get("/faculty/{id}", 10L))
                .andExpect(status().isNotFound());

        verify(facultyService).findFaculty(10L);
    }
//
    @Test
    @DisplayName("POST - Добавить Факультет")
    void testPostFaculty() throws Exception {
        Faculty newFaculty = new Faculty(0L,NAME_FACULTY_IT, COLOR_BLACK,new ArrayList<>());
        Faculty savedFaculty = new Faculty(10L,NAME_FACULTY_IT, COLOR_BLACK,new ArrayList<>());


        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(savedFaculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.name").value(NAME_FACULTY_IT))
                .andExpect(jsonPath("$.color").value(COLOR_BLACK));

        verify(facultyService).createFaculty(any(Faculty.class));
    }
//
    @Test
    @DisplayName("PUT - Редактирование Факультета по Id")
    void testEditFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty(1L,NAME_FACULTY_OIT, COLOR_RED,new ArrayList<>());

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(NAME_FACULTY_OIT))
                .andExpect(jsonPath("$.color").value(COLOR_RED));

        verify(facultyService).updateFaculty(any(Faculty.class));
    }
//
    @Test
    @DisplayName("DELETE - Удаление Факультета по Id")
    void testDeleteFaculty() throws Exception {
        when(facultyService.findFaculty(1L)).thenReturn(faculty);
        doNothing().when(facultyService).deleteFaculty(1L);

        mockMvc.perform(delete("/faculty/{id}", 1L))
                .andExpect(status().isOk());

        verify(facultyService).deleteFaculty(1L);
    }

    @Test
    @DisplayName("Get - Поиск факультета по цвету")
    void testGetFacultyByColor() throws Exception {
        Collection<Faculty> faculties = Arrays.asList(faculty);
        when(facultyService.filterColor(COLOR_BLACK)).thenReturn(faculties);
        mockMvc.perform(get("/faculty")
                        .param("color", COLOR_BLACK))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value(NAME_FACULTY_IT))
                .andExpect(jsonPath("$[0].color").value(COLOR_BLACK));

        verify(facultyService).filterColor(COLOR_BLACK);
    }
//
    @Test
    @DisplayName("Поиск факультета по цвету - нет результатов")
    void testGetFacultyByColorEmpty() throws Exception {
        when(facultyService.filterColor(COLOR_RED)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/faculty")
                        .param("color", COLOR_RED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(facultyService).filterColor(COLOR_RED);
    }

    @Test
    @DisplayName("GET - Поиск факультета по Названию ИлИ Цвету")
    void testGetSearchFaculties() throws Exception {
        Collection<Faculty> faculties = Arrays.asList(faculty);

        // Тест поиска по названию
        when(facultyService.findByNameContainsIgnoreCaseOrColorIgnoreCase(COLOR_BLACK)).thenReturn(faculties);

        mockMvc.perform(get("/faculty")
                        .param("find", COLOR_BLACK))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value(NAME_FACULTY_IT))
                .andExpect(jsonPath("$[0].color").value(COLOR_BLACK));

        verify(facultyService).findByNameContainsIgnoreCaseOrColorIgnoreCase(COLOR_BLACK);
    }


}

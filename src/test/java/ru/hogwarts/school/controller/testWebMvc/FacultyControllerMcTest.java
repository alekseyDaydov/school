package ru.hogwarts.school.controller.testWebMvc;

import ch.qos.logback.core.joran.spi.HttpUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FacultyControllerMcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentRepository studentRepository;

    @MockitoBean
    private FacultyRepository facultyRepository;

    @MockitoSpyBean
    private StudentService studentService;

    @MockitoSpyBean
    private FacultyService facultyService;

    @Test
    public void saveStudentTest() throws Exception{
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", "Vasy");
        studentObject.put("age", 34);
        Student student = new Student();
        student.setId(1);
        student.setName("Vasy");
        student.setAge(34);

        //когда выполняется метод save в параметр передается любой объект типа Student, тогда вернуть объект student
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        when(studentRepository.findFacultyById(any(Integer.class))).thenReturn(Optional.of(student));


        mockMvc.perform(MockMvcRequestBuilders
                .post("/student")
                .content(studentObject.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.age").value("age"));

//        mockMvc.perform(MockMvcRequestBuilders
//                .get())

    }
}

package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faculty_seq")
    private Long id;
    private String name;
    private String color;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "faculty", cascade = CascadeType.ALL, orphanRemoval = true )
    @JsonBackReference
    private Collection<Student> students = new ArrayList<>();

    public Faculty() {
    }

    public Faculty(String name, String color) {
        this.name = name;
        this.color = color;
    }

//    public Faculty(long id, String name, String color, Collection<Student> students) {
//        this.id = id;
//        this.name = name;
//        this.color = color;
//        this.students = students;
//    }

    // Хелпер для синхронизации (ОБЯЗАТЕЛЬНО!)
    public void addStudent(Student student) {
        students.add(student);
        student.setFaculty(this);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", students=" + students +
                '}';
    }

    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return id == faculty.id && Objects.equals(name, faculty.name) && Objects.equals(color, faculty.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

}

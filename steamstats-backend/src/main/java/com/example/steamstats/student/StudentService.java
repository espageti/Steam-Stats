package com.example.steamstats.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private final StudentRepository studentRepository;
    public List<Student> getStudents()
    {

        return studentRepository.findAll();
//        return List.of(
//                new Student(
//                    1l,
//                    "John",
//                    "mariam@gmail.com",
//                    LocalDate.of(2000, Month.JANUARY, 4),
//                    21
//
//                )
//        );
    }

    public void updateStudent(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setDob(student.getDob());
        existingStudent.setAge(student.getAge());
        studentRepository.save(existingStudent);
    }

    public void addStudent(Student student) {
        studentRepository.save(student);
    }
}

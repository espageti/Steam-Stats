//package com.example.steamstats.student;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDate;
//import java.time.Month;
//import java.util.List;
//
//import static java.time.Month.*;
//
//@Configuration
//public class StudentConfig {
//
//    @Bean
//    CommandLineRunner commandLineRunner(StudentRepository repository)
//    {
//        return args ->{
//            Student john = new Student(
//                    "John",
//                    "john@gmail.com",
//                    LocalDate.of(2000, JANUARY, 4),
//                    22
//            );
//            Student mariam = new Student(
//                    "Mariam",
//                    "mariam@gmail.com",
//                    LocalDate.of(2010, JANUARY, 8),
//                    240
//            );
//
//            repository.saveAll(
//                    List.of(mariam, john)
//            );
//        };
//
//    }
//}

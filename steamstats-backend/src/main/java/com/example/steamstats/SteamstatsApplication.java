package com.example.steamstats;

import com.example.steamstats.student.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
public class 	SteamstatsApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		SpringApplication.run(SteamstatsApplication.class, args);
	}



}

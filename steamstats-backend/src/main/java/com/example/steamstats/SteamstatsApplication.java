package com.example.steamstats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SteamstatsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SteamstatsApplication.class, args);
	}
}

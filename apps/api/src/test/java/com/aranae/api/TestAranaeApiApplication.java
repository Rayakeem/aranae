package com.aranae.api;

import org.springframework.boot.SpringApplication;

public class TestAranaeApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(AranaeApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

package de.immosolve;// *********************************************************************************************************************
// * (C) 2017 Immosolve GmbH, Tegelbarg 43, 24576 Bad Bramstedt
// *********************************************************************************************************************

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

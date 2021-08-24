package de.astahsrm.gremiomat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// @SpringBootApplication
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class GremiomatApplication {

	public static void main(String[] args) {
		SpringApplication.run(GremiomatApplication.class, args);
	}

}

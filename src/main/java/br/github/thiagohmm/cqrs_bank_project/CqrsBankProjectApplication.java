package br.github.thiagohmm.cqrs_bank_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories(basePackages = "br.github.thiagohmm.cqrs_bank_project.command.repository")
@EnableMongoRepositories(basePackages = "br.github.thiagohmm.cqrs_bank_project.query.repository")
public class CqrsBankProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CqrsBankProjectApplication.class, args);
	}

}

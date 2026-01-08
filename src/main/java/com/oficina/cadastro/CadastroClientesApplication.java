package com.oficina.cadastro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.oficina.cadastro.domain.repository")
@EntityScan(basePackages = "com.oficina.cadastro.domain.model")
public class CadastroClientesApplication {

	public static void main(String[] args) {
		// Forçar uso de IPv4 para evitar problemas de conexão com Supabase no Railway
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.net.preferIPv6Addresses", "false");
		SpringApplication.run(CadastroClientesApplication.class, args);
	}

	@Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
	private String swaggerPath;

	@Bean
	public CommandLineRunner logUrl(Environment env) {
		return args -> {
			String port = env.getProperty("server.port", "8080");
			String contextPath = env.getProperty("server.servlet.context-path", "");
			String baseUrl = "http://localhost:" + port + contextPath;

			System.out.println("\n----------------------------------------------------------");
			System.out.println("API Initialized successfully!");
			System.out.println("Base URL:    " + baseUrl);
			System.out.println("Swagger UI:  " + baseUrl + swaggerPath);
			System.out.println("----------------------------------------------------------\n");
		};
	}

}

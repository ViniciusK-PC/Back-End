package com.oficina.cadastro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
})
public class CadastroClientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadastroClientesApplication.class, args);
	}

	@org.springframework.context.annotation.Bean
	public org.springframework.boot.CommandLineRunner logUrls(org.springframework.core.env.Environment env) {
		return args -> {
			String port = env.getProperty("server.port", "8080");
			String contextPath = env.getProperty("server.servlet.context-path", "");
			String baseUrl = "http://localhost:" + port + contextPath;
			String swaggerUrl = baseUrl + "/swagger-ui.html";

			System.out.println("\n-----------------------------------------------------------");
			System.out.println("🚀 APLICAÇÃO RODANDO CORRETAMENTE! 🚀");
			System.out.println("");
			System.out.println("📡 API Base URL: " + baseUrl);
			System.out.println("📄 Swagger UI:   " + swaggerUrl);
			System.out.println("-----------------------------------------------------------\n");
		};
	}

}

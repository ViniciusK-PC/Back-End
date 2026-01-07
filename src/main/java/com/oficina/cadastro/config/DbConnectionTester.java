package com.oficina.cadastro.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DbConnectionTester {

    @Bean
    public CommandLineRunner testDbConnection(DataSource dataSource) {
        return args -> {
            System.out.println("[DB-TEST] Attempting to obtain a connection from the DataSource...");
            try (Connection conn = dataSource.getConnection()) {
                System.out.println("[DB-TEST] Connection successful! URL=" + conn.getMetaData().getURL());
            } catch (Exception e) {
                System.err.println("[DB-TEST] Connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}

package com.oficina.cadastro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@org.springframework.boot.autoconfigure.condition.ConditionalOnExpression("#{environment.getProperty('DATABASE_URL') != null}")
public class HerokuDatabaseConfig {

    @Bean
    @Primary
    @Profile("!test")
    public DataSource dataSource() {

        try {
            System.out.println("Configurando Heroku Postgres a partir da DATABASE_URL...");
            URI dbUri = new URI(databaseUrl);

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
                    + "?sslmode=require";

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName("org.postgresql.Driver");

            // Configurações para o Heroku
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(1);
            config.setIdleTimeout(10000);
            config.setMaxLifetime(20000);
            config.setConnectionTimeout(30000);

            return new HikariDataSource(config);
        } catch (URISyntaxException e) {
            System.err.println("Erro de sintaxe na DATABASE_URL: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao configurar DataSource: " + e.getMessage());
            return null;
        }
    }
}

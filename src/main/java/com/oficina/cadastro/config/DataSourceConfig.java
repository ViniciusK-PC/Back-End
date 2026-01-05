package com.oficina.cadastro.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTestQuery("SELECT 1");
        
        // Desabilita auto-commit para permitir controle de transações
        config.setAutoCommit(false);
        
        HikariDataSource dataSource = new HikariDataSource(config);
        
        // Wrapper para garantir que todas as conexões tenham auto-commit desabilitado
        return new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                Connection conn = dataSource.getConnection();
                if (conn.getAutoCommit()) {
                    conn.setAutoCommit(false);
                }
                return conn;
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                Connection conn = dataSource.getConnection(username, password);
                if (conn.getAutoCommit()) {
                    conn.setAutoCommit(false);
                }
                return conn;
            }

            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                return dataSource.unwrap(iface);
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return dataSource.isWrapperFor(iface);
            }

            @Override
            public java.io.PrintWriter getLogWriter() throws SQLException {
                return dataSource.getLogWriter();
            }

            @Override
            public void setLogWriter(java.io.PrintWriter out) throws SQLException {
                dataSource.setLogWriter(out);
            }

            @Override
            public void setLoginTimeout(int seconds) throws SQLException {
                dataSource.setLoginTimeout(seconds);
            }

            @Override
            public int getLoginTimeout() throws SQLException {
                return dataSource.getLoginTimeout();
            }

            @Override
            public java.util.logging.Logger getParentLogger() {
                try {
                    return dataSource.getParentLogger();
                } catch (Exception e) {
                    return java.util.logging.Logger.getLogger(DataSourceConfig.class.getName());
                }
            }
        };
    }
}


package com.bs.dbperformancemetrics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories(basePackages = "com.bs.dbperformancemetrics.repository.oracle.jdbc")
public class JDBCConfig {
    private final JdbcTemplate jdbcTemplate;

    public JDBCConfig(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @PostConstruct
    public void init() {
        deleteAllTableContents();
        resetSequence();
    }

    @Transactional
    public void deleteAllTableContents() {
        jdbcTemplate.update("DELETE FROM ORACLE_USER");
    }

    @Transactional
    public void resetSequence() {
        jdbcTemplate.update("DROP SEQUENCE HIBERNATE_SEQUENCE");
        jdbcTemplate.update("CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1");
    }
}

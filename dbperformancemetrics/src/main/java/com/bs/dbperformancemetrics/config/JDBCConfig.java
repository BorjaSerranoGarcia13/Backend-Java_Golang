package com.bs.dbperformancemetrics.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

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
    }

    @Transactional
    public void createIDSequence() {
        String sql = "CREATE SEQUENCE SEQ_USER_ID MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 1000";
        jdbcTemplate.execute(sql);
    }

    @Transactional
    public void deleteAllTableContents() {
        jdbcTemplate.update("DELETE FROM ORACLE_USER");
    }

    @Transactional
    public void resetAndCreateIDSequence() {
        String dropSql = "BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE SEQ_USER_ID'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;";
        jdbcTemplate.execute(dropSql);
        String createSql = "CREATE SEQUENCE SEQ_USER_ID MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 1000";
        jdbcTemplate.execute(createSql);
    }
}

package com.bs.dbperformancemetrics.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Configuration
@EnableJpaRepositories(basePackages = "com.bs.dbperformancemetrics.repository.oracle.jpa")
public class JPAConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    public void init() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {

            }
        });
    }

    @Transactional
    public void deleteAllTableContents() {
        entityManager.createNativeQuery("DELETE FROM ORACLE_USER").executeUpdate();
    }

    @Transactional
    public void resetAndCreateIDSequence() {
        String dropSql = "BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE SEQ_USER_ID'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;";
        entityManager.createNativeQuery(dropSql).executeUpdate();
        String createSql = "CREATE SEQUENCE SEQ_USER_ID MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 1000";
        entityManager.createNativeQuery(createSql).executeUpdate();
    }

}

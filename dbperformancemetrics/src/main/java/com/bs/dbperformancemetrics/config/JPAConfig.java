package com.bs.dbperformancemetrics.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                deleteAllTableContents();
            }
        });
    }

    @Transactional
    public void deleteAllTableContents() {
        entityManager.createNativeQuery("DELETE FROM ORACLE_USER").executeUpdate();
        resetSequence();
    }

    @Transactional
    public void resetSequence() {
        entityManager.createNativeQuery("DROP SEQUENCE HIBERNATE_SEQUENCE").executeUpdate();
        entityManager.createNativeQuery("CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1").executeUpdate();
    }

}


package com.bs.dbperformancemetrics.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
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

    @Transactional
    public void createIDSequence() {
        entityManager.createNativeQuery("CREATE SEQUENCE seq_user_id MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 1000").executeUpdate();
    }

    @Transactional
    public void dropIDSequence() {
        entityManager.createNativeQuery("DROP SEQUENCE seq_user_id").executeUpdate();
    }

    @Transactional
    public void printActiveSequences() {
        String sql = "SELECT sequence_name, min_value, max_value, increment_by, last_number FROM user_sequences";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> sequences = query.getResultList();

        for (Object[] sequence : sequences) {
            System.out.println("Sequence Name: " + sequence[0]);
            System.out.println("Min Value: " + sequence[1]);
            System.out.println("Max Value: " + sequence[2]);
            System.out.println("Increment By: " + sequence[3]);
            System.out.println("Last Number: " + sequence[4]);
            System.out.println("-----------------------------");
        }
    }

}

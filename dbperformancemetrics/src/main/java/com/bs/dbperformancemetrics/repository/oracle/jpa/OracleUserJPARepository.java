package com.bs.dbperformancemetrics.repository.oracle.jpa;

import com.bs.dbperformancemetrics.model.OracleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OracleUserJPARepository extends JpaRepository<OracleUser, Long> {

    Optional<OracleUser> findByUsername(String username);

    List<OracleUser> findByName(String name);

    @Query("SELECT u.password as password FROM OracleUser u WHERE u.username = :username")
    Optional<String> findPasswordByUsername(@Param("username") String username);

    void deleteByUsername(String username);

    void deleteByName(String name);

    @Query(value = "SELECT MAX(u.id) FROM OracleUser u", nativeQuery = true)
    Optional<Long> findMaxId();

}
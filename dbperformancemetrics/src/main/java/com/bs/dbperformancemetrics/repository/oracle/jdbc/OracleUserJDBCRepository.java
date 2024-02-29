package com.bs.dbperformancemetrics.repository.oracle.jdbc;

import com.bs.dbperformancemetrics.model.OracleUser;
import com.bs.dbperformancemetrics.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OracleUserJDBCRepository implements BaseRepository<OracleUser, Long> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OracleUserJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(OracleUser user) {
        String sql = "INSERT INTO ORACLE_USER (id, name, username, password) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, generateId(), user.getName(), user.getUsername(), user.getPassword());
    }

    @Override
    public void insertAll(List<OracleUser> users) {
        String sql = "INSERT INTO ORACLE_USER (id, name, username, password) VALUES (?, ?, ?, ?)";

        long maxId = generateId();

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OracleUser user = users.get(i);

                if (user.getId() == null) {
                    user.setId(maxId + i);
                }

                ps.setLong(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getUsername());
                ps.setString(4, user.getPassword());
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }

    @Override
    public void save(OracleUser user) {
        String sql = "MERGE INTO ORACLE_USER u USING (SELECT ? AS id, ? AS name, ? AS username, ? AS password FROM dual) incoming "
                     + "ON (u.id = incoming.id) "
                     + "WHEN MATCHED THEN UPDATE SET u.name = incoming.name, u.username = incoming.username, u.password = incoming.password "
                     + "WHEN NOT MATCHED THEN INSERT (id, name, username, password) VALUES (incoming.id, incoming.name, incoming.username, incoming.password)";

        if (user.getId() == null) {
            user.setId(generateId());
        }

        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getUsername(), user.getPassword());
    }

    @Override
    public void saveAll(List<OracleUser> users) {
        String sql = "MERGE INTO ORACLE_USER u USING (SELECT ? AS id, ? AS name, ? AS username, ? AS password FROM dual) incoming "
                     + "ON (u.id = incoming.id) "
                     + "WHEN MATCHED THEN UPDATE SET u.name = incoming.name, u.username = incoming.username, u.password = incoming.password "
                     + "WHEN NOT MATCHED THEN INSERT (id, name, username, password) VALUES (incoming.id, incoming.name, incoming.username, incoming.password)";

        long maxId = generateId();

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OracleUser user = users.get(i);

                if (user.getId() == null) {
                    user.setId(maxId + i);
                }

                ps.setLong(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getUsername());
                ps.setString(4, user.getPassword());
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }

    @Override
    public List<OracleUser> findAll() {
        String sql = "SELECT * FROM ORACLE_USER ORDER BY id ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OracleUser.class));
    }

    @Override
    public Optional<OracleUser> findById(Long id) {
        String sql = "SELECT * FROM ORACLE_USER WHERE id = ?";
        try {
            OracleUser user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(OracleUser.class), id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<OracleUser> findByUsername(String username) {
        String sql = "SELECT * FROM ORACLE_USER WHERE username = ?";
        try {
            OracleUser user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(OracleUser.class), username);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OracleUser> findByName(String name) {
        String sql = "SELECT * FROM ORACLE_USER WHERE name = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OracleUser.class), name);
    }

    @Override
    public Optional<String> findPasswordByUsername(String username) {
        String sql = "SELECT password FROM ORACLE_USER WHERE username = ?";
        try {
            String password = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getString(1), username);
            return Optional.ofNullable(password);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(OracleUser user) {
        String sql = "UPDATE ORACLE_USER SET name = ?, username = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getUsername(), user.getPassword(), user.getId());
    }

    @Override
    public void updateAll(List<OracleUser> users) {
        String sql = "UPDATE ORACLE_USER SET name = ?, username = ?, password = ? WHERE id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OracleUser user = users.get(i);
                ps.setString(1, user.getName());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getPassword());
                ps.setLong(4, user.getId());
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM ORACLE_USER";
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM ORACLE_USER WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Long generateId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM ORACLE_USER";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}

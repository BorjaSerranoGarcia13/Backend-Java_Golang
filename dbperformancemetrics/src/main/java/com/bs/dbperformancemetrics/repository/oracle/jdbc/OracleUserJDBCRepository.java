package com.bs.dbperformancemetrics.repository.oracle.jdbc;

import com.bs.dbperformancemetrics.model.OracleUser;
import com.bs.dbperformancemetrics.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OracleUserJDBCRepository implements BaseRepository<OracleUser, Long> {

    private final JdbcTemplate jdbcTemplate;

    final static String INSERT_SQL = "INSERT INTO ORACLE_USER (id, name, username, password) VALUES (?, ?, ?, ?)";

    final static String INSERT_ALL_SQL = "INSERT INTO ORACLE_USER (id, name, username, password) VALUES (?, ?, ?, ?)";

    final static String SAVE_SQL = "MERGE INTO ORACLE_USER u USING (SELECT ? AS id, ? AS name, ? AS username, ? AS password FROM dual) incoming "
                                   + "ON (u.id = incoming.id) "
                                   + "WHEN MATCHED THEN UPDATE SET u.name = incoming.name, u.username = incoming.username, u.password = incoming.password "
                                   + "WHEN NOT MATCHED THEN INSERT (id, name, username, password) VALUES (incoming.id, incoming.name, incoming.username, incoming.password)";

    final static String SAVE_ALL_SQL = "MERGE INTO ORACLE_USER u USING (SELECT ? AS id, ? AS name, ? AS username, ? AS password FROM dual) incoming "
                                       + "ON (u.id = incoming.id) "
                                       + "WHEN MATCHED THEN UPDATE SET u.name = incoming.name, u.username = incoming.username, u.password = incoming.password "
                                       + "WHEN NOT MATCHED THEN INSERT (id, name, username, password) VALUES (incoming.id, incoming.name, incoming.username, incoming.password)";


    final static String DELETE_BY_NAME_SQL = "DELETE FROM ORACLE_USER WHERE name = ?";

    final static String DELETE_BY_USERNAME_SQL = "DELETE FROM ORACLE_USER WHERE username = ?";

    final static String DELETE_BY_ID_SQL = "DELETE FROM ORACLE_USER WHERE id = ?";

    final static String DELETE_ALL_SQL = "DELETE FROM ORACLE_USER";

    final static String UPDATE_ALL_SQL = "UPDATE ORACLE_USER SET name = ?, username = ?, password = ? WHERE id = ?";

    final static String UPDATE_BY_ID_SQL = "UPDATE ORACLE_USER SET name = ?, username = ?, password = ? WHERE id = ?";

    final static String FIND_PASSWORD_BU_USERNAME_SQL = "SELECT password FROM ORACLE_USER WHERE username = ?";

    final static String FIND_BY_NAME_SQL = "SELECT * FROM ORACLE_USER WHERE name = ?";

    final static String FIND_BY_USERNAME_SQL = "SELECT * FROM ORACLE_USER WHERE username = ?";

    final static String FIND_BY_ID_SQL = "SELECT * FROM ORACLE_USER WHERE id = ?";

    final static String FIND_ALL_SQL = "SELECT * FROM ORACLE_USER ORDER BY id ASC";

    final static String GENERATE_ID_SQL = "SELECT COALESCE(MAX(id), 0) + 1 FROM ORACLE_USER";


    @Autowired
    public OracleUserJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(OracleUser user) {
        jdbcTemplate.update(INSERT_SQL, generateId(), user.getName(), user.getUsername(), user.getPassword());
    }

    @Override
    public void insertAll(List<OracleUser> users) {

        final int batchSize = 1000;

        long maxId = generateId();

        for (int i = 0; i < users.size(); i += batchSize) {
            int index = i;

            final List<OracleUser> batch = users.subList(i, Math.min(users.size(), i + batchSize));

            jdbcTemplate.batchUpdate(INSERT_ALL_SQL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int j) throws SQLException {
                    OracleUser user = batch.get(j);

                    if (user.getId() == null) {
                        user.setId(maxId + index + j);
                    }

                    ps.setLong(1, user.getId());
                    ps.setString(2, user.getName());
                    ps.setString(3, user.getUsername());
                    ps.setString(4, user.getPassword());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public void save(OracleUser user) {

        if (user.getId() == null) {
            user.setId(generateId());
        }

        jdbcTemplate.update(SAVE_SQL, user.getId(), user.getName(), user.getUsername(), user.getPassword());
    }

    @Override
    public void saveAll(List<OracleUser> users) {

        final int batchSize = 1000;

        long maxId = generateId();

        for (int i = 0; i < users.size(); i += batchSize) {
            int index = i;

            final List<OracleUser> batch = users.subList(i, Math.min(users.size(), i + batchSize));

            jdbcTemplate.batchUpdate(SAVE_ALL_SQL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int j) throws SQLException {
                    OracleUser user = batch.get(j);

                    if (user.getId() == null) {
                        user.setId(maxId + index + j);
                    }

                    ps.setLong(1, user.getId());
                    ps.setString(2, user.getName());
                    ps.setString(3, user.getUsername());
                    ps.setString(4, user.getPassword());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public List<OracleUser> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, new BeanPropertyRowMapper<>(OracleUser.class));
    }

    @Override
    public Optional<OracleUser> findById(Long id) {

        try {
            OracleUser user = jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new BeanPropertyRowMapper<>(OracleUser.class), id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<OracleUser> findByUsername(String username) {
        try {
            OracleUser user = jdbcTemplate.queryForObject(FIND_BY_USERNAME_SQL, new BeanPropertyRowMapper<>(OracleUser.class), username);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OracleUser> findByName(String name) {
        return jdbcTemplate.query(FIND_BY_NAME_SQL, new BeanPropertyRowMapper<>(OracleUser.class), name);
    }

    @Override
    public Optional<String> findPasswordByUsername(String username) {

        try {
            String password = jdbcTemplate.queryForObject(FIND_PASSWORD_BU_USERNAME_SQL, (rs, rowNum) -> rs.getString(1), username);
            return Optional.ofNullable(password);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(OracleUser user) {
        jdbcTemplate.update(UPDATE_BY_ID_SQL, user.getName(), user.getUsername(), user.getPassword(), user.getId());
    }

    @Override
    public void updateAll(List<OracleUser> users) {
        final int batchSize = 1000;

        long maxId = generateId();

        for (int i = 0; i < users.size(); i += batchSize) {
            int index = i;

            final List<OracleUser> batch = users.subList(i, Math.min(users.size(), i + batchSize));

            jdbcTemplate.batchUpdate(UPDATE_ALL_SQL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int j) throws SQLException {
                    OracleUser user = batch.get(j);

                    if (user.getId() == null) {
                        user.setId(maxId + index + j);
                    }

                    ps.setString(1, user.getName());
                    ps.setString(2, user.getUsername());
                    ps.setString(3, user.getPassword());
                    ps.setLong(4, user.getId());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_SQL);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }

    @Override
    public void deleteByUsername(String username) {
        jdbcTemplate.update(DELETE_BY_USERNAME_SQL, username);
    }

    @Override
    public void deleteByName(String name) {
        jdbcTemplate.update(DELETE_BY_NAME_SQL, name);
    }

    private synchronized Long generateId() {
        return jdbcTemplate.queryForObject(GENERATE_ID_SQL, Long.class);
    }
}

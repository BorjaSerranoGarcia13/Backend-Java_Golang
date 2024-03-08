package com.bs.dbperformancemetrics.repository.oracle.plsql;

import com.bs.dbperformancemetrics.model.OracleUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FindUserByUsernameStoredProcedure extends BaseStoredProcedure {
    public FindUserByUsernameStoredProcedure(DataSource dataSource) {
        super(dataSource, "FIND_USER_BY_USERNAME");
    }

    @Override
    protected void declareParameters() {
        declareParameter(new SqlParameter("username", Types.VARCHAR));
        declareParameter(new SqlReturnResultSet("user", new UserRowMapper()));
    }
    public OracleUser execute(String username) {
        Map<String, Object> results = super.execute(username);
        List<?> tempList = (List<?>) results.get("user");
        if (tempList.isEmpty() || !(tempList.get(0) instanceof OracleUser)) {
            throw new IllegalArgumentException("User not found or wrong type");
        }
        return (OracleUser) tempList.get(0);
    }
    private static final class UserRowMapper implements RowMapper<OracleUser> {
        public OracleUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            OracleUser user = new OracleUser();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));

            Array friendsArray = rs.getArray("friends");
            if (friendsArray != null) {
                Long[] friends = (Long[]) friendsArray.getArray();
                user.setFriendIds(Arrays.asList(friends));
            } else {
                user.setFriendIds(new ArrayList<>());
            }

            return user;
        }
    }
}
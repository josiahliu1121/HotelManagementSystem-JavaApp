package com.utils;

import com.config.AppConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcExecutor {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    public JdbcExecutor() {
        AppConfig appConfig = new AppConfig();

        URL = appConfig.getProperty("database.url");
        USER = appConfig.getProperty("database.username");
        PASSWORD = appConfig.getProperty("database.password");
    }

    /**
     * Functional interface to map ResultSet rows to Java objects.
     */
    public interface ResultMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    /**
     * Executes a SELECT query and maps the results using the given ResultMapper.
     * @param sql The SQL query with placeholders (?)
     * @param mapper A functional interface for mapping ResultSet to Java objects
     * @param params The parameters to be set in the query
     * @return A list of mapped objects
     */
    public <T> List<T> executeQuery(String sql, ResultMapper<T> mapper, Object... params) {
        List<T> resultList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParameters(pstmt, params);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                resultList.add(mapper.map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE SQL statement.
     * @param sql The SQL query with placeholders (?)
     * @param params The parameters to be set in the query
     * @return The number of affected rows
     */
    public int executeUpdate(String sql, Object... params) {
        int rowsAffected = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setParameters(pstmt, params);
            rowsAffected = pstmt.executeUpdate();

            // If it's an INSERT, get generated keys (optional)
            if (sql.trim().toLowerCase().startsWith("insert")) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println("Inserted ID: " + generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowsAffected;
    }

    /**
     * Helper method to set parameters in a PreparedStatement.
     */
    private void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }
}

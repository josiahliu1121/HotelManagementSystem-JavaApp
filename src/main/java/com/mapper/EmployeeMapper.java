package com.mapper;

import com.pojo.Employee;
import com.utils.JdbcExecutor;
import com.utils.MD5Util;

import java.util.List;

public class EmployeeMapper implements Mapper{
    public static EmployeeMapper instance = new EmployeeMapper();
    private final static JdbcExecutor jdbcExecutor = new JdbcExecutor();
    private final static JdbcExecutor.ResultMapper resultMapper = rs -> new Employee(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("name"),
            rs.getString("phone"),
            rs.getInt("gender"),
            rs.getTimestamp("create_time").toLocalDateTime()
    );

    @Override
    public void delete(Long id) {
        String sql = "delete from employee where id = ?";
        int rows = jdbcExecutor.executeUpdate(sql, id);

        System.out.println("Deleted rows: " + rows);
    }

    public List<Employee> findAll() {
        String sql = "select * from employee";
        return jdbcExecutor.executeQuery(sql, resultMapper);
    }

    public List<Employee> findByUsername(String username) {
        String sql = "select * from employee where username like concat('%',?,'%')";
        return jdbcExecutor.executeQuery(sql, resultMapper, username);
    }

    public void insert(Employee employee) {
        // Hash password with MD5
        String hashedPassword = MD5Util.md5(employee.getPassword());

        String sql = "insert into employee (username, password, name, phone, gender, create_time) values(?,?,?,?,?,?)";
        int rows = jdbcExecutor.executeUpdate(sql, employee.getUserName(), hashedPassword, employee.getName(),
                employee.getPhone(), employee.getGender(), employee.getCreateTime());

        System.out.println("Inserted rows: " + rows);
    }

    public void update(Employee employee) {
        //only name phone gender can be update
        String sql = "update employee set name = ?, phone = ?, gender = ? where id = ?";
        int rows = jdbcExecutor.executeUpdate(sql, employee.getName(), employee.getPhone(), employee.getGender(), employee.getId());

        System.out.println("Updated rows: " + rows);
    }

    public List<String> findUserName() {
        JdbcExecutor.ResultMapper resultMapper = rs -> rs.getString("username");

        String sql = "select username from employee";
        return jdbcExecutor.executeQuery(sql, resultMapper);
    }
}

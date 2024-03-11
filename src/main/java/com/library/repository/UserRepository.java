package com.library.repository;

import com.library.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    public static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setAdmin(rs.getBoolean("is_admin"));
        user.setBirthDate(rs.getDate("birth_date"));
        user.setMaster(rs.getBoolean("master"));
        return user;
    };

    public List<User> findAll() {
        String sql = "SELECT user_id, last_name,first_name,birth_date, email, password ,is_admin,master FROM users ORDER BY user_id";
        List<User> userList = template.query(sql, USER_ROW_MAPPER);
        return userList;
    }

    public void insert(User user) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("lastName", user.getLastName())
                .addValue("firstName", user.getFirstName()).addValue("birthDate", user.getBirthDate())
                .addValue("email", user.getEmail()).addValue("password", user.getPassword());
        String sql = "INSERT into users(last_name,first_name,birth_date, email, password)" +
                "VALUES(:lastName,:firstName,:birthDate,:email,:password)";
        template.update(sql, param);
    }

    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT user_id,first_name,last_name,is_admin,birth_date,master,email,password FROM users WHERE email=:email AND password=:password";
        SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password", password);
        List<User> usersList = template.query(sql, param, USER_ROW_MAPPER);
        if (usersList.size() == 0) {
            return null;
        }
        System.out.println("findByEmailAndPassword: " + email + ", " + password);
        return usersList.get(0);
    }

    public User findByEmail(String email) {
        String sql = "SELECT user_id,first_name,last_name,is_admin,birth_date,master,email,password FROM users WHERE email=:email";
        SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
        List<User> usersList = template.query(sql, param, USER_ROW_MAPPER);
        if (usersList.size() == 0) {
            return null;
        }
        System.out.println("findByEmail: " + email);
        return usersList.get(0);
    }

    public void delete(Integer userId){
        String sql = "DELETE FROM users WHERE user_id=:userId";

        SqlParameterSource param = new MapSqlParameterSource().addValue("user_id",userId);
        template.update(sql,param);

    }
}

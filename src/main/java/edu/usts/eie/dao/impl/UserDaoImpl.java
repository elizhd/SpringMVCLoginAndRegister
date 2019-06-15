package edu.usts.eie.dao.impl;

import edu.usts.eie.dao.IUserDao;
import edu.usts.eie.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;

public class UserDaoImpl implements IUserDao {

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public User findByNameAndPassword(String name, String password) {
        User user;
        String sql = "SELECT * FROM user WHERE name=? AND password=?";
        Object[] obj = new Object[]{name, password};
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        try {
            user = jdbcTemplate.queryForObject(sql, rowMapper, obj);
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    @Override
    public int insertUser(User user) {
        int result = 0;
        String sql = "insert into user(name, password, email, role) values(?,?,?,false)";
        Object[] obj = new Object[]{
                user.getName(),
                user.getPassword(),
                user.getEmail(),
        };
        try {
            result = jdbcTemplate.update(sql, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public User findUserByName(String name) {
        User user;
        String sql = "SELECT * FROM user WHERE name=?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        try {
            user = jdbcTemplate.queryForObject(sql, rowMapper, name);
        } catch (Exception e) {
            return null;
        }
        return user;
    }
}

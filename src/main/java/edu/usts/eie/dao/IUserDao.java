package edu.usts.eie.dao;


import edu.usts.eie.model.User;
import org.apache.ibatis.annotations.Param;


/**
 * @author : Haodong Zhao
 * @date : 2019/5/19
 * @time : 16:17
 */
public interface IUserDao {
    User findByNameAndPassword(@Param(value = "name") String name, @Param(value = "password") String password);
    int insertUser(User user);
    User findUserByName(String name);

}

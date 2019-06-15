package edu.usts.eie.service;


import edu.usts.eie.model.User;

/**
 * @author : Haodong Zhao
 * @date : 2019/5/19
 * @time : 17:49
 */
public interface IUserService {
    User findByNameAndPassword(String name, String password);

    int insertUser(User user);

    User findUserByName(String name);
}

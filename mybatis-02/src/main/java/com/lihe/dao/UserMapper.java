package com.lihe.dao;

import com.lihe.pojo.User;

import java.util.List;
import java.util.Map;

// Dao等价于MyBatis的mapper，以后写mapper
public interface UserMapper {

    // 查询全部用户
    List<User> getUserList();

    // 根据ID查询用户
    User getUserByID(int id);

    // insert 一个用户

    int addUser(User user);


    // 修改用户
    int updateUser(User user);

    int deleteUser(int id);

}

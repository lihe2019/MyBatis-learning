package com.lihe.dao;

import com.lihe.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

// Dao等价于MyBatis的mapper，以后写mapper
public interface UserMapper {
    @Select("select * from user")
    List<User> getUsers();
}

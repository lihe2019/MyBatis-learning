package com.lihe.dao;

import com.lihe.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;


// Dao等价于MyBatis的mapper，以后写mapper
public interface UserMapper {
    @Select("select * from user")
    List<User> getUsers();

    @Select("select * from user where id = #{id}")
    User getUSerByID(@Param("id") int id);

    @Insert("insert into user(id,name,pwd) values ()")
    int addUser(User user);
}

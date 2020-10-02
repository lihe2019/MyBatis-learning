package com.lihe.dao;

import com.lihe.pojo.User;
import com.lihe.utils.MyBatisUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMapperTest {


    static Logger logger = Logger.getLogger((UserMapperTest.class));

    @Test
    public void getUserById(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserByID(1);

        // select * from mybatis.user where id = #{id}
        // select id, name, pwd from mybatis.user where id = #{id}
        // select id, name, pwd as password from mybatis.user where id = #{id}

        System.out.println(user);

        sqlSession.close();
    }
    @Test
    public void testLog4j(){
        logger.info("info: 进入了testLog4j");
        logger.debug("debug: 进入了testLog4j");
        logger.error("error: 进入了testLog4j");
    }

    @Test
    public void getUserByLimit(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("startIndex", 0);
        map.put("pageSize", 2);
        List<User> userList = mapper.getUserByLimit(map);
        for (User user : userList) {
            System.out.println(user);
        }

        sqlSession.close();
    }

    @Test
    public void getUserByRowBounds(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        // 通过RowBounds实现
        RowBounds rowBounds = new RowBounds(1, 2);

        // 通过java代码层面实现分页
        List<User> userList = sqlSession.selectList("com.lihe.dao.UserMapper.getUserByRowBounds", null, rowBounds);

        for (User user : userList) {
            System.out.println(user);
        }

        sqlSession.close();
    }

}

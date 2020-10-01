package com.lihe.dao;

import com.lihe.pojo.User;
import com.lihe.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.List;


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

}

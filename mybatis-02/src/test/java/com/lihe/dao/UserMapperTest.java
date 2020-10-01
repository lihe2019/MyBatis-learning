package com.lihe.dao;

import com.lihe.pojo.User;
import com.lihe.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class UserMapperTest {



    @Test
    public void test(){
        // 第一步：获取sqlSession对象
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        //  执行SQL
        try {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = mapper.getUserList();
            for (User user : userList) {
                System.out.println(user);
            }
        }catch (Exception e){

        }finally {
            // 关闭sqlSession
            sqlSession.close();
        }
        // 官方建议用try catch


        //  方法2
//        List<User> userList = sqlSession.selectList("com.lihe.dao.USerDao.getUserList");

    }
    @Test
    public void getUserById(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserByID(1);
        System.out.println(user);

        sqlSession.close();
    }
    @Test
    public void addUser(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int number =  mapper.addUser(new User(4, "哈哈", "1234564"));
        if (number > 0){
            System.out.println("插入成功！");

        }

        sqlSession.commit();

        sqlSession.close();
    }



    @Test
    public void updateUser(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int number =  mapper.updateUser(new User(1, "李赫", "1234564"));
        if (number > 0){
            System.out.println("修改成功！");

        }

        sqlSession.commit();

        sqlSession.close();
    }

    @Test
    public void deleteUser(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.deleteUser(4);
        sqlSession.commit();
        sqlSession.close();
    }
}

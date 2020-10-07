import com.lihe.dao.UserMapper;
import com.lihe.pojo.User;
import com.lihe.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class MyTest {
    @Test
    public void UserMapper(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        // 底层主要应用反射
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = mapper.getUsers();

        for (User user : users) {
            System.out.println(user);
        }
        sqlSession.close();
    }
    @Test
    public void getUserByID(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        // 底层主要应用反射
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user =  mapper.getUSerByID(1);

        System.out.println(user);
        sqlSession.close();
    }
}

import com.lihe.dao.StudentMapper;
import com.lihe.dao.TeacherMapper;
import com.lihe.pojo.Student;
import com.lihe.pojo.Teacher;
import com.lihe.utils.MyBatisUtils;
import com.sun.javaws.IconUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class MyTest {
    public static void main(String[] args) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacher(1);
        System.out.println(teacher);
        sqlSession.close();
    }

    @Test
    public void testGetStudent(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> student = mapper.getStudent();

        for (Student student1 : student) {
            System.out.println(student1);
        }
        sqlSession.close();
    }

    @Test public void testStudent2(){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> student = mapper.getStudent2();

        for (Student student1 : student) {
            System.out.println(student1);
        }
        sqlSession.close();
    }
}

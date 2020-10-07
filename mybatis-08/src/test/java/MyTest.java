import com.lihe.dao.BlogMapper;
import com.lihe.pojo.Blog;
import com.lihe.utils.IDUtils;
import com.lihe.utils.MyBatisUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyTest {
    @Test
    public void addInitBlog(){
        SqlSession session = MyBatisUtils.getSqlSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);

        Blog blog = new Blog();
        blog.setId(IDUtils.getId());
        blog.setTitle("Mybatissosimple");
        blog.setAuthor("kuangshenshuo");
        blog.setCreateTime(new Date());
        blog.setViews(9999);

        mapper.addBlog(blog);

        blog.setId(IDUtils.getId());
        blog.setTitle("Javasosimple");
        mapper.addBlog(blog);

        blog.setId(IDUtils.getId());
        blog.setTitle("Springsosimple");
        mapper.addBlog(blog);

        blog.setId(IDUtils.getId());
        blog.setTitle("littleservicesosimple");
        mapper.addBlog(blog);

        session.close();
    }

    @Test
    public void queryBlogIF()
    {
        SqlSession session = MyBatisUtils.getSqlSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);
        HashMap hashMap = new HashMap();

        hashMap.put("author","kuangshenshuo");
        hashMap.put("id", "1");
        hashMap.put("title", "Javasosimple22");
        List<Blog> blogs = mapper.queryBlogIF(hashMap);

        for (Blog blog : blogs) {
            System.out.println(blog);
        }
        session.close();


    }


}

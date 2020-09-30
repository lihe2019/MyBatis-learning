# MyBatis-9.28

环境：

- JDK 1.8
- Mysql 5.7
- maven 3.6.1
- IDEA

回顾：

- JDBC
- Mysql
- Java基础
- Maven
- Junit



SSM框架：配置文件。最好的方式：看官网文档；

https://mybatis.org/mybatis-3/zh/getting-started.html

## 1. 简介

### 1.1 什么是MyBatis

![image-20200930103257105](C:\Users\91156\AppData\Roaming\Typora\typora-user-images\image-20200930103257105.png)

- MyBatis 是一款优秀的 **持久层框架**
- 它支持自定义 SQL、存储过程以及高级映射。
- MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。
- MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。
- MyBatis 本是[apache](https://baike.baidu.com/item/apache/6265)的一个开源项目[iBatis](https://baike.baidu.com/item/iBatis), 2010年这个项目由apache software foundation 迁移到了google code，并且改名为MyBatis 。
- 2013年11月迁移到Github。



如何获取MyBatis？

- Maven仓库

  ```xml
  <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
  <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.2</version>
  </dependency>
  ```

- GitHub https://github.com/mybatis/mybatis-3

- 中文文档

### 1.2 持久化

数据持久化

- 持久化㐊将程序的数据在持久状态和瞬时状态转化的过程
- 内存：**断电即失**
- 数据库（jdbc）， io文件持久化（特别浪费资源）。
- 生活中：冷藏、罐头。

**为什么要持久化？**

- 有一些对象，不能让他丢失
- 内存太贵了



### 1.3 持久层

Dao层，Service层， Controller层...

- 完成持久化工作的代码块
- 层是界限明显的。

### 1.4 为什么需要MyBatis

- 帮助程序员将数据存入到数据库中。
- 方便
- 传统的JDBC代码太复杂了。简化。框架。自动化。
- 不用MyBatis也可以。更容易上手。**技术没有高低之分** 关键在于人
- 优点：
  - 简单易学：本身就很小且简单。没有任何第三方依赖，最简单安装只要两个jar文件+配置几个sql映射文件易于学习，易于使用，通过文档和源代码，可以比较完全的掌握它的设计思路和实现。
  - 灵活：mybatis不会对应用程序或者数据库的现有设计强加任何影响。 sql写在xml里，便于统一管理和优化。通过sql语句可以满足操作数据库的所有需求。
  - 解除sql与程序代码的耦合：通过提供DAO层，将业务逻辑和数据访问逻辑分离，使系统的设计更清晰，更易维护，更易单元测试。sql和代码的分离，提高了可维护性。
  - 提供映射标签，支持对象与数据库的orm字段关系映射
  - 提供对象关系映射标签，支持对象关系组建维护
  - 提供xml标签，支持编写动态sql。

**最重要的一点：使用的人多！**

Spring SpringMVC  SpringBoot



## 2.第一个MyBatis程序

思路：搭建环境 --> 导入MyBatis --> 编写代码 --> 测试！

### 2.1 搭建环境

搭建数据库

```mysql
CREATE DATABASE `MyBatis`;

USE `MyBatis`;

CREATE TABLE `user`(
	`id` INT(20) NOT NULL,
	`name` VARCHAR(30) DEFAULT NULL,
	`pwd` VARCHAR(30) DEFAULT NULL,
	PRIMARY KEY(`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT into `user` (`id`, `name`, `pwd`) VALUES
(1, 'lihe', '123456'),
(2, 'lihe22', '12asd34asd56'),
(3, 'lihe33', '12345asd6')
```

新建项目

1. 新建一个普通的Maven项目

2. 删除src目录（父工程）

3. 导入Maven依赖

   ```xml
   		<dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version>5.1.47</version>
           </dependency>
           <dependency>
               <groupId>org.mybatis</groupId>
               <artifactId>mybatis</artifactId>
               <version>3.5.2</version>
           </dependency>
           <dependency>
               <groupId>junit</groupId>
               <artifactId>junit</artifactId>
               <version>4.12</version>
               <scope>test</scope>
           </dependency>
   ```

### 2.2 创建一个模块

- 编写MyBatis的核心配置文件

- 编写MyBatis工具类

  ```java
  // sqlSessionFactory -- >  sqlSession
  public class MyBatisUtils {
      // 提升作用域技巧，很常用
      private static SqlSessionFactory sqlSessionFactory;
  
      static {
          try {
              // 获取sql session factory对象
              // 直接从官网得到的，所以可以写成工具类
              String resource = "mybatis-config.xml";
              InputStream inputStream = Resources.getResourceAsStream(resource);
              SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
  
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      // 既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
      // SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。你可以通过 SqlSession 实例来直接执行已映射的 SQL 语句。
      public static SqlSession getSqlSession(){
          return sqlSessionFactory.openSession();
      }
  }
  ```

  



### 2.3 编写代码

- 实体类

  ```java
  // 实体类
  public class User {
      private int id;
      private String name;
      private String pwd;
  
      public User() {
      }
  
      public User(int id, String name, String pwd) {
          this.id = id;
          this.name = name;
          this.pwd = pwd;
      }
  
      public int getId() {
          return id;
      }
  
      public void setId(int id) {
          this.id = id;
      }
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  
      public String getPwd() {
          return pwd;
      }
  
      public void setPwd(String pwd) {
          this.pwd = pwd;
      }
  
      @Override
      public String toString() {
          return "User{" +
                  "id=" + id +
                  ", name='" + name + '\'' +
                  ", pwd='" + pwd + '\'' +
                  '}';
      }
  }
  ```

  

- Dao接口

  ```java
  // Dao等价于MyBatis的mapper，以后写mapper
  public interface UserDao {
      List<User> getUser();
  }
  ```

  

- 接口实现类  由原来的Impl转换为一个Mapper配置文件

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <!-- namespace= 绑定一个对应的Dao/Mapper接口-->
  <mapper namespace="org.lihe.dao.UserDao">
      <!--这里的id对应原来的方法名-->
      <!--所有的xml配置都是空格里面加东西，属性-->
      <!--resultType现在先写类名，不要写集合-->
  
      <select id="getUserList" resultType="com.lihe.pojo.User">
          select * from mybatis.user;
      </select>
  </mapper>
  ```

### 2.4 测试

注意点：org.apache.ibatis.binding.BindingException: Type interface com.kuang.dao.UserDao is not known to theMapperRegistry.

MapperRegistry是什么

核心配置文件中注册mappers

Junit

==这里没通过，还不知道为什么==

可以通过但是配置文件找不到的问题没解决

- Junit测试

  ```java
  public class UserDaoTest {
      @Test
      public void test(){
          // 第一步：获取sqlSession对象
          SqlSession sqlSession = MyBatisUtils.getSqlSession();
          //  执行SQL
          UserDao mapper = sqlSession.getMapper(UserDao.class);
          List<User> userList = mapper.getUserList();
          for (User user : userList) {
              System.out.println(user);
          }
  
          // 关闭sqlSession
          sqlSession.close();
  
      }
  }
  ```

可能遇到的问题：

1. 配置文件没有注册
2. 绑定接口错误
3. 方法名不对
4. 返回类型不对
5. Maven导出资源问题

## 3. CRUD

### 3.1 namespace

namespace中的包名要和Dao/Mapper接口的包名一直！

### 3.2 select

选择、查询语句；

- id：就是对应的namespace中的方法名
- resultType：sql语句的返回值！ parameterType
- parameterType：参数类型



1. 编写接口

2. 编写对应的mapper中的sql语句

   ```xml
       <select id="getUserByID" parameterType="int" resultType="com.lihe.pojo.User">
           select * from mybatis.user where id = #{id}
       </select>
   ```

   

3. 测试

   ```java
   @Test
       public void getUserById(){
           SqlSession sqlSession = MyBatisUtils.getSqlSession();
   
           UserMapper mapper = sqlSession.getMapper(UserMapper.class);
           User user = mapper.getUserByID(1);
           System.out.println(user);
   
           sqlSession.close();
       }
   ```

   

### 3.3 Insert

```xml
<insert id="addUser" parameterType="com.lihe.pojo.User">
        insert into mybatis.user (id, name, pwd) value (#{id}, #{name}, #{pwd});
    </insert>
```



### 3.4 Update

```xml
<update id="updateUser" parameterType="com.lihe.pojo.User">
        update mybatis.user set name=#{name},pwd=#{pwd} =  where id = #{id};
    </update>
```



### 3.5 Delete

```xml
<delete id="deleteUser" parameterType="int">
        delete from mybatis.user where id = #{id};
    </delete>
```

注意点：

- 增删需要提交事务



==我的update有问题，还不知道为什么==

### 3.6 分析错误

- 标签不要匹配错
- resource绑定mapper，需要路径（ / ） ==读错误从后往前==
- 程序配置文件必须符合规范！
- NullPointerException，没有注册到资源
- 输出的xml文件中存在乱码问题！
- maven

### 3.7 万能Map

假设，我们的实体类，或者数据库中的表，字段或者参数过多，我们应该考虑使用map，甚至可以全用map，但是不正规，野路子。

可定制化，map传递参数，直接在sql中取出key即可！【parameterType="map"】

对象传递参数，直接取对象的属性即可！【parameterType="com.lihe.pojo.User"】

只有一个参数的情况下，可以直接在sql中取到！【可以不写】

多个参数用map或**注解**

### 3.8 思考题？

模糊查询怎么写？

1. Java代码执行的时候，传递通配符% %  【更安全】
2. 在sql拼接中使用通配符！


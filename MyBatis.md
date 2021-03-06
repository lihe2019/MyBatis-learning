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
  
      <select id="getUserList" resultType="com.User">
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
   <select id="getUserByID" parameterType="int" resultType="com.User">
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
<insert id="addUser" parameterType="com.User">
    insert into mybatis.user (id, name, pwd) value (#{id}, #{name}, #{pwd});
</insert>
```



### 3.4 Update

```xml
<update id="updateUser" parameterType="com.User">
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

对象传递参数，直接取对象的属性即可！【parameterType="com.User"】

只有一个参数的情况下，可以直接在sql中取到！【可以不写】

多个参数用map或**注解**

### 3.8 思考题？

模糊查询怎么写？

1. Java代码执行的时候，传递通配符% %  【更安全】
2. 在sql拼接中使用通配符！



## 4. 配置解析

### 4.1 核心配置文件

- mybatisConfig.xml

- MyBatis 的配置文件包含了会深深影响 MyBatis 行为的设置和属性信息。

  ```xml
  configuration（配置）
  properties（属性）
  settings（设置）
  typeAliases（类型别名）
  typeHandlers（类型处理器）
  objectFactory（对象工厂）
  plugins（插件）
  environments（环境配置）
  environment（环境变量）
  transactionManager（事务管理器）
  dataSource（数据源）
  databaseIdProvider（数据库厂商标识）
  mappers（映射器）
  ```



### 4.2 环境变量（environments）

MyBatis 可以配置成适应多种环境，**但每个 SqlSessionFactory 实例只能选择一种环境。**

学会使用配置多套环境。

MyBatis默认的事务管理器是JDBC，连接池：POOLED。

### 4.3 属性（properties）

我们可以通过properties属性来实现引用配置文件

这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置。【db.properties】

编写一个配置文件

db.properties

```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?
# 这里useSSL必须单独一行，不知道为什么
useSSL=tue&useUnicode=true&characterEncoding=UTF-8
username=root
password=root
```

在核心配置文件中引入（在cml中，元素要遵守顺序，properties要放在第一行）

![image-20201001104642694](C:\Users\91156\AppData\Roaming\Typora\typora-user-images\image-20201001104642694.png)

```xml
<properties resource="db.properties">
    <property name="username" value="root"/>
    <property name="password" value="root"/>
</properties>
```

注意点：

- 可以直接引用外部文件
- 可以在其中增加一些属性配置
- 如果两个文件都有同一字段，优先使用外部配置文件的！



### 4.4 类型别名（typeAliases）

- 类型别名可为 Java 类型设置一个缩写名字。 
- 它仅用于 XML 配置，意在降低冗余的全限定类名书写。

```xml
<typeAliases>
    <typeAlias type="com.User" alias="User"/>
</typeAliases>
```

也可以指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean

扫描实体类的包，它的默认别名就是这个类名的别名，首字母小写（其实实际上大写也可以）！

```xml
<typeAliases>
    <package name="com.lihe.pojo"/>
</typeAliases>
```

在实体类比较少的时候，使用第一种；

实体类比较多，建议使用第二种。

第一种可以DIY别名。第二种不行（但可以用注解Alias）。




### 4.5 设置

这是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。



### 4.6 其他配置

- [typeHandlers（类型处理器）](https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers)

- [objectFactory（对象工厂）](https://mybatis.org/mybatis-3/zh/configuration.html#objectFactory)

- [plugins（插件）](https://mybatis.org/mybatis-3/zh/configuration.html#plugins)

  - mybatis-generator-core

  - mybatis-plus

  - 通用mapper

    ![image-20201001112844861](C:\Users\91156\AppData\Roaming\Typora\typora-user-images\image-20201001112844861.png)

    ![image-20201001112932861](C:\Users\91156\AppData\Roaming\Typora\typora-user-images\image-20201001112932861.png)



### 4.7 映射器

MapperResgistry：注册绑定我们的Mapper文件；

方式1：通过资源文件（注意，不是. 而是/）【推荐】

```xml
<mappers>
    <mapper resource="com/lihe/dao/USerMapper.xml"/>
</mappers>
```

方式2：通过class文件绑定注册

```xml
<mappers>
    <mapper class="com.UserMapper"/>
</mappers>
```

注意点：

- 接口和它的Mapper配置文件必须同名！
- 接口和他的Mapper配置文件必须在同一路径！

方式3：使用扫描包进行绑定

```xml
<mappers>
    <package name="com.lihe.dao"/>
</mappers>
```

和方式2的注意点一样



练习时间：

- 将数据库配置文件外部引入
- 实体类别名
- 保证UserMapper接口和UserMapper.xml配置文件同名且放在同一包内。

### 4.8 生命周期（Scope）和作用域

<img src="C:\Users\91156\AppData\Roaming\Typora\typora-user-images\image-20201001115350237.png" alt="image-20201001115350237" style="zoom:50%;" />

不同作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的**并发问题**。

**SqlSessionFactoryBuilder：**

- 一旦创建了 SqlSessionFactory（Builder），就不再需要它了。
- 局部变量

**SqlSessionFactory**：

- 说白了，就是可以想象为：数据库连接池
- SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，**没有任何理由丢弃它或重新创建另一个实例**。
- 多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。**因此 SqlSessionFactory 的最佳作用域是应用作用域**。
- 最简单的就是使用**单例模式**或者静态单例模式。

**SqlSession**：

- 可以理解为，连接到连接池的一个请求！
- SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。
- 用完之后需要赶紧关闭，否则资源被占用！
-  如果你现在正在使用一种 Web 框架，考虑将 SqlSession 放在一个和 HTTP 请求相似的作用域中。 换句话说，每次收到 HTTP 请求，就可以打开一个 SqlSession，返回一个响应后，就关闭它。 这个关闭操作很重要，为了确保每次都能执行关闭操作，你应该把这个关闭操作放到 finally 块中。

<img src="C:\Users\91156\AppData\Roaming\Typora\typora-user-images\image-20201001120407128.png" alt="image-20201001120407128" style="zoom: 50%;" />

这里的每个Mapper，就代表每一个业务！



## 5. 解决属性名和字段名不一致的问题

### 5.1 问题

目前数据库中的字段和属性名一致

新建一个项目，拷贝之前的，测试实体类字段不一致的情况

```java
public class User {
    private int id;
    private String name;
    private String password;
}
```

测试出现问题

![image-20201001144038254](C:\Users\91156\AppData\Roaming\Typora\typora-user-images\image-20201001144038254.png)



解决方法：

- 起别名

  ```xml
  <select id="getUserByID" parameterType="int" resultType="com.User">
      select id, name, pwd as password from mybatis.user where id = #{id}
  </select>
  ```



### 5.2 resultMap

结果集映射

|  id  | name | pwd  |
| :--: | :--: | :--: |
|  id  | name | pwd  |

```xml
<!--结果映射-->
<resultMap id="UserMap" type="user">
    <!--colume数据库中的字段，property实体类中的属性-->
    <result column="id" property="id"/>
    <result column="name" property="name"/>
    <result column="pwd" property="password"/>
</resultMap>
```

- `resultMap` 元素是 MyBatis 中最重要最强大的元素。
- ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。
- 你会发现上面的例子没有一个需要显式配置 `ResultMap`，这就是 `ResultMap` 的优秀之处——你完全可以不用显式地配置它们。 （只需要映射不一样的字段）
- 世界总是这么简单就好了

<img src="C:\Users\91156\AppData\Roaming\Typora\typora-user-images\image-20201001162358474.png" alt="image-20201001162358474" style="zoom:50%;" />

## 6. 日志

### 6.1 日志工厂

如果一个数据库操作，出现了异常，我们需要排错。日志就是最好的帮手。

曾经：sout、debug

现在：日志工厂！

![image-20201001163216002](C:\Users\91156\AppData\Roaming\Typora\typora-user-images\image-20201001163216002.png)

- SLF4J

- LOG4J【掌握】

- LOG4J2

- JDK_LOGGING

- COMMONS_LOGGING

- STDOUT_LOGGING【掌握】

- NO_LOGGING



在MyBatis中具体使用哪个日志实现，在设置中设定！

STDOUT_LOGGING 标准日志输出

在MyBatis核心配置文件中，配置我们的日志！

```xml
<settings>
    <setting name="logImpl" value="STDOUT_LOGGING"/>
</settings>
```

<img src="C:\Users\91156\AppData\Roaming\Typora\typora-user-images\image-20201001164130538.png" alt="image-20201001164130538" style="zoom:67%;" />

### 6.2 Log4j

标准日志比较麻烦

什么事log4j？

- Log4j是[Apache](https://baike.baidu.com/item/Apache/8512995)的一个开源项目，通过使用Log4j，我们可以控制日志信息输送的目的地是[控制台](https://baike.baidu.com/item/控制台/2438626)、文件、[GUI](https://baike.baidu.com/item/GUI)组件，甚至是套接口服务器、[NT](https://baike.baidu.com/item/NT/3443842)的事件记录器、[UNIX](https://baike.baidu.com/item/UNIX) [Syslog](https://baike.baidu.com/item/Syslog)[守护进程](https://baike.baidu.com/item/守护进程/966835)等
- 我们也可以控制每一条日志的输出格式；
- 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程。
- 最令人感兴趣的就是，这些可以通过一个[配置文件](https://baike.baidu.com/item/配置文件/286550)来灵活地进行配置，而不需要修改应用的代码。



1. 先导入log4j的包

   ```xml
   <!-- https://mvnrepository.com/artifact/log4j/log4j -->
   <dependency>
       <groupId>log4j</groupId>
       <artifactId>log4j</artifactId>
       <version>1.2.17</version>
   </dependency>
   ```

2. log4j.properties

   ```properties
   #将等级为DEBUG的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
   log4j.rootLogger=DEBUG,console,file
   
   #控制台输出的相关设置
   log4j.appender.console = org.apache.log4j.ConsoleAppender
   log4j.appender.console.Target = System.out
   log4j.appender.console.Threshold=DEBUG
   log4j.appender.console.layout = org.apache.log4j.PatternLayout
   log4j.appender.console.layout.ConversionPattern=【%c】-%m%n
   
   #文件输出的相关设置
   log4j.appender.file = org.apache.log4j.RollingFileAppender
   log4j.appender.file.File=./log/kuang.log
   log4j.appender.file.MaxFileSize=10mb
   log4j.appender.file.Threshold=DEBUG
   log4j.appender.file.layout=org.apache.log4j.PatternLayout
   log4j.appender.file.layout.ConversionPattern=【%p】【%d{yy-MM-dd}】【%c】%m%n
   
   #日志输出级别
   log4j.logger.org.mybatis=DEBUG
   log4j.logger.java.sql=DEBUG
   log4j.logger.java.sql.Statement=DEBUG
   log4j.logger.java.sql.ResultSet=DEBUG
   log4j.logger.java.sql.PreparedStatement=DEBUG
   ```

3. 配置log4j为日志的实现

   ```xml
   <settings>
       <setting name="logImpl" value="LOG4J"/>
   </settings>
   ```

4. Log4j的使用！直接测试运行刚才的查询

**简单使用**

1. 在要使用log4j的类中，导入包 import org.apache.log4j.Logger;

2. 日志对象，加载为当前类的class

   ```java
   static Logger logger = Logger.getLogger((UserMapperTest.class));
   ```

3. 日志级别

   ```java
   logger.info("info: 进入了testLog4j");
   logger.debug("debug: 进入了testLog4j");
   logger.error("error: 进入了testLog4j");
   ```




## 7. 分页

**思考：为什么要分页？**

- 减少数据的处理量



### 7.1 **使用Limit分页**

语法：

```sql
select * from user limit startIndex,pageSize;
```

使用MyBatis实现分页，核心就是SQL

1. 接口

   ```java
   // 分页
   List<User> getUserByLimit(Map<String, Integer> map);
   ```

2. Mapper.xml

   ```xml
   <!--    分页实现查询-->
   <select id="getUserByLimit" parameterType="map" resultMap="UserMap">
       SELECT * FROM user limit #{startIndex},#{pageSize}
   </select>
   ```

3. 测试

   ```java
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
   ```

### 7.2 RowBounds分页

面向对象思想，但是不建议使用

不再使用SQL实现分页

1. 接口

   ```java
   // 分页2
   List<User> getUserByRowBounds();
   ```

2. MApper.xml

   ```xml
   <!--    分页2-->
   <select id="getUserByLimit" resultMap="UserMap">
       SELECT * FROM user
   </select>
   ```

3. 测试

   ```java
   @Test
   public void getUserByRowBounds(){
       SqlSession sqlSession = MyBatisUtils.getSqlSession();
   
       // 通过RowBounds实现
       RowBounds rowBounds = new RowBounds(1, 2);
   
       // 通过java代码层面实现分页
       List<User> userList = sqlSession.selectList("com.UserMapper.getUserByRowBounds", null, rowBounds);
   
       for (User user : userList) {
           System.out.println(user);
       }
   
       sqlSession.close();
   }
   ```

### 7.3 分页插件

<img src="MyBatis.assets/image-20201002181628220.png" alt="image-20201002181628220" style="zoom: 50%;" />

了解即可，玩意以后公司的架构师说要使用，你要知道他是什么东西！



## 8. 使用注解开发

### 8.1 面向接口编程

- 大家之前都学过面向对象编程，也学习过接口，但在真正的开发中，很多时候我们会选择面向接口编程
- **根本原因 :  解耦 , 可拓展 , 提高复用 , 分层开发中 , 上层不用管具体的实现 , 大家都遵守共同的标准 , 使得开发变得容易 , 规范性更好**
- 在一个面向对象的系统中，系统的各种功能是由许许多多的不同对象协作完成的。在这种情况下，各个对象内部是如何实现自己的,对系统设计人员来讲就不那么重要了；
- 而各个对象之间的协作关系则成为系统设计的关键。小到不同类之间的通信，大到各模块之间的交互，在系统设计之初都是要着重考虑的，这也是系统设计的主要工作内容。面向接口编程就是指按照这种思想来编程。



**关于接口的理解**

- 接口从更深层次的理解，应是**定义（规范，约束）与实现（名实分离的原则）的分离**。

- 接口的本身反映了系统设计人员对系统的抽象理解。

- 接口应有两类：

- - 第一类是对一个个体的抽象，它可对应为一个抽象体(abstract class)；
  - 第二类是对一个个体某一方面的抽象，即形成一个抽象面（interface）；

- 一个体有可能有多个抽象面。抽象体与抽象面是有区别的。

架构师（成千上万的接口）



**三个面向区别**

- 面向对象是指，我们考虑问题时，以对象为单位，考虑它的属性及方法 .
- 面向过程是指，我们考虑问题时，以一个具体的流程（事务过程）为单位，考虑它的实现 .
- 接口设计与非接口设计是针对复用技术而言的，与面向对象（过程）不是一个问题.更多的体现就是对系统整体的架构



### 8.2 使用注解开发

核心就是使用了反射

1. 注解在接口上实现

   ```java
   @Select("select * from user")
   List<User> getUsers();
   ```

2. 需要在核心配置文件中绑定接口

   ```xml
   <!--    绑定接口-->
   <mappers>
       <mapper class="com.lihe.dao.UserMapper"/>
   </mappers>
   ```

3. 测试

   ```java
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
   ```

本质：反射机制的实现

底层：动态代理！

![image-20201003225616444](MyBatis.assets/image-20201003225616444.png)



**MyBatis详细执行流程！**

==这里还要再理解，看源码==



### 8.3 CRUD

我们可以在工具类



## 9 Lombok

- Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.
- Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.

使用步骤：

1. 在IDEA中安装插件
2. 在项目中导入lombok的jar包
3. 

```java
@Getter and @Setter
@FieldNameConstants
@ToString
@EqualsAndHashCode
@AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
@Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
@Data
@Builder
@SuperBuilder
@Singular
@Delegate
@Value
@Accessors
@Wither
@With
@SneakyThrows
@val
@var
experimental @var
@UtilityClass
```



```java
@Data:无参构造，get，set，tostring，hashcode，equals
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
```



## 10 多对一

- 多个学生对应一个老师
- 对于学生这边而言，**关联**。。多个学生关联一个老师
- 对于老师而言，**集合**，一对多

SQL：

```mysql
use mybatis

CREATE TABLE `teacher` (
  `id` INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO teacher(`id`, `name`) VALUES (1, '秦老师'); 


CREATE TABLE `student` (
  `id` INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  `tid` INT(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fktid` (`tid`),
  CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('1', '小明', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('2', '小红', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('3', '小张', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('4', '小李', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('5', '小王', '1');
```

#### **测试环境搭建**

1. 导入lombok
2. 新建实体类 Teacher Student
3. 建立Mapper接口
4. 建立Mapper.xml文件
5. 在核心配置文件中绑定注册Mapoer
6. 测试查询是否成功

#### **按照查询嵌套处理**

```xml
<mapper namespace="com.lihe.dao.StudentMapper">
    <!--
    思路
    1. 查询多有的学生信息
    2. 根据查询出来的学生的tid寻找对应的老师
    -->
    <select id="getStudent" resultMap="StudentTeacher">
        select * from student;
    </select>

    <resultMap id="StudentTeacher" type="Student">
        <result property="id" column="id"/>
        <result property="name" column="name"/>
        <!-- f复杂的属性，我们需要单独处理
           对象：association
           集合：collection
           -->
        <association property="teacher" column="tid" javaType="Teacher" select="getTeacher"/>
    </resultMap>

    <select id="getTeacher" resultType="Teacher">
        select * from teacher where id = #{id};
    </select>
</mapper>
```

#### **按照结果嵌套处理**



回顾MySQL多对一查询方式：

- 子查询
- 连表查询



## 11 一对多处理

比如：一个老师拥有多个学生

对于老师而言就是一对多的关系

1. #### 环境搭建，和刚才一样

   **实体类**

   ```java
   @Data
   public class Teacher {
       private int id;
       private String name;
   
       // 一个老师拥有多个学生
       private List<Student> students;
   }
   ```

   ```java
   @Data
   public class Student {
       private int id;
       private String name;
   
       // 学生需要关联一个老师
       private int tid;
   }
   ```



#### 按照结果嵌套处理

```xml
<!-- 按照结果嵌套查询 -->
<select id="getTeacher" resultMap="TeacherStudent">
    SELECT s.id sid, s.name sname, t.name tname, t.id tid
    FROM student s, teacher t
    WHERE s.tid = t.id and t.id = #{tid};
</select>
<!-- f复杂的属性，我们需要单独处理
   对象：association
   集合：collection
   javaType=“” 指定属性的类型
   集合中的泛型信息，我们使用ofType
   -->
<resultMap id="TeacherStudent" type="Teacher">
    <result property="id" column="tid"/>
    <result property="name" column="tname"/>
    <collection property="students" ofType="Student">
        <result property="id" column="sid"/>
        <result property="name" column="sname"/>
        <result property="tid" column="tid"/>
    </collection>
</resultMap>
```

#### 按照查询嵌套处理

```xml
<select id="getTeacher2" resultMap="TeacherStudent2">
    select * from teacher where id = #{tid};
</select>

<resultMap id="TeacherStudent2" type="Teacher">
    <collection property="students" javaType="ArrayList" ofType="Student" select="getStudentByTeacherID" column="id"/>
</resultMap>

<select id="getStudentByTeacherID" resultType="Student">
    select * from student where tid = #{tid};
</select>
```



#### 小节

1. 关联 - association 【多对一】
2. 集合 - collection 【一对多】
3. javaType & ofType
   - javaType 用来指定实体类中的属性类型
   - ofType 用来指定映射到List或者集合中的pojo类型，泛型中的约束类型！

**注意点：**

- 保证SQL的可读性，尽量保证通俗易懂
- 注意一对多和多对一中，属性名和字段的问题
- 如果问题不好排查错误，可以使用日志，建议使用Log4j



慢SQL    1s 	1000s	不会写不要自己乱写，可以去网上查



**面试高频：**

- MySQL引擎
- InnoDB底层原理
- 索引
- 索引优化



## 动态SQL

==什么是动态SQL：动态SQL就是指根据不同的条件生成不同的SQL语句==

利用动态 SQL，可以彻底摆脱这种痛苦。你应该能理解根据不同条件拼接 SQL 语句有多痛苦

如果你之前用过 JSTL 或任何基于类 XML 语言的文本处理器，你对动态 SQL 元素可能会感觉似曾相识。在 MyBatis 之前的版本中，需要花时间了解大量的元素。借助功能强大的基于 OGNL 的表达式，MyBatis 3 替换了之前的大部分元素，大大精简了元素种类，现在要学习的元素种类比原来的一半还要少。

- if
- choose (when, otherwise)
- trim (where, set)
- foreach

#### 搭建环境

```mysql
CREATE TABLE `blog`(
`id` VARCHAR(50) NOT NULL COMMENT '博客id',
`title` VARCHAR(100) NOT NULL COMMENT '博客标题',
`author` VARCHAR(30) NOT NULL COMMENT '博客作者',
`create_time` DATETIME NOT NULL COMMENT '创建时间',
`views` INT(30) NOT NULL COMMENT '浏览量'
)ENGINE=INNODB DEFAULT CHARSET=utf8
```

创建个基础工程

1. 导包

2. 编写配置文件

3. 编写实体类

   ```java
   @Data
   public class Blog {
       private int id;
       private String title;
       private String author;
       private Date createTime;
       private int views;
   }
   ```

4. 编写实体类对应的接口和MApper.xml文件



#### IF

```xml
<select id="queryBlogIF" parameterType="map" resultType="blog">
    select * from mybatis.blog where 1 = 1
    <if test="title != null">
        and title = #{title}
    </if>
    <if test="author != null">
        and author = #{author}
    </if>
</select>
```

#### trim(where, set )



**所谓的动态SQL，还是SQL语句，只是我们可以在SQL层面去执行一些逻辑代码**

if

where， set ， choose， when



Foreach

SQL片段



## 13 缓存

### 13.1 简介

==没有什么是加一层解决不了的==

查询：连接数据库，耗资源！

一次查询的结果，暂存在一个可以直接取到的地方！--> 内存   这就是缓存

我们再次查询相同的数据的时候，直接走缓存，就不用走数据库了

1. 什么是缓存[Cache]？
   - 存在内存中的临时数据
   - 将用户经常查询的数据放在缓存【内存】中，用户去查询数据就不用从磁盘上（关系型数据库数据文件）查询，从缓存中查询，从而提高查询效率，**解决了高并发系统的性能问题**。
2. 为什么使用缓存？
   - 减少和数据库交互次数，减少系统开销，提高系统效率。
3. 什么样的数据能使用缓存？
   - 经常查询并且不经常改变的数据。

![image-20201004203250938](MyBatis.assets/image-20201004203250938.png)

### 13.2 MyBatis缓存

- MyBatis包含一个非常强大的查询缓存特性，他可以非常方便地定制和配置缓存。缓存可以极大地提高查询效率。
- MyBatis系统中默认定义了两级缓存：**一级缓存**和**二级缓存**
  - 默认情况下，只有一级缓存开启。（SqlSession级别的缓存，也成为了本地缓存，只在sqlSession开启和关闭之间有效）
  - 二级缓存需要手动开启和配置，他是基于namespace级别的缓存（级别更高）
  - 为了提高扩展性，MyBatis定义了缓存接口Cache，我们可以通过实现Cache接口来实现二级缓存



13.3 
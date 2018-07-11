<p align="center">
  <a href="http://www.cnblogs.com/selton/">
    <img src="https://files.cnblogs.com/files/selton/show.ico" alt="" width=72 height=72>
  </a>

  <h3 align="center">SSM搭建</h3>

  <p align="center">
    SSM（Spring+SpringMVC+MyBatis）框架集由Spring、SpringMVC、MyBatis三个开源框架整合而成，常作为数据源较简单的web项目的框架。.
   
    <br>
    <a href="">SpringIoc</a>
    ·
    <a href="">SpringMVC</a>
    ·
    <a href="">Mybatis</a>
  </p>
</p>

<br>

## Table of contents

- [环境](#环境)
- [搭建](#搭建)
- [使用](#使用)

##环境

- jdk8
- tomcat8
- maven
- IDEA
- win7


##搭建

1. [导入web工程依赖](#导入web工程依赖)
2. [导入spring工程依赖](#导入spring工程依赖)
3. [搭建基本包结构](#基本包结构)
4. [配置resources配置文件](#配置文件)

###导入web工程依赖

将基本的web工程的依赖导入

	<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>javax.servlet-api</artifactId>
	<version>3.1.0</version>
	<scope>provided</scope>
	</dependency>

	<dependency>
	<groupId>javax.servlet.jsp</groupId>
	<artifactId>javax.servlet.jsp-api</artifactId>
	<version>2.3.1</version>
	<scope>provided</scope>
	</dependency>


	<!--没有这个依赖会报错-->
	<!--java.lang.NoClassDefFoundError:org/springframework/dao/support/DaoSupport-->
	<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-tx</artifactId>
	<version>4.3.11.RELEASE</version>
	</dependency>





###导入spring工程依赖

将基本的spring工程所需要的依赖导入  
springmvc依赖  

	<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-webmvc</artifactId>
	<version>4.3.11.RELEASE</version>
	</dependency>


###基本包结构

先搭建基本包的基本结构  
一般来说是在src/main/java包下新建出一个能代表你和当前项目的包的名字,比如说可是是com.selton.hellossm,
然后在这个包下面,
新建controller包,
controller包用来直接对接前端,
新建dao包,
dao包用来从数据库获取数据,
新建service包,
主要的业务逻辑需要在这里体现,
service包会调用dao层,然后提供给controller使用,
新建entities,
用来存放数据库的实体,
新建util包,
用来存放工具类,
新建constant包
用来存放一般常量

###配置文件  

接下来就是配置resource里的配置文件

首先是数据源连接池的配置
1.c3p0数据源连接池配置

mysql5
导入mysql5依赖  

	<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>5.1.18</version>
	</dependency>  

	<!--没有这个依赖会报错-->
	<!--PropertyAccessException1:org.springframework.beans.MethodInvocationException:-->
	<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-jdbc</artifactId>
	<version>4.3.11.RELEASE</version>
	</dependency>

(后面不提,都是在resources下)新建文件

导入依赖
	<dependency>
	<groupId>com.mchange</groupId>
	<artifactId>c3p0</artifactId>
	<version>0.9.5.2</version>
	</dependency>
  
`c3p0-config.properties`  

	c3p0.driverClassName=com.mysql.jdbc.Driver
	c3p0.url=jdbc:mysql://localhost:3306/databasename?useUnicode=true&characterEncoding=UTF-8
	c3p0.username=root
	c3p0.password=123456
	c3p0.maxActive=50
	c3p0.maxIdle=10
	c3p0.minIdle=5
	c3p0.initialSize=10
	c3p0.maxWait=5000
	c3p0.minPoolSize=10

接着将数据源连接池注入给mybatis
导入依赖

	<dependency>
	    <groupId>org.mybatis</groupId>
	    <artifactId>mybatis</artifactId>
	    <version>3.2.8</version>
	</dependency>
	
	<dependency>
	<groupId>org.mybatis</groupId>
	<artifactId>mybatis-spring</artifactId>
	<version>1.2.2</version>
	</dependency>

新建`spring-mybatis.xml`
	
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	       xmlns:context="http://www.springframework.org/schema/context"
	       xmlns:task="http://www.springframework.org/schema/task"
	       xsi:schemaLocation="http://www.springframework.org/schema/beans
	        http://www.springframework.org/schema/beans/spring-beans.xsd
	        http://www.springframework.org/schema/context
	        http://www.springframework.org/schema/context/spring-context-4.3.xsd http://www.springframework.org/schema/task http://www.springframework.org/schema/task/spring-task.xsd">
	
	
	     <!--构建一个C3P0数据源连接池对象-->
	     <bean id="id_ds_c3p0" class="com.mchange.v2.c3p0.ComboPooledDataSource">
	          <property name="driverClass" value="${c3p0.driverClassName}"></property>
	          <property name="jdbcUrl" value="${c3p0.url}"></property>
	          <property name="user" value="${c3p0.username}"></property>
	          <property name="password" value="${c3p0.password}"></property>
	          <property name="maxPoolSize" value="${c3p0.maxActive}"></property>
	          <property name="initialPoolSize" value="${c3p0.initialSize}"></property>
	          <property name="minPoolSize" value="${c3p0.minPoolSize}"></property>
	     </bean>
	
	     <!--配置SqlSessionFactory-->
	     <bean name="sqlSessionFactory" id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
	          <property name="dataSource" ref="id_ds_c3p0"></property>
	          <!--<property name="configLocation" value="classpath:mybatis-config.xml"></property>-->
	     </bean>
	     <!--有了这个配置，我们就指明了我们的Mapper们，即Dao们，都在哪个包
	          也能使用注解了
	           同时不用写Dao的实现类了
	     -->
	     <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
	          <!--basePackage的值如果没有对呀好包,会报错-->
	          <!--PropertyAccessException 1: org.springframework.beans.MethodInvocationException:-->
	          <property name="basePackage" value="com.selton.hellossm.dao"></property>
	          <property name="sqlSessionFactoryBeanName"  value="sqlSessionFactory"></property>
	     </bean>
	
	
	</beans>

新建`springmvc-config.xml`

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	       xmlns:context="http://www.springframework.org/schema/context"
	       xmlns:mvc="http://www.springframework.org/schema/mvc"
	       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd">
	
	    <!--springmvc 只管扫描 controller包-->
	    <context:component-scan base-package="com.selton.hellossm.controller"></context:component-scan>
	
	    <!-- 让spring-mvc支持注解  -->
	    <mvc:annotation-driven>
	        
	    </mvc:annotation-driven>
	
	 </beans>
	

##使用

这时完成了后台的配置,让我们实现一个简单地登录系统

用基本maven项目搭建出来的工程骨架里没有webapp

我们需要在src/main下新建文件夹webapp
当然不需要手动创建
idea有自动化的功能 ---链接  

在webapp下新建`loginfailed.html`

	<!DOCTYPE html>
	<html lang="en">
	<head>
	    <meta charset="UTF-8">
	    <title>Title</title>
	</head>
	<body>
	login failed
	</body>
	</html>  

新建`loginsuccess.html`

	<!DOCTYPE html>
	<html lang="en">
	<head>
	    <meta charset="UTF-8">
	    <title>Title</title>
	</head>
	<body>
	login success
	</body>
	</html>  

新建`index.html`

	<!DOCTYPE html>
	<html lang="en">
	<head>
	    <meta charset="UTF-8">
	    <title>Title</title>
	</head>
	<body>
	
	<form action="" method="get">
	    name: <input type="text" name="name">
	    <br>
	    password: <input type="password" name="password">
	    <br>
	    <input type="submit" value="登录">
	</form>
	</body>
	</html>  

在web.xml中写入

	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
	         version="3.1">
	    
	
	    <!-- 这个全局参数的作用是：加载Spring-config的配置文件-->
	    <context-param>
	        <param-name>contextConfigLocation</param-name>
	        <param-value>classpath:spring-config.xml</param-value>
	    </context-param>
	
	    <!--Spring的一个核心监听器，对Spring容器进行初始化动作
	      即我们以前的ApplicationContext context=new ClassPathXml.....
	     -->
	    <listener>
	        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	    </listener>
	
	
	    <!--SpringMVC的核心Servlet-->
	    <servlet>
	        <servlet-name>spring-mvc</servlet-name>
	        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	        <init-param>
	            <param-name>contextConfigLocation</param-name>
	            <param-value>classpath:springmvc-config.xml</param-value>
	        </init-param>
	        <load-on-startup>1</load-on-startup>
	    </servlet>
	    <servlet-mapping>
	        <servlet-name>spring-mvc</servlet-name>
	        <url-pattern>*.php</url-pattern>
	    </servlet-mapping>
	 
	
	    <!--  处理乱码-->
	    <filter>
	        <filter-name>encoding</filter-name>
	        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
	        <init-param>
	            <param-name>encoding</param-name>
	            <param-value>UTF-8</param-value>
	        </init-param>
	    </filter>
	    <filter-mapping>
	        <filter-name>encoding</filter-name>
	        <url-pattern>/*</url-pattern>
	    </filter-mapping>
	</web-app>

完成所有的配置工作后
开始写后台代码

在数据库中新建  

	/*
	 Navicat Premium Data Transfer
	
	 Source Server         : link1
	 Source Server Type    : MySQL
	 Source Server Version : 50622
	 Source Host           : localhost:3306
	 Source Schema         : db_test1
	
	 Target Server Type    : MySQL
	 Target Server Version : 50622
	 File Encoding         : 65001
	
	 Date: 10/07/2018 11:22:41
	*/
	
	SET NAMES utf8mb4;
	SET FOREIGN_KEY_CHECKS = 0;
	
	-- ----------------------------
	-- Table structure for user
	-- ----------------------------
	DROP TABLE IF EXISTS `user`;
	CREATE TABLE `user`  (
	  `id` int(11) NOT NULL,
	  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
	  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
	  `age` int(11) NULL DEFAULT NULL,
	  PRIMARY KEY (`id`) USING BTREE
	) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
	
	SET FOREIGN_KEY_CHECKS = 1;  

加入lombok依赖---[lombok资料](https://www.cnblogs.com/selton/p/9228528.html)  
不使用lombok的话,去掉@Data
然后自己添上无参构造,等各种get,set
在entities中新建实体类`User`

	@Data
	public class User {
	
	    private int id;
	    private String name;
	    private String password;
	    private int age;
	}  

在dao中新建UserDao  

	@Repository
	public interface UserDao {
	    
	    @Select("SELECT password FROM user WHERE name = #{name}")
	    String getUserByNameAndPassword(@Param("name") String name, @Param("password") String password);
	}  

service包中用来存放接口
在service下新建包serviceimpl,该包下存放service包中接口的实现类
service中新建接口类`UserService`

	public interface UserService {
	
	    boolean loginUserStatus(String name,String password);
	}  

在serviceimpl包下新建UserServiceImpl类实现`UserService`接口

	@Service
	public class UserServiceImpl implements UserService {
	
	    @Autowired
	    private UserDao userDao;
	
	    public boolean loginUserStatus(String name, String password) {
	
	        if(name == null || "".equals(name)){
	            return false;
	        }
	
	        if(password == null || "".equals(password)){
	            return false;
	        }
	        String passwordByName = userDao.getPasswordByName(name);
	        System.out.println("passwordByName = " + passwordByName);
	        if (password == null){
	            return false;
	        }
	
	        if (password.equals(passwordByName)) {
	
	            return true;
	        }
	        return false;
	    }
	}  

controller下新建类`UserController`

	@Controller
	@RequestMapping("user")
	public class UserController {
	
	    @Autowired
	    private UserService userService;
	
	    @PostMapping("userLogin")
	    public String userLogin(String name,String password){
	
	        if (userService.loginUserStatus(name,password)) {
	
	            return "/loginsuccess.html";
	        }
	
	        return "/loginfailed.html";
	    }
	}  

在index.html的form表单的action中写入
/userLogin/userLogin.php
写入form表单中的数据将会提交到这个controller中

运行,tomcat自行配置,记得将war配置到tomcat中,以及pom中packing war

<p align="center">
  <a href="http://www.cnblogs.com/selton/">
    <img src="https://files.cnblogs.com/files/selton/show.ico" alt="" width=72 height=72>
  </a>

  <h3 align="center">Hibernate</h3>

  <p align="center">
    随心所欲的使用面向对象思想操纵数据库.
  </p>
</p>

<br>

## Table of contents

- [介绍](#Hibernate)
- [搭建开发环境](#搭建环境)
- [半sql半面向对象写法](#半sql半面向对象写法)
- [完全的sql写法](#完全的sql写法)
- [完全的面向对象写法](#完全的面向对象写法)

## Hibernate

Hibernate是一个开放源代码的对象关系映射框架，它对JDBC进行了非常轻量级的对象封装，它将POJO与数据库表建立映射关系，hibernate可以自动生成SQL语句，自动执行，使得Java程序员可以随心所欲的使用对象编程思维来操纵数据库,从而无需顾及数据库的实现究竟是SQLServer还是Mysql还是Oracle

## 搭建环境  

**搭建在一般工程中**  
1. [jar下载](#jar下载)  
2. [配置文件](#配置文件)  


####jar下载
官网[Hibernate](http://hibernate.org/)进入后看到hibenate ORM,点击more,左侧边栏选择releases中的一个版本,页面最下方,选择download下载即可
官方jar包:lib文件:requeired文件里的所有jar拷贝到自己的新建工程中去,然后在加上连接数据库相关的包,mysql-connector

####配置文件 
 `Hibernate.cfg.xml`放置在src下,需要修改url,username和password   

		<?xmlversion='1.0'encoding='utf-8'?>
		<!DOCTYPEhibernate-configurationPUBLIC
		"-//Hibernate/HibernateConfigurationDTD//EN"
		"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
		
		<hibernate-configuration>
		<session-factory>
		<propertyname="connection.url">jdbc:mysql://localhost:3306/mycms</property>
		<propertyname="connection.driver_class">com.mysql.jdbc.Driver</property>
		<propertyname="connection.username">root</property>
		<propertyname="connection.password">123456</property>
		
		<mappingresource="com/selton/Node.hbm.xml"></mapping>
		</session-factory>
		</hibernate-configuration>
 
	<mappingresource="com/selton/Node.hbm.xml"></mapping>


 
`Node.hbm.xml` 映射到具体的pojo,一个pojo配置一个映射的xml  
名字和数据库的名字即使一样,也需要写上property的映射   

		<?xml version='1.0' encoding='utf-8'?>
		<!DOCTYPE hibernate-mapping PUBLIC
		    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
		    "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
		
		<hibernate-mapping package="com.selton">
		<class name="com.selton.Node" table="tree">
		    <id name="id" column="id">
		        <generator class="native"></generator>
		        <!--可以实现自动增长,也就是将实体类存储到数据库的时候,少set一个主键
		           比较疑惑,反正有没有这句话,数据库那儿都需要自动增长-->
		    </id>
		
		    <property name="nodeId" column="nodeId"></property>
		    <property name="pid" column="pid"></property>
		    <property name="type" column="type"></property>
		    <property name="url" column="url"></property>
		    <property name="icon" column="icon"></property>
		    <property name="description" column="description"></property>
		    <property name="level" column="level"></property>
		    <property name="name" column="name"></property>
		</class>
		
		</hibernate-mapping>


pojo的主键属性名称对应到数据库实体的主键名称写在id中
,其他的写在property中

## 半sql半面向对象写法

- [单个数据(对象)存储到数据库](#单个数据存储到数据库)
- [查询单个对象](#查询单个对象)
- [更新单个对象](#更新单个对象)
- [删除对象](#删除对象)
- [查询整个表](#查询整个表)
- [查询某个对象的某个属性](#查询某个对象的某个属性)
- [查询指定行数据](#查询指定行数据)
- [查询指定数据传回一个实体](#查询指定数据传回一个实体)
- [分组聚合](#分组聚合)
- [排序](#排序)
- [limit](#limit)
- [使用参数](#使用参数)
- [in](#in)
- [写在配置文件里](#写在配置文件里)

在一个入口方法或者测试类方法中,加入  

	//构建上下文换肩加配置连接池,开启事务
    Configuration configuration = new Configuration().configure();
    SessionFactory sessionFactory = configuration.buildSessionFactory();
    Session session = sessionFactory.openSession();
    Transaction transaction = session.beginTransaction();
	//这里填写测试代码
	//提交事务并关闭各种流
    transaction.commit();
    session.close();
    sessionFactory.close();
	
	
以下的测试代码,分别放在上面代码的``这里填写测试代码`处  

#### 单个数据存储到数据库  
 
		Node node = new Node();;
		node.setNodeId("testNodeId1");
		node.setPid("testPid1");
		node.setType((byte)1);
		node.setLevel((short)1);
		node.setName("testName1");
		session.save(node);  

####查询单个对象

	Node node=(Node)session.get(Node.class,1);
	System.out.println(node);
	//Node.class后面的1是数据库中的主键值  

####更新单个对象  
	
	Node node=(Node)session.get(Node.class,10);
	node.setName("updateName");
	session.update(node);  

####删除对象  

	Node node = (Node) session.get(Node.class, 10);
	session.delete(node);  

####查询整个表 

这里需要注意,如果你的pojo叫 myuser,而数据库中对应的表叫user,所有使用createQuery的地方,涉及到了表,就该填myuser  

	Query query = session.createQuery("FROM Node");
	List list = query.list();
	System.out.println("list = " + list);  


####查询某个对象的某个属性  

	Query query = session.createQuery("SELECT nodeId FROM Node");
	List list = query.list();
	System.out.println("list = " + list);  

####查询指定行数据  

	Query query = session.createQuery("FROM Node WHERE type=?");
	query.setParameter(0,10);
	List list = query.list();
	System.out.println("list = " + list);  


####查询指定数据传回一个实体  
	
	//需要pojo有相应的构造器
	Query query = session.createQuery("SELECT new Node(id,name,nodeId) FROM Node");
	List<Node> list = query.list();
	System.out.println("list = " + list);  

####分组聚合  
	
	Query query = session.createQuery("SELECT type,SUM(id) FROM Node GROUP BY type");
	List list = query.list();
	for (Object o : list) {
	    Object[] result = (Object[]) o;
	    System.out.println(Arrays.toString(result));
	}  

####排序  

	Query query = session.createQuery("FROM Node ORDER BY id DESC");
	List list = query.list();
	System.out.println("list = " + list);  

####limit  

	Query query = session.createQuery("FROM Node ORDER BY id DESC");
	query.setFirstResult(2);
	query.setMaxResults(3);
	List list = query.list();
	System.out.println("list = " + list);  

####使用参数  

	String colName = "id";
	String sql = "FROM Node WHERE " + colName + "=?";
	Query query = session.createQuery(sql);
	query.setParameter(0,6);
	
	Node node = (Node) query.uniqueResult();
	System.out.println("node = " + node);  
或者这种  

	Query query = session.createQuery("FROM Node WHERE id:id");
	query.setParameter("id",7);
	List list = query.list();
	System.out.println("list = " + list);  

####in  

	Query query = session.createQuery("FROM Node WHERE id IN(:ids)");
	query.setParameterList("ids",new Object[]{4,6,7});
	List list = query.list();
	System.out.println("list = " + list);
	//In 的效率很低

####写在配置文件里  
`User.hbm.xml`   
 
	<hibernate-mapping>
	...	
	<query name="getUserByAge">
	    FROM Node WHERE id between ? AND ?
	</query>
	...
	</hibernate-mapping>  

代码部分

	Query query = session.getNamedQuery("getUserByAge");
	query.setParameter(0,6);
	query.setParameter(1,8);
	List list = query.list();
	System.out.println("list = " + list);  


## 完全的sql写法

仿照第一种半sql写法,格式变化就可以  

1.原生sql写法  

	SQLQuery query = session.createSQLQuery("SELECT * FROM tree");
	query.addEntity(Node.class);
	List list = query.list();
	System.out.println("list = " + list);  

## 完全的面向对象写法  

仿照第一种半sql写法,格式变化就可以 

	Criteria criteria = session.createCriteria(Node.class);
	criteria.add(Restrictions.eq("id",6));
	List list = criteria.list();
	System.out.println("list = " + list);  

相当于查出来了所有的放在criteria里面

- [不等于](#不等于)
- [排序](#排序)
- [添加分页](#添加分页)
- [分组聚合](#分组聚合)


####不等于

	Criteria criteria = session.createCriteria(Node.class);
	criteria.add(Restrictions.ne("id",1));
	List list = criteria.list();
	System.out.println("list = " + list);  

####排序  

	Criteria criteria = session.createCriteria(Node.class);
	criteria.addOrder(Order.desc("id"));
	List list = criteria.list();
	System.out.println("list = " + list);  

####添加分页  

	Criteria criteria = session.createCriteria(Node.class);
	criteria.add(Restrictions.ne("id",1));
	criteria.setFirstResult(0);
	criteria.setMaxResults(2);
	List list = criteria.list();
	System.out.println("list = " + list);  

####分组聚合  

	Criteria criteria = session.createCriteria(Node.class);
	ProjectionList projectionList = Projections.projectionList();
	projectionList.add(Projections.sum("id"));
	projectionList.add(Projections.groupProperty("type"));
	criteria.setProjection(projectionList);
	List list = criteria.list();
	for (Object o : list) {
	    Object[] result = (Object[]) o;
	System.out.println(Arrays.toString(result));
	}
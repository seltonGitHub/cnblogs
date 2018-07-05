<p align="center">
  <a href="http://www.cnblogs.com/selton/">
    <img src="https://files.cnblogs.com/files/selton/show.ico" alt="" width=72 height=72>
  </a>

  <h3 align="center">Memcached</h3>

  <p align="center">
    Memcached 是一个高性能的分布式内存对象缓存系统，用于动态Web应用以减轻数据库负载
  </p>
</p>

<br>

## Table of contents

- [安装](#安装)
- [使用](#使用)
- [在spring中使用](#spring)

##安装
1. 下载下来memcached.exe
2. 切换到memcached.exe所在路径
3. 输入memcached -d install
4. win + r  输入 services.msc打开window服务
5. 随便选中一个输入memcached就可以查看到安装好的服务,右击启动它,然后关闭窗口


##使用  

1. 新建java工程或者maven工程  
2. 导入三个必备的依赖,`fastjson-1.2.3`,`slf4j-api-1.7.5`,`xmemcached-2.3.2`
3. main方法中加入  

		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("127.0.0.1:11211"));
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		try {
		    MemcachedClient memcachedClient =builder.build();
		
		//在这里写入测试代码
		
		    memcachedClient.shutdown();
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		} catch (MemcachedException e) {
		    e.printStackTrace();
		} catch (TimeoutException e) {
		    e.printStackTrace();
		}

- [测试一定时间后数据是否还能取出来](#时效)
- [永久时间,测试重启服务之后先前存储的数据是否还能取出来](#恢复)
- [delete](#delete)
- [自动增长](#自动增长)  

####时效
  
		memcachedClient.set("testTime",2,"testTimeValue");
		String testTime = memcachedClient.get("testTime");
		System.out.println("testTime = " + testTime);
		TimeUnit.SECONDS.sleep(4);
		System.out.println("4s 过去了");
		String valueExist  = memcachedClient.get(testTime);
		System.out.println("valueExist = " + valueExist);  
>输出:  
>testTime = testTimeValue  
4s 过去了  
valueExist = null    


####恢复  

	//存储,然后关闭掉服务
	memcachedClient.set("test",0,"testValue");
	String testTime=memcachedClient.get("test");
	System.out.println("testTime="+testTime);
	System.out.println("存储成功");  

>输出:
>testTime = testValue  
存储成功
	
	//关闭掉服务后的重启
	String testTime = memcachedClient.get("test");
	System.out.println("testTime = " + testTime);
	System.out.println("取值失败");  
>输出:  
>testTime = null  
取值失败  

####delete  
	memcachedClient.set("test",0,"testValue");
	String getVal = memcachedClient.get("test");
	System.out.println("getVal = " + getVal);
	memcachedClient.delete("test");
	System.out.println("after delete ...");
	getVal = memcachedClient.get("test");
	System.out.println("getVal = " + getVal);  

>输出:  
>getVal = testValue  
after delete ...  
getVal = null  

####自动增长  

	//三个参数,第一个指定键,第二个指定递增的幅度大小,第三个指定当key不存在的情况下的初始值
	for (int i = 0; i < 5; i++) {
	    memcachedClient.incr("博客的赞",1,20);
	    String point = memcachedClient.get("博客的赞");
	    System.out.println("point = " + point);
	}  
  
>输出:  
>  point = 20  
point = 21  
point = 22  
point = 23  
point = 24  

关于incr的用法,值得警惕的是,它的值虽然看起来是一个数字,实际上正如代码中的String point = memcachedClient.get("博客的赞");
其实是一个字符串,所以会出现如下错误  

	memcachedClient.set("博客的赞1",0,10);
	int str = memcachedClient.get("博客的赞1");
	System.out.println("str1 = " + str);
	memcachedClient.incr("博客的赞1",2,22);
	str = memcachedClient.get("博客的赞1");
	System.out.println("str2 = " + str);  

>输出:  
>net.rubyeye.xmemcached.exception.MemcachedClientException: cannot increment or decrement non-numeric value,key=博客的赞1
	at net.rubyeye.xmemcached.command.Command.decodeError(Command.java:267)
..
at com.google.code.yanf4j.nio.impl.NioController.onRead(NioController.java:157)
at com.google.code.yanf4j.nio.impl.Reactor.dispatchEvent(Reactor.java:323)
at com.google.code.yanf4j.nio.impl.Reactor.run(Reactor.java:180)  
str1 = 10  

输出的顺序不同,注意输出的异常栈信息的第一条和后面的几条就指明nio.impl.Reactor.run,线程的,这儿就不深入展开了  


##spring

- 新建一个maven工程
- [pom.xml](#pom.xml)
- 在resource中新建[sping-config.xml](#config)  
- [spring单元测试代码骨架](#骨架)
- [测试代码](#测试代码)


####骨架  

	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = "classpath:spring-config.xml")
	public class TestMem {
	    @Autowired
	    private MemcachedClient memcachedClient;
		
		@Test
		public void test1(){
		//测试代码部分
		}
	
	}  

####测试代码  

	memcachedClient.set("springData",3,"dataVal");
	String str = memcachedClient.get("springData");
	System.out.println("str = " + str);  

>输出:  
>str = dataVal  

还可以存储对象,不过该对象必须实现Serializable接口,不然会报错`java.io.NotSerializableException: Teacher`,  

实现接口后  

	import lombok.Data;
	import java.io.Serializable;
	@Data
	public class Teacher implements Serializable {
	    private int age;
	    private String name;
	}  

关于@Data关我在另一篇博客中有介绍[lombok](https://www.cnblogs.com/selton/p/9228528.html)  

	Teacher teacher = new Teacher();
	teacher.setAge(3);
	teacher.setName("23");
	
	memcachedClient.set("te", 0, teacher);
	Teacher teacher1 = memcachedClient.get("te");
	System.out.println("teacher1 = " + teacher1);

>输出:  
>teacher1 = Teacher(age=3, name=23)  

####pom.xml  

	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0"
	         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	    <modelVersion>4.0.0</modelVersion>
	
	    <groupId>com.selton</groupId>
	    <artifactId>DemoMemSpring</artifactId>
	    <version>1.0</version>
	
	    <dependencies>
	
	        <dependency>
	            <groupId>org.projectlombok</groupId>
	            <artifactId>lombok</artifactId>
	            <version>1.18.0</version>
	            <scope>provided</scope>
	        </dependency>
	
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-context</artifactId>
	            <version>4.3.11.RELEASE</version>
	        </dependency>
	
	        <dependency>
	            <groupId>com.googlecode.xmemcached</groupId>
	            <artifactId>xmemcached</artifactId>
	            <version>2.0.0</version>
	            <exclusions>
	                <exclusion>
	                    <groupId>org.springframework</groupId>
	                    <artifactId>spring-context</artifactId>
	                </exclusion>
	            </exclusions>
	        </dependency>
	
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-test</artifactId>
	            <version>4.3.11.RELEASE</version>
	        </dependency>
	
	        <dependency>
	            <groupId>junit</groupId>
	            <artifactId>junit</artifactId>
	            <version>4.12</version>
	            <scope>test</scope>
	        </dependency>
	
	    </dependencies>
	
	</project>  


####config  

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	       xmlns:p="http://www.springframework.org/schema/p"
	       xmlns:context="http://www.springframework.org/schema/context"
	       xsi:schemaLocation="http://www.springframework.org/schema/beans
	          http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
	          http://www.springframework.org/schema/context
	          http://www.springframework.org/schema/context/spring-context-3.0.xsd
	          ">
	
	
	    <bean id="memcachedClient" name="memcachedClient"
	          class="net.rubyeye.xmemcached.utils.XMemcachedClientFactoryBean">
	        <property name="servers">
	            <!--配置端口,另加入的话,空格隔开-->
	            <value>127.0.0.1:11211</value>
	        </property>
	        <property name="weights">
	            <list>
	                <!--设置不同端口的权重,这里只有一个端口-->
	                <value>1</value>
	            </list>
	        </property>
	        <property name="sessionLocator">
	            <bean class="net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator"></bean>
	        </property>
	        <property name="transcoder">
	            <bean class="net.rubyeye.xmemcached.transcoders.SerializingTranscoder" />
	        </property>
	        <property name="bufferAllocator">
	            <bean class="net.rubyeye.xmemcached.buffer.SimpleBufferAllocator"></bean>
	        </property>
	    </bean>
	
	</beans>
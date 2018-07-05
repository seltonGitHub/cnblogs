<p align="center">
  <a href="http://www.cnblogs.com/selton/">
    <img src="https://files.cnblogs.com/files/selton/show.ico" alt="" width=72 height=72>
  </a>

  <h3 align="center">Lombok</h3>

  <p align="center">
    Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.
  </p>
</p>

<br>

## Table of contents

- [安装](#Install)
- [使用](#使用)
- [数据字典](#数据字典)

##Install

在idea上使用  

Install [Lombok](jarhttps://www.projectlombok.org/download)  
>注意:如果编译出现错误`Error:java: Lombok annotation handler class lombok.javac.handlers.HandleData failed on...`,检查你的版本,确保不是官网教程示例的版本0.9.2,替换成1.18.0,或者是RELEASE 
 
2. 新建一个person类设置一些属性,加入lombok的jar后,我们在person类上加入[@Data](#Data)注解,现在只需要了解它可以为类属性生成get,set方法,发现有提示而且并没有报错,也就是至少jar成功被依赖了,但是在单元测试中,发现new出person对象无法调用get,set等方法,因为lombok虽然会在编译时将你的注解转化为实际对应的代码执行,但是在写代码的时候,idea并不知道[@Data](#Data)可以带来什么,所以需要加入插件

3. 需要安装idea的lombok插件(需要梯子)idea -> file ->setting -> plugin ->browser repos 输入lombok 选择lombok 然后 install
安装完成后restart idea


4. idea 重启后需要配置注解处理器
同样我们在Settings设置页面，我们点击Build，Execution，Deployment-->选择Compiler-->选中Annotation Processors，然后在右侧勾选Enable annotation processing即可,完成后,需要再次重启idea,不要和上一步的重启合并成一步

5. 这个时候发现[@Data](#Data)注解在菜单栏点击View-->Tool Windows-->Structure，便可以看到类中所有的方法了这些都是lombok帮我自动生成的。
person类的方法实现了
![](https://images2018.cnblogs.com/blog/1394862/201806/1394862-20180626122906445-151083626.gif)

也可以调用了
![](https://images2018.cnblogs.com/blog/1394862/201806/1394862-20180626122933095-321528284.gif)


##使用
- [@Data的使用](#@Data使用)  
  
####@Data的使用
新建people类
为了测试data官方文档中说明的
get,toString,equal对于静态属性与非静态属性的区分
set,constructor对于final非final的区分  
![](https://images2018.cnblogs.com/blog/1394862/201806/1394862-20180626164521254-1498831536.gif)



##数据字典

- [Data](#Data)


####Data
官方文档:
Generates getters for all fields, a useful toString method, and hashCode and equals implementations that check all non-transient fields. Will also generate setters for all non-final fields, as well as a constructor.  
翻译过来就是加入`@Data`会为类的所有非静态属性生成get方法,以及对应非静态属性的toString()方法,也会为类的所有没有final修饰的属性生成set方法和对应的构造器
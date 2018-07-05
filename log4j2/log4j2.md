<p align="center">
  <a href="http://www.cnblogs.com/selton/">
    <img src="https://files.cnblogs.com/files/selton/show.ico" alt="" width=72 height=72>
  </a>

  <h3 align="center">Log4j2</h3>

  <p align="center">
    Apache Log4j 2 is an upgrade to Log4j that provides significant improvements over its predecessor 
  </p>
</p>

<br>



## Table of contents

- [环境搭建](#环境搭建)
- [配置详解](#配置详解)



##环境搭建

一般java工程:

- [Download](#下载)

- [配置到环境](#配置到环境)

- [配置文件路径](#配置文件路径)

  

###下载

apache官网给的链接是清华镜像网站的,国外的很多开源的在国内太慢的话不妨尝试一下国内的最大镜像站[清华镜像站](https://mirrors.tuna.tsinghua.edu.cn/),下载地址[log4j-2.11.0](https://mirrors.tuna.tsinghua.edu.cn/apache/logging/log4j/2.11.0/apache-log4j-2.11.0-bin.zip),  



### 配置到环境  

To use Log4j 2 in your application make sure that both the API and Core jars are in the application’s classpath. Add the dependencies listed below to your classpath. -----这是官网的原话,翻译过来就是,使用log4j2的方式是,将接口jar和实现jar放到所需项目的classpath中,并添加依赖,具体就是log4j-api-2.11.0.jar和log4j-core-2.11.0.jar

### 配置文件路径  

log4j2.xml可以放在任意的地方，只要你最后把它放到了classpath里，上面的项目中新建一个resources目录用于放置log4j2.xml，如果在未加入classpath时尝试运行时会报如下错误：  

```
ERROR StatusLogger No Log4j 2 configuration file found. Using default configuration (logging only errors to the console), or user programmatically provided configurations. Set system property 'log4j2.debug' to show Log4j 2 internal initialization logging. See <https://logging.apache.org/log4j/2.x/manual/configuration.html> for instructions on how to configure Log4j 2   

```

大义就是运行的项目没有在classpath 中找到你的配置文件

以Eclipse环境为例，可以在Run—Run Configurations对应项目的Classpath中选择User Entries，点击Advanced，选择Add Folder，把resources文件夹添加进来。 



idea环境

熟悉idea的话点击右上方放大镜图标左侧的图标  
![](https://images2018.cnblogs.com/blog/1394862/201806/1394862-20180616215217477-1697213702.png)



再点击左侧边栏Project Settings下的Modules    
![](https://images2018.cnblogs.com/blog/1394862/201806/1394862-20180616215332359-324087624.png)  


扩展一下,点了之后,右侧边栏区域  
![](https://images2018.cnblogs.com/blog/1394862/201806/1394862-20180616215400976-1899710376.png)


表示的是已作为classpath的文件夹,这里可以从已设置为classpath的文件夹中取消选择, 

取消掉以后记得按下下方的apply保存一下   

选中右侧出现的工程目录中你用来存放配置文件log4j2.xml中的文件夹,点击一下选中,再点击resource,右侧边栏就会多出一下你选中的文件,点下方的apply即可   





## 配置详解  

```xml
<?xml version="1.0" encoding="UTF-8"?>


<!-- monitorInterval，配置为120，单位为秒。即在服务运行过程中发生了log4j2配置文件的修改，log4j2能够在monitorInterval时间范围重新加载配置，无需重启应用。-->
<Configuration status="WARN" monitorInterval="120">


    <properties>
        <!--当输出到文件中的时候使用LOG_HOME替代输出到的文件夹,类似于配置java环境的时候的JAVA_HOME的做法-->
        <!--最好是填写相对路径,基路径是当前项目也就是src的上一级别-->
        <!--如果写成testlog/mylog,即使没有testlog,也会在src同级目录下新建出testlog文件夹以及其下的mylog文件夹-->
        <property name="LOG_HOME">testlog/mylog</property>
    </properties>

    <!--翻译：附加器,记录方式-->
    <Appenders>

        <!--appenders里的两个属性,分别为name=Console和name=log(两个名字是自己起的)-->
        <!--appenders属性同级的loggers中的root的level的值控制输出信息的严格级别,一般是info-->
        <!--root中的AppenderRef的ref写appenders中的name,在这里也就是添Console或log-->

        <!--name是自己命名的,target=SYSTEM_OUT表示输出到控制台-->
        <Console name="Console" target="SYSTEM_OUT">
            <!--pattern控制格式化输出的格式-->
            <!--例子:在代码中写入logger.info("info级别信息");-->
            <!--输出:12:8:34.501 [main] INFO  com.selton.Log4jTest - info级别信息-->
            <PatternLayout pattern="%d{H:m:s.S} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>

        <!--临时日志生成-->
        <!--<File name="log" fileName="log/test.log" append="true">
            <PatternLayout pattern="%d{H:m:s.S} [%t] %-5level %logger{36} - %msg%n"/>
        </File>-->

        <!--fileName：日志存储路径,
        filePattern：历史日志封存路径。其中%d{yyyyMMddHH}表示了封存历史日志的时间单位（目前单位为小时，yyyy表示年，MM表示月，dd表示天，HH表示小时，mm表示分钟，ss表示秒，SS表示毫秒）。
        注意后缀，log4j2自动识别zip等后缀，表示历史日志需要压缩。-->
        <RollingRandomAccessFile name="File" immediateFlush="true" fileName="${LOG_HOME}/today.log"
                                 filePattern="${LOG_HOME}/history-%d{yyyy-MM-dd}.log">

            <!-- level，表示最低接受的日志级别，配置为INFO，即我们期望打印INFO级别以上的日志。-->
            <!--onMatch，表示当日志事件的日志级别与level一致时，应怎么做。一般为ACCEPT，表示接受。-->
            <!--onMismatch，表示日志事件的日志级别与level不一致时，应怎么做。一般为DENY，表示拒绝。也可以为NEUTRAL表示中立。-->
            <Filters>

                <!--最下方的Root level="debug",如果不设置这句的话,4个级别信息都会打印,设置后,就会只打印INFO以及之上-->
                <ThresholdFilter level="INFO" onMatch="ACCEPT" onMismatch="DENY"/>
            </Filters>

            <!--输出到文件夹中去-->
            <PatternLayout pattern="%d{y-M-d H:m:s.S} [%t] %-5level %logger{36} - %msg%n" />
            <!--<HTMLLayout pattern="%d{y-M-d H:m:s.S} [%t] %-5level %logger{36} - %msg%n" />-->

            <!--必配项,TriggeringPolicy(触发策略) -->
            <Policies>
                <!--按天,划分日志文件-->
                <TimeBasedTriggeringPolicy interval="1" modulate="true" />
            </Policies>

            <!--必配项,RolloverStrategy(覆盖策略)-->
            <!--<DefaultRolloverStrategy max="20"/>-->
        </RollingRandomAccessFile>

    </Appenders>

    <Loggers>
        <Root level="debug">
            <AppenderRef ref="File"/>
        </Root>
    </Loggers>

</Configuration>

```
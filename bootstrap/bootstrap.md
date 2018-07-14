<p align="center">
  <a href="https://getbootstrap.com/">
    <img src="https://files.cnblogs.com/files/selton/show.ico" alt="" width=72 height=72>
  </a>

  <h3 align="center">Bootstrap-table</h3>

  <p align="center">
    快速入门----
	bootstrap-table是一个基于Bootstrap的jQuery插件<br>可以实现从数据库中提取数据到前端进行相应操作的功能
    <br>
    <br>
    <a href="https://www.cnblogs.com/selton/p/9211415.html">基于bootstrap-table的后台系统功能展示</a>
   
  </p>
</p>

<br>



## Table of contents  

[TOC]  


- [Quick start](#quick-start)
- [Why use it](#why-use)
- [What's included](#whats-included)
- [details](#details)
- [进阶之行内编辑](#editable)
- [额外的坑点](#注意)


##quick-start

>对本文有什么疑问或者建议,可以在下方的评论区说明,笔者会尽能力给出解答,另外已经完成了bootstrap-table的增删改查操作,以及增加操作的的模态框(bootstrap modal),以及表单校检(bootstrap-validate),最近有点忙,过一段时间更新,还有侧边栏的动态生成(bootstrap-treeview)也是在学习当中  
  
快速使用:

- [Download bootstrap-table hello demo](https://github.com/seltonGitHub/helloBootTable)
- [预览前端demo代码](#demo)
- Clone the repo: `git clone https://github.com/seltonGitHub/helloBootTable.git`


没有配置java环境,移步[JDK安装与环境变量配置](https://jingyan.baidu.com/article/6dad5075d1dc40a123e36ea3.html);  
没有配置tomcat环境,移步[tomcat的下载和安装配置](https://jingyan.baidu.com/article/2c8c281daa77aa0008252aff.html);  
没有下载配置idea环境,移步[IntelliJ IDEA安装以及配置](https://jingyan.baidu.com/article/afd8f4debd60f434e286e91f.html);  


##why-use  
- 学习成本低,配置简单,文档齐全
- 与Bootstrap无缝衔接,整体风格一致,也便于二次开发
- 开发者活跃,Github定期维护
  
##whats-included

file list:

```
bootstrapDemo/
├── web/
│   ├── js
│   ├── WEB-INF
│   └── bootindex.html
└── src/
│   └── DataSendServlet.java


```

表单展示页面 (`bootindex.html`)  
javascript文件 (`showOrder.js`)从服务器取得数据,然后渲染表格  

##details  
 
	$("#table").bootstrapTable({
	  method: "post",
	  url: "获取后台数据的url",
	  ... ...
	
	});   

这里的js语句的所有渲染操作是针对html页面中的id为table的一个table,所以不要忘了在导入了该js的html中构建出id为table的table  

bootstrap-table中的重要键值的简单解释:  

- [url(必须修改)](#url)
- [method](#method)
- [pageSize(必须修改)](#pageSize)  
- [jsonstyle(必须修改)](#jsonstyle)
- [columns(必须修改)](#columns)
- [contentType(必须填写)](#contentType)
- [queryParams](#queryParams)
- [pageNumber](#pageNumber)
- [表格绑定事件](#表格绑定事件)

`showorder.js`会向服务器发起ajax访问  

`bootstrapTable`构建元素解析:  
####url  
	$("#table").bootstrapTable({
	  method: "post",
	  url: "获取后台数据的url",
	  ... ...
	
	}); 
ajax访问到的后台路径(必须),该后台需要按照指定的[json](#jsonstyle) 格式返回数据

####method

get发送的数据在请求报文的请求行,也就是url部分,而且参数如果有中文会出现乱码问题,而post发送的数据在报文实体,都应该是post,表单的提交也一般都是post
 
####queryParams  
不需要任何修改,相当于ajax中的data键,上面的method决定这些参数传递给后台的的传递方式.发送给后台的数据,给出实现表单分页的两个参数,[offset](#offset)和[limit](#limit),在`oTableInit.queryParams`中给出,后台用`request.getParameter()`的方式拿到queryParams中传递过来的值,然后制定dao  

####pageSize  
当前table一次最多显示多少行,也就是你的table的一页应该展现多少行,必须  
  
####pageNumber  
起始页,一般是1不用改,这个和pageSize决定了queryParams中的offset的值,`offset=(pageNow - 1)   * pageSize,limit=pageSize`
  

####contentType  
`contentType: "application/x-www-form-urlencoded"`
####columns  
	$("#table").bootstrapTable({
	  method: "post",
	  url: "获取后台数据的url",
      data: [
            {field: 'testId', title: 'ID'},
            {field: 'testName', title: '姓名'},
            {field: 'testPassword', title: '密码'}
            ]  
	      ... ...
	  ]
	});  

你的table的表结构,以上例子表示表有三列,列的实际显示名字分别是ID,姓名,密码,但是field代表实际数据的名字,表中的数据是由于ajax向服务器发起访问,服务器返回给的数据中的rows的每一个json对象的键都会对应到field的列中-----[服务器返还的值](#jsonstyle)
  
###jsonstyle 

    {  
    "total":25,  
	    "rows":[
            {
            "testID":1,
            "testName":"xiaoming1",
            "testPassword":"xiaomingpwd1"
            },
            {
            "testID":2,
            "testName":"xiaoming2",
            "testPassword":"xiaomingpwd2"
            }
        ]
    }    
数据库返还给发起访问的ajax的数据,必须满足,包含两个json形式的键值对,
一个是total键,值为表单拥有者在数据库中的全部数据的数量(行数),这个数据和pageSize决定table展示的页面有多少页,另一个是rows键,值为多个json对象,rows的每一个json对象就是当前table页的一行实体展示,这里的rows相当于会给前端table两行数据,testID,testName,testPassword分别会被填入到table中的field对应的列中-----[前端接收到值表现](#columns)
  


####offset  

    oTableInit.queryParams = function (params) {

        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,    //params.limit,   页面大小
            offset: params.offset,
            testNum: 445,
            testNum1: 343
        };  

offset=([pageNumber](#pageNumber) - 1) * [pageSize](#pageSize),是会被发送到后台使用的数据,[后台数据提取sql语句示例](#sql示例)  
  
####limit  

    oTableInit.queryParams = function (params) {

        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,    //params.limit,   页面大小
            offset: params.offset,
            testNum: 445,
            testNum1: 343
        };
limit=[pageSize](#pageSize),是会被发送到后台使用的数据,[后台数据提取sql语句示例](#sql示例)

####sql示例  
  
`SELECT * FROM test WHERE id = ? LIMIT offset,limit`  

####表格绑定事件  
用于测试ajax返回的数据是最好的  

	$("#table").bootstrapTable({
	      method: "post",
	      url: "获取后台数据的url",
	      onLoadSuccess: function(){  //加载成功时执行
	            console.info("加载成功");
	      },
	      onLoadError: function(){  //加载失败时执行
	            console.info("加载数据失败");
	      }
	});    

关于事件,更为详细的介绍请访问[boottableDoc](http://bootstrap-table.wenzhixin.net.cn/zh-cn/documentation/#%E4%BA%8B%E4%BB%B6)  

##editable  
**这是在操作table吗,感觉就像是数据库展现在了页面上**

如果你已经阅读完或者已经在自己代码中实现了上述功能,但是table存在的目的本来就不应该只是展现,应该还有寻常的CRUD,精力有限,只是实践了update,笔者使用的是行内编辑的方式实现的update,需要用到另一个工具X-editable,不过还好boottable有这样的插件,将x-editable封装整合到了当中,只需要引入`https://cdn.bootcss.com/bootstrap-table/1.12.1/extensions/editable/bootstrap-table-editable.min.js`  

- [快速使用](#start-editable)
- [方法详解](#details-editable)
- [关于editable的语法的细节](#moreEditable)
- [x-editableDemo在线展示](http://vitalets.github.io/x-editable/demo-bs3.html)

####start editable  

	$("#table").bootstrapTable({
	  method: "post",
	  url: "获取后台数据的url",
        [
        {field: 'testId',
        title: 'ID',
        editable: {mode: 'popup'}
        },
        {field: 'testName', 
        title: '姓名'},
        {field: 'testPassword', 
        title: '密码'}
        ]  
	      ... ...
	  ]
	});  

    onEditableSave: function (field, row, oldValue, $el) {

        $.ajax({
            type: "post",
            url: "/ordercenter/updateOrder.json",
            data: {
                orderid: row.orderid,
                updateCol: field,
                updateVal: eval('row.'+field)
            },
            dataType: 'JSON',
            success: function (data, status) {
                console.log(data);
                if (status == "success") {
                    alert('旧数据: 订单号: ' + row.orderid + ' ' + field + ': ' + oldValue + '\r\n'
                    + '更新后的数据: 订单号: ' + data.updateId + ' ' + data.updateCol + ': ' + data.updateVal)
                }
            },
            error: function () {
                alert('编辑失败');
            },
            complete: function () {

            }

        });
    },

####details editable  

编辑后的提交方法统一放到`onEditableSave`事件里面统一处理

例子: 页面table中的列姓名,field为testName,实际的值为xiaoming1,通过修改将其改为xiaoming2,这时候field为testName,row为一个json,键值对分别为该行的所有键值组合,oldValue为xiaoming1
更为详细的描述请到[x-editable](http://vitalets.github.io/x-editable/)  

建议读者直接使用我的`onEditableSave`,它向后台发送了三个数据精确完成update,行特定标识和列特定标识定位到修改了哪一个具体的数据,再给出updateVal指出原本的数据被修改成了updateVal 


####moreEditable

editable中的mode的值一般是popup,翻译是弹出的意思,也可以使用inline值,但是点击并且编辑的时候会使表格样式发生改变,而popup则不会
 


####demo

- [web-xml](#web-xml)
- [html代码](#html-demo)
- [js代码](#js-demo)
- [后台代码](#server-demo)

####web-xml

	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
	         version="4.0">
	
	
	    <welcome-file-list>
	        <welcome-file>/bootindex.html</welcome-file>
	    </welcome-file-list>
	    <servlet>
	        <servlet-name>DataSendServlet</servlet-name>
	        <servlet-class>com.selton.DataSendServlet</servlet-class>
	    </servlet>
	
	    <servlet-mapping>
	        <servlet-name>DataSendServlet</servlet-name>
	        <url-pattern>/DataSendServlet</url-pattern>
	    </servlet-mapping>
	</web-app>

####html-demo
	<!DOCTYPE html>
	<html lang="en">
	<head>
	    <meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.0.1/css/bootstrap.min.css">
	    <link href="https://cdn.bootcss.com/bootstrap-table/1.12.1/bootstrap-table.min.css" rel="stylesheet">
	    <script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
	    <link href="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/css/bootstrap-editable.css" rel="stylesheet"/>
	    <script src="//cdnjs.cloudflare.com/ajax/libs/x-editable/1.5.0/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
	    <script language="JavaScript" src="/js/showOrder.js"></script>
	    <script language="JavaScript">
	        $(function () {
	            //1.初始化Table
	            var oTable = new TableInit();
	            oTable.Init();
	        });
	    </script>
	
	</head>
	<body>
	<div class="container">
	
	    <table id="Table"></table>
	
	</div>
	<script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
	<script src="http://apps.bdimg.com/libs/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	<script src="https://cdn.bootcss.com/bootstrap-table/1.12.1/bootstrap-table.min.js"></script>
	
	</body>
	</html>




####js-demo

	var TableInit = function () {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
	    $('#Table').bootstrapTable({
	
	        url: '/DataSendServlet',         //请求后台的URL（*）
	
	        method: 'get',                      //请求方式（*）
			async:  true,                        //true表示执行到这,ajax向后台发起访问,在等待响应的这段时间里,继续执行下面的代码
                                                 //设置为true,基本都是后面的代码(除非还有ajax)先执行
			
	
	        // toolbar: '#toolbar',                //工具按钮用哪个容器
	
	        striped: true,                      //是否显示行间隔色
	
	        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	
	        pagination: true,                   //是否显示分页（*）
	
	        queryParams: oTableInit.queryParams,//传递参数（*）
	
	        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
	
	        paginationPreText:'<',              //上一页按钮样式
	
	        paginationNextText:'>',             //下一页按钮样式
	
	        pageNumber: 1,                       //初始化加载第一页，默认第一页
	
	        pageSize: 10,                       //每页的记录行数（*）
	
	        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
	
	        contentType: "application/x-www-form-urlencoded",   //重要选项,必填
	
	        showColumns: true,                  //是否显示所有的列
	
	        showRefresh: true,                  //是否显示刷新按钮
	
	        minimumCountColumns: 2,             //最少允许的列数
	
	        clickToSelect: true,                //是否启用点击选中行
	
	        //height: 700,                        //行高，如果没有设置height属性，表格自动根据记录条数决定表格高度,最好不要设置这个属性
	
	        uniqueId: "no",                     //每一行的唯一标识，一般为主键列
	
	        showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
	
	        cardView: false,                    //是否显示详细视图
	
	        detailView: false,                   //是否显示父子表
	
	        columns: [
	            {
	                field: 'testId',
	                title: 'ID',
	                editable: {
	                    mode: 'inline'
	                }
	            }, {
	
	                field: 'testName',
	                title: '用户名'
	            }, {
	
	                field: 'testPassword',
	                title: '密码'
	            }
	        ],
	        rowStyle: function (row, index) {
	
	            var classesArr = ['success', 'info'];
	
	            var strclass = "";
	
	            if (index % 2 === 0) {//偶数行
	
	                strclass = classesArr[0];
	
	            } else {//奇数行
	
	                strclass = classesArr[1];
	
	            }
	
	            return {classes: strclass};
	
	        },//隔行变色
	
	    });
	
	
	};
	
	
	//得到查询的参数
	
	oTableInit.queryParams = function (params) {
	
	    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	        limit: params.limit,    //params.limit,   页面大小
	        offset: params.offset,
	        testNum: 445,
	        testNum1: 343
	    };
	
	
	
	    return temp;
	};
	return oTableInit;
	};


####server-demo

	package com.selton;
	
	import javax.servlet.ServletException;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import java.io.IOException;
	import java.util.Enumeration;
	
	/**
	 * @author seltonzyf@gmail.com
	 * @date 2018/5/10 13:59
	 */
	@WebServlet(name = "DataSendServlet")
	public class DataSendServlet extends HttpServlet {
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
	        //这里输出后台从ajax拿到的数据
	        Enumeration<String> parameterNames = request.getParameterNames();
	        while (parameterNames.hasMoreElements()) {
	            String s = parameterNames.nextElement();
	            String parameter = request.getParameter(s);
	            System.out.println("s = " + s);
	            System.out.println("parameter = " + parameter);
	        }
	        response.getWriter().print("{\"total\": 11, \"rows\":[{\"testId\":9, \"testName\":\"selton\", \"testPassword\": 1}]}");
	    }
	
	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        doPost(request,response);
	    }
	}




####注意

- bootstrap-table只能被调用一次的问题

在inittable之前

	$("#table").bootstrapTable('destroy'); 

清空之前表内数据

- 服务器向前端发送的用于构建表单的json,所有的键都会变成小写
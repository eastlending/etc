Thrift
======

##编写thrift文件

* 参见两篇文章[Apache Thrift - 可伸缩的跨语言服务开发框架](https://www.ibm.com/developerworks/cn/java/j-lo-apachethrift/)
[Thrift使用指南](http://blog.csdn.net/njchenyi/article/details/8889013)


##Java Server端

* maven增加依赖
'''xml
<!-- RPC -->
<dependency>
    <groupId>org.apache.thrift</groupId>
    <artifactId>libthrift</artifactId>
    <version>0.9.2</version>
    <!--thrift 依赖的slf4j版本较低 如果已有slf4j版本较高则排除此依赖-->
    <exclusions>
        <exclusion>
            <artifactId>slf4j-api</artifactId>
            <groupId>org.slf4j</groupId>
        </exclusion>
    </exclusions>
</dependency>
'''

* 将thrift编译成java文件 
>thrift-0.9.2.exe -r -gen java ThriftService.thrift

* 编写服务端代码 示例在 [ThriftListener.java](https://github.com/eastlending/etc/blob/master/thrift/ThriftListener.java)




##使用 python client 测试

* 安装py2.7

* 下载thrift python 源代码 [Apache Thrift](https://github.com/apache/thrift)
>目前我们用的是0.9.2

* 安装py的thrift库 找到lib/py目录 命令行运行 
>$python setup.py install

* 将thrift编译成py文件 
>thrift-0.9.2.exe -r -gen py ThriftService.thrift 

>(如果有中文注释 需要在编译好的文件中加入 # coding=utf-8)

* 将ThriftUnitTest.py文件放在编译好的py文件目录下

* 修改 thrift_server_ip,thrift_server_port的值

* 在main函数内增加要测试的函数及参数列表 
>execute('methodName',[param1,param2])

* 运行

>安装[sublime package control](https://packagecontrol.io/installation)
>在packagecontrol中[安装repl](http://blog.sina.com.cn/s/blog_6476250d0101a881.html)并配置快捷键

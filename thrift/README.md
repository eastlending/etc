#Thrift
=======

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

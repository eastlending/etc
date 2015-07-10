RabbitMQ
======

##安装

首先安装erlang
[Erlang rpm 安装包](http://www.rabbitmq.com/releases/erlang/)

然后安装兔子mq server
[rhel rpm安装包](http://www.rabbitmq.com/install-rpm.html)


* 安装完成后就可以启动了 
>service rabbitmq-server stop

但是这个时候只有guest用户且只能本地访问 我们需要修改一下配置

首先修改兔子mq的环境配置/etc/rabbitmq/rabbitmq-env.conf
默认不存在要自己创建

CONFIG_FILE=/etc/rabbitmq/rabbitmq
指定配置文件地址


默认配置文件的位置是 /etc/rabbitmq/rabbitmq.config
这个文件默认是不存在的
创建一下
配置修改guest用户为可以远程访问
[
        {rabbit, [
                {log_levels, [{connection, error}]},
                {loopback_users,[]}
        ]}
].

这个配置文件 最后的那个 点 是必须要有的 这个是血与泪的教训,切记
{loopback_users,[]} 这个配置在默认里面是有写 guest的 置为空的意思是
不限制任何用户的远程登录

好,现在guest可以远程访问了,密码也是guest

然后我们要打开兔子mq的管理端
命令行键入
>rabbitmq-plugins enable rabbitmq_management

现在management插件开启了,可以通过命令行和web界面访问
默认web访问为http://server:15672/
用guest可以登录了(如果之前没有开启guest的远程访问就没法登录)

现在server端算是基本配置好了

##Java 

maven增加依赖:

	<!-- RPC -->
	<dependency>
    	<groupId>com.rabbitmq</groupId>
		<artifactId>amqp-client</artifactId>
		<version>3.5.3</version>
    </dependency>

发送消息的代码
[Send.java](https://github.com/eastlending/etc/blob/master/rabbitmq/Send.java)

处理消息的代码
[Consumer.java](https://github.com/eastlending/etc/blob/master/rabbitmq/Consumer.java)


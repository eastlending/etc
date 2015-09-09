package com.coamctech.eastlending.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.TopologyRecoveryException;
import com.rabbitmq.client.impl.DefaultExceptionHandler;
import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xc
 */
@WebListener
public class RabbitListener implements ServletContextListener{
    
    private static final String USR = "";
    private static final String PWD = "";
    private static final String HOST = "";
    private static final int PORT = 5672;
    private static final boolean AUTO_RECOVERY = Boolean.TRUE;
    private static final int CONN_TIMEOUT = 10000;
    private static final int REQ_CHANNEL_MAX = 0;//zero for unlimited
    
    private static Logger logger = LoggerFactory.getLogger(RabbitListener.class);

    private ConnectionFactory factory = new ConnectionFactory();
    
    private Connection conn = null;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            logger.info("creating mq connection......");
            factory.setUsername(USR);
            factory.setPassword(PWD);
            //		factory.setVirtualHost(5672);
            factory.setHost(HOST);
            factory.setPort(PORT);
            factory.setAutomaticRecoveryEnabled(AUTO_RECOVERY);
            factory.setConnectionTimeout(CONN_TIMEOUT);
            factory.setExceptionHandler(new LoggerExceptionHandler(logger));
            factory.setRequestedChannelMax(REQ_CHANNEL_MAX);
            conn = factory.newConnection();
            sce.getServletContext().setAttribute("rmqconn", conn);
            
            logger.info("mq conn create succes :{}",conn);
        } catch (IOException | TimeoutException ex) {
            logger.error("{}",ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            logger.info("closing mq conn");
            conn.close();
            logger.info("mq conn closed");
        } catch (IOException ex) {
            logger.info("mq conn close failed ex is {}",ex);
        }
    }
    
    public static class LoggerExceptionHandler extends DefaultExceptionHandler {

        Logger logger;

        public LoggerExceptionHandler(Logger logger) {
            this.logger = logger;
        }

        @Override
        public void handleUnexpectedConnectionDriverException(Connection conn, Throwable exception) {
            this.logger.error("UnexpectedConnectionDriverException ex is {} conn is {}", exception, conn.getClientProperties());
        }

        @Override
        public void handleConnectionRecoveryException(Connection conn, Throwable ex) {
           // ignore java.net.ConnectException as those are
            // expected during recovery and will only produce noisy
            // traces
            if (ex instanceof ConnectException) {
                this.logger.error("ConnectException expected during recovery {}",ex);
            } else {
                this.logger.error("unexpected exception during recovery ex is : {} | conn is {}",ex,conn);
            }
        }

        @Override
        public void handleChannelRecoveryException(Channel ch, Throwable ex) {
            this.logger.error("Caught an exception when recovering channel ex is {} | channel no is :{} ",
                    ch.getChannelNumber(),ex);
        }

        @Override
        public void handleTopologyRecoveryException(Connection conn, Channel ch, TopologyRecoveryException ex) {
            this.logger.error("Caught an exception when recovering topology ex is :{} | connection is {} | channel is {}",ex,conn,ch);
        }

        @Override
        protected void handleChannelKiller(Channel channel, Throwable ex, String what) {
            this.logger.error("DefaultExceptionHandler: {} threw an exception for channel  ex is: {}",what,channel,ex);
            try {
                channel.close(AMQP.REPLY_SUCCESS, "Closed due to exception from " + what);
            } catch (AlreadyClosedException ace) {
                this.logger.error("AlreadyClosedException ex is : {}",ace);
            } catch (TimeoutException te) {
                this.logger.error("TimeoutException is : {}",te);
            } catch (IOException ioe) {
                this.logger.error("Failure during close of channel {} after {} ",channel,ioe);
                channel.getConnection().abort(AMQP.INTERNAL_ERROR, "Internal error closing channel for " + what);
            }
        }

        @Override
        protected void handleConnectionKiller(Connection connection, Throwable exception, String what) {
            this.logger.error("DefaultExceptionHandler: {}  threw an exception for connection {},ex is {}",what,connection,exception);
            try {
                connection.close(AMQP.REPLY_SUCCESS, "Closed due to exception from " + what);
            } catch (AlreadyClosedException ace) {
                this.logger.error("AlreadyClosedException ex is : {}",ace);
            } catch (IOException ioe) {
                this.logger.error("Failure during close of connection {} after {} ",connection,ioe);
                connection.abort(AMQP.INTERNAL_ERROR, "Internal error closing connection for " + what);
            }
        }

    }
    
}

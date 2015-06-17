package xxxx.thrift;

import xxx.InvestRank;
import xxx.ThriftService;
import xxx.ThriftServiceBean;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xc
 */
@WebListener
public class ThriftListener implements ServletContextListener{
    
    Logger logger = LoggerFactory.getLogger(ThriftListener.class);
    
    private static Thread thriftServer;
    
    private static TServer server ;
    
    private static final Integer PORT = 7911;
    
    @EJB
    ThriftServiceBean serviceBean;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("\n\n\n\n ThriftListener contextInitialized...\n\n\n\n\n\n\n\n\n"); 
        synchronized(PORT){
            if(thriftServer!=null){
                return;
            }
            thriftServer = new Thread(new Runnable(){
                @Override
                public void run() {
                    try { 
                        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(PORT); 
                        TBinaryProtocol.Factory proFactory = new TBinaryProtocol.Factory(); 
                        TProcessor processor = new ThriftService.Processor(serviceBean);
                        TNonblockingServer.Args args = new TNonblockingServer.Args(serverTransport);
                        args.processor(processor);
                        args.protocolFactory(proFactory);
                        server = new TNonblockingServer(args); 
                        logger.info("\n\n\n\nStart thrift server on port {}...\n\n\n\n\n\n\n\n\n",PORT); 
                        server.serve(); 
                    } catch (TTransportException ex) { 
                        logger.error("\n\n\n Start thrift server failed Ex is : {}\n\n\n",ex);
                    } 
                }

            });
            thriftServer.start();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try{
            server.stop();
            logger.info("\n\n\n\n Thrift server on port {} stopped...\n\n\n\n\n\n\n\n\n",PORT); 
        }catch(Exception ex){
            logger.error("\n\n\n Stop thrift server failed Ex is : {}\n\n\n",ex);
        }
    }
    
}

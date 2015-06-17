# coding=utf-8  
'''  
Created on 20150616
  
@author: xc  
'''  
thrift_server_ip = '127.0.0.1'
thrift_server_port = 7911

import time
from datetime import datetime
  
import sys  
sys.path.append('../') #导入上下文环境  
  
from ThriftService import ThriftService  
from thrift import Thrift  
from thrift.transport import TSocket  
from thrift.transport import TTransport  
from thrift.protocol import TBinaryProtocol  
from thrift.protocol import TCompactProtocol

toLongTime = lambda x : int(time.mktime(time.strptime(x, '%Y-%m-%d %H:%M:%S'))) * 1000
iteratorPrint = lambda x : isinstance(x,list) or isinstance(x,tuple) or isinstance(x,map) or isinstance(x,set)


'''
funcName : 要执行的方法名
params : 参数列表
'''
def execute(funcName,params):
    print ("\r\n\r\nExecute test :" + funcName + "\n")
    try:  
        transport = TSocket.TSocket(thrift_server_ip, thrift_server_port)   
        transport = TTransport.TFramedTransport(transport)  
        protocol = TBinaryProtocol.TBinaryProtocol(transport)  
        client = ThriftService.Client(protocol)  
        transport.open()  
        clientMethod = getattr(client,funcName)

        print ("\ninvoke method : " + clientMethod.__name__)
        print ("\nwith params : " + str(params))

        result = apply(clientMethod,params)

        if result:
            print ("\nresult is :")
            if iteratorPrint(result) :
                for r in result:
                    print r
            else :
                print result
        transport.close()  
        
    except Thrift.TException, tx:  
        print '%s' % (tx.message)
    print ("\r\n\r\nEnd test :" + funcName)

if __name__ == '__main__':  
    execute('method',[param1 ,param2])

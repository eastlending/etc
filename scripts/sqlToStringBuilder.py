#coding:utf-8
'''
Created on 2014年8月29日
@author: xuechong
'''

if __name__ == '__main__':
    path = ''
    open(path)
    resultStr = 'StringBuilder sql = new StringBuilder();\n'
    warp = lambda x :'sql.append("' + x.replace('"', '\\"').replace('\n',' ') + '");\n'
    resultStr = reduce(lambda result,cur : result + warp(cur),open(path).readlines(),resultStr)
    print (resultStr)
    input("")

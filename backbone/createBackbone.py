#coding:utf-8
'''
20151205 create
Author : xc

$ python createBackbone.py moduleName
'''
import sys
import os
moduleNameInner = "test1"

tplList = ["apptpl","modeltpl","viewtpl"]


getSysArg = lambda index,defaultval : len(sys.argv)>1 and str(sys.argv[index]) or defaultval

__MODULE_NAME_KEY__ = "${moduleName}"
moduleName = getSysArg(1,moduleNameInner)

print ("moduleName is : %s"%moduleName)

isKey = lambda s,key: s is not None and s.strip().find(key) is 0
getKey = lambda s,key : s.replace(key,"").strip()

isNameSpace = lambda s:isKey(s,"namespace:")
getNameSpace = lambda s:getKey(s,"namespace:")

isDestFileName = lambda s:isKey(s,"destfile:")
getDestFileName = lambda s:getKey(s,"destfile:")


cwd = os.getcwd()
print(cwd)

__DEST_FILE_PATH__ =cwd + os.sep + "createdFiles" + os.sep 

def processLine(s):
	return replaceKeyWords(s)

def replaceKeyWords(s):
	result = s.replace(__MODULE_NAME_KEY__,moduleName)
	return result

def processTpl(tplPath):
	tpl = open(tplPath)
	destFileNameSpace = ""
	destFileName = moduleName

	resultStrs = []
	for line in tpl.readlines():
		if isNameSpace(line) : 
			destFileNameSpace = replaceKeyWords(getNameSpace(line))
		elif isDestFileName(line) : 
			destFileName = replaceKeyWords(getDestFileName(line))
		else:
			resultStrs.append(processLine(line))
	result = "".join(resultStrs)

	destFilePath =__DEST_FILE_PATH__ + destFileNameSpace

	print("dest file path is %s"%destFileNameSpace)
	print("dest file name is %s"%destFileName)
	
	print(result)
	if not os.path.exists(destFilePath):
		os.makedirs(destFilePath)
	destFile = open(destFilePath + os.sep + destFileName ,"w+")
	destFile.write(result)

	destFile.close()
	tpl.close()

	pass

for tpl in tplList:
	processTpl(cwd + os.sep + tpl)
	pass

print("ok")





#coding:utf-8

from Crypto.Cipher import AES
import base64

_KEY= str(bytearray('keykeykeykey').ljust(16,'\x00'))

_IV = str(bytearray('iviviviviv').ljust(16,'\x00'))

_MODE = AES.MODE_CBC

BS = AES.block_size
pad = lambda s: s + (BS - len(s) % BS) * chr(BS - len(s) % BS)
unpad = lambda s : s[0:-ord(s[-1])]

def encodeAes(ori_str):
	cipher = AES.new(key=_KEY, mode=AES.MODE_CBC, IV=_IV)
	return base64.standard_b64encode(cipher.encrypt(pad(ori_str)))

def decodeAes(encoded_str):
	cipher = AES.new(key=_KEY, mode=AES.MODE_CBC, IV=_IV)
	return unpad(cipher.decrypt(base64.standard_b64decode(encoded_str)))

print(encodeAes('asdasdasdasdasdasd'))

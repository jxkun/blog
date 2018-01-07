# python socket/socketserver笔记
## Socket
A socket API is an application programming interface (API), usually provided by the operating system, that allows application programs to control and use network sockets. Internet socket APIs are usually based on the Berkeley sockets standard. In the Berkeley sockets standard, sockets are a form of file descriptor (a file handle), due to the Unix philosophy that "everything is a file", and the analogies between sockets and files: you can read, write, open, and close both. In practice the differences mean the analogy is strained, and one instead use different interfaces (send and receive) on a socket. In inter-process communication, each end will generally have its own socket, but these may use different APIs: they are abstracted by the network protocol.

## Socket Families
A socket address is the combination of an IP address and a port number, much like one end of a telephone connection is the combination of a phone number and a particular extension. Sockets need not have an address (for example for only sending data), but if a program binds a socket to an address, the socket can be used to receive data sent to that address. Based on this address, internet sockets deliver incoming data packets to the appropriate application process or thread.
```python
# Socket Families(地址簇)
socket.AF_UNIX   # unix本机进程间通信 
socket.AF_INET　# IPV4　
socket.AF_INET6  # IPV6
```
These constants represent the address (and protocol) families, used for the first argument to socket(). If the AF_UNIX constant is not defined then this protocol is unsupported. **More constants may be available depending on the system.**

## Socket Type
```python
# Socket Types
socket.SOCK_STREAM  #for tcp
socket.SOCK_DGRAM   #for udp 
socket.SOCK_RAW  #原始套接字，普通的套接字无法处理ICMP、IGMP等网络报文，而SOCK_RAW可以；其次，SOCK_RAW也可以处理特殊的IPv4报文；此外，利用原始套接字，可以通过IP_HDRINCL套接字选项由用户构造IP头。        
socket.SOCK_RDM  #是一种可靠的UDP形式，即保证交付数据报但不保证顺序。SOCK_RAM用来提供对原始协议的低级访问，在需要执行某些特殊操作时使用，如发送ICMP报文。SOCK_RAM通常仅限于高级用户或管理员运行的程序使用。
socket.SOCK_SEQPACKET #废弃了
```                         
**These constants represent the socket types, used for the second argument to socket().** More constants may be available depending on the system. (**Only SOCK_STREAM and SOCK_DGRAM appear to be generally useful.**)

## Socket 方法
### 1、创建socket对象
```python
socket.socket( family=AF_INET, type=SOCK_STREAM, proto=0, fileno=None)
def __init__(self, family=AF_INET, type=SOCK_STREAM, proto=0, fileno=None) 
```
Create a new socket using the given ***address family, socket type and protocol number***.  
The address family should be ***AF_INET (the default),  AF_INET6,  AF_UNIX,  AF_CAN  or  AF_RDS.***  
The socket type should be ***SOCK_STREAM (the default),  SOCK_DGRAM,  SOCK_RAW or perhaps one of the other  SOCK_ constants.***  
The protocol number is usually ***zero and may be omitted*** or in the case where the address family is AF_CAN the protocol should be one of CAN_RAW or CAN_BCM.   
***If fileno is specified, the other arguments are ignored, causing the socket with the specified file descriptor to return. Unlike socket.fromfd(), fileno will return the same socket and not a duplicate. This may help close a detached socket using socket.close().***

```python
socket.socketpair([family[, type[, proto]]])
def socketpair(family=AF_INET, type=SOCK_STREAM, proto=0)
```
***Build a pair of connected socket objects*** using the given address family, socket type, and protocol number. Address family, socket type, and protocol number are as for the socket() function above. The default family is AF_UNIX if defined on the platform; otherwise, the default is AF_INET.

```python
socket.create_connection(address[, timeout[, source_address]])
def create_connection(address, timeout=_GLOBAL_DEFAULT_TIMEOUT, source_address=None):
```
***Connect to a TCP service listening on the Internet address (a 2-tuple (host, port)), and return the socket object.*** This is a higher-level function than socket.connect(): if host is a non-numeric hostname, it will try to resolve it for both AF_INET and AF_INET6, and then try to connect to all possible addresses in turn until a connection succeeds. This makes it easy to write clients that are compatible to both IPv4 and IPv6.

Passing the optional timeout parameter will set the timeout on the socket instance before attempting to connect. ***If no timeout is supplied, the global default timeout setting returned by getdefaulttimeout() is used.***

If supplied, source_address must be a 2-tuple (host, port) for the socket to bind to as its source address before connecting. If host or port are ‘’ or 0 respectively the OS default behavior will be used.

### 2、绑定和监听
```python
socket.getaddrinfo(host, port, family=0, type=0, proto=0, flags=0) #获取要连接的对端主机地址
sk = socket.scoket()
sk.bind(address)

def getaddrinfo(host, port, family=0, type=0, proto=0, flags=0)
def bind(self, address)
```
　　sk.bind(address) 将套接字绑定到地址。address地址的格式取决于地址族。在AF_INET下，以元组（host,port）的形式表示地址。
```python
sk = socket.socket()
sk.listen(backlog)
def listen(self, backlog=None)
```
- 开始监听传入连接。backlog指定在拒绝连接之前，可以挂起的最大连接数量。例如：
backlog等于5，表示内核已经接到了连接请求，但服务器还没有调用accept进行处理的连接个数最大为5。
- 这个值不能无限大，因为要在内核中维护连接队列。

### socket对象的一些方法
注 ： 下面的sk 为创建的socket对象，sk = socket.scoket()
>sk.setblocking(bool)
设置是否阻塞（默认True），如果设置False，那么accept和recv时一旦无数据，则报错。

>sk.accept()
接受连接并返回（conn,address）,其中conn是新的套接字对象，可以用来接收和发送数据。address是连接客户端的地址。
接收TCP 客户的连接（阻塞式）等待连接的到来

>sk.connect(address)
接收TCP 客户的连接（阻塞式）等待连接的到来；连接到address处的套接字。一般，address的格式为元组（hostname,port）,如果连接出错，返回socket.error错误。

>sk.connect_ex(address)
同上，只不过会有返回值，连接成功时返回 0 ，连接失败时候返回编码，例如：10061

>sk.close()
关闭套接字

>sk.recv(bufsize[,flag])
def recv(self, buffersize, flags=None)
接受套接字的数据。数据以字符串形式返回，bufsize指定最多可以接收的数量。flag提供有关消息的其他信息，通常可以忽略。

>sk.recvfrom(bufsize[.flag])
def recvfrom(self, buffersize, flags=None)
与recv()类似，但返回值是（data,address）。其中data是包含接收数据的字符串，address是发送数据的套接字地址。

>sk.send(string[,flag])
def send(self, data, flags=None)
将string中的数据发送到连接的套接字。返回值是要发送的字节数量，该数量可能小于string的字节大小。即：可能未将指定内容全部发送。

>sk.sendall(string[,flag])
将string中的数据发送到连接的套接字，但在返回之前会尝试发送所有数据。成功返回None，失败则抛出异常。
内部通过递归调用send，将所有内容发送出去。

>sk.sendto(string[,flag],address)
将数据发送到套接字，address是形式为（ipaddr，port）的元组，指定远程地址。返回值是发送的字节数。该函数主要用于UDP协议。

>sk.settimeout(timeout)
设置套接字操作的超时期，timeout是一个浮点数，单位是秒。值为None表示没有超时期。一般，超时期应该在刚创建套接字时设置，因为它们可能用于连接的操作（如 client 连接最多等待5s ）

>sk.getpeername()
返回连接套接字的远程地址。返回值通常是元组（ipaddr,port）。

>sk.getsockname()
返回套接字自己的地址。通常是一个元组(ipaddr,port)

>sk.fileno()
套接字的文件描述符

>socket.sendfile(file, offset=0, count=None)
发送文件 ，但目前多数情况下并无什么用。

### socket案例
 ```python
#_*_coding:utf-8_*_
import socket
import os,subprocess

server = socket.socket() #获得socket实例
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind(("localhost",9999)) #绑定ip port
server.listen()  #开始监听

while True: #第一层loop
    print("等待客户端的连接...")
    conn,addr = server.accept() #接受并建立与客户端的连接,程序在此处开始阻塞,只到有客户端连接进来...
    print("新连接:",addr )
    while True:
        data = conn.recv(1024)
        if not data:
            print("客户端断开了...")
            break #这里断开就会再次回到第一次外层的loop
        print("收到命令:",data)
        #res = os.popen(data.decode()).read() #py3 里socket发送的只有bytes,os.popen又只能接受str,所以要decode一下
        res = subprocess.Popen(data,shell=True,stdout=subprocess.PIPE).stdout.read() #跟上面那条命令的效果是一样的
        if len(res) == 0:
            res = "cmd exec success,has not output!".encode("utf-8")
        conn.send(str(len(res)).encode("utf-8")) #发送数据之前,先告诉客户端要发多少数据给它
        print("等待客户ack应答...")
        client_final_ack = conn.recv(1024) #等待客户端响应
        print("客户应答:",client_final_ack.decode())
        print(type(res))
        conn.sendall(res) #发送端也有最大数据量限制,所以这里用sendall,相当于重复循环调用conn.send,直至数据发送完毕
server.close()
```
```python
#_*_coding:utf-8_*_

import socket
import sys
client = socket.socket()
client.connect(("localhost",9999))

while True:
    msg = input(">>:").strip()
    if len(msg) == 0:continue
    client.send( msg.encode("utf-8") )

    res_return_size  = client.recv(1024) #接收这条命令执行结果的大小
    print("getting cmd result , ", res_return_size)
    total_rece_size = int(res_return_size)
    print("total size:",res_return_size)
    client.send("准备好接收了,发吧loser".encode("utf-8"))
    received_size = 0 #已接收到的数据
    cmd_res = b''
    f = open("test_copy.html","wb")#把接收到的结果存下来,一会看看收到的数据 对不对
    while received_size != total_rece_size: #代表还没收完
        data = client.recv(1024)
        received_size += len(data) #为什么不是直接1024,还判断len干嘛,注意,实际收到的data有可能比1024少
        cmd_res += data
    else:
        print("数据收完了",received_size)
        #print(cmd_res.decode())
        f.write(cmd_res) #把接收到的结果存下来,一会看看收到的数据 对不对
    #print(data.decode()) #命令执行结果
client.close()
```
 ## SocketServer
The socketserver module simplifies the task of writing network servers.
### socketserver一共有这么几种类型
#### 1、TCPServer
```python
class socketserver.TCPServer(server_address, RequestHandlerClass, bind_and_activate=True)
```
This uses the Internet TCP protocol, which provides for continuous streams of data between the client and server. 
#### 2、UDPServer
```python
class socketserver.UDPServer(server_address, RequestHandlerClass, bind_and_activate=True)
```
This uses datagrams, which are discrete packets of information that may arrive out of order or be lost while in transit. The parameters are the same as for TCPServer.
#### 3、UnixStreamServer 和  UnixDatagramServer
```python
class socketserver.UnixStreamServer(server_address, RequestHandlerClass, bind_and_activate=True)
class socketserver.UnixDatagramServer(server_address, RequestHandlerClass,bind_and_activate=True)
```
These more infrequently used classes are similar to the TCP and UDP classes, but use Unix domain sockets; they’re not available on non-Unix platforms. The parameters are the same as for TCPServer.

***There are five classes in an inheritance diagram, four of which represent synchronous servers of four types:***
```
+------------+
| BaseServer |
+------------+
      |
      v
+-----------+        +------------------+
| TCPServer |------->| UnixStreamServer |
+-----------+        +------------------+
      |
      v
+-----------+        +--------------------+
| UDPServer |------->| UnixDatagramServer |
+-----------+        +--------------------+
```

### 创建一个socketserver 至少分以下几步:

- First, you must create a request handler class by subclassing the BaseRequestHandlerclass and overriding its handle() method; this method will process incoming requests. 　　
- Second, you must instantiate one of the server classes, passing it the server’s address and the request handler class.
- Then call the handle_request() orserve_forever() method of the server object to process one or many requests.
- Finally, call server_close() to close the socket.

### socketServer基本使用
```python
import socketserver

class MyTCPHandler(socketserver.BaseRequestHandler):
    """
    The request handler class for our server.
    It is instantiated once per connection to the server, and must
    override the handle() method to implement communication to the
    client.
    """
    def handle(self):
        # self.request is the TCP socket connected to the client
        self.data = self.request.recv(1024).strip()
        print("{} wrote:".format(self.client_address[0]))
        print(self.data)
        # just send back the same data, but upper-cased
        self.request.sendall(self.data.upper())

if __name__ == "__main__":
    HOST, PORT = "localhost", 9999
   # Create the server, binding to localhost on port 9999
    server = socketserver.TCPServer((HOST, PORT), MyTCPHandler)
    # Activate the server; this will keep running until you
    # interrupt the program with Ctrl-C
    server.serve_forever()
```

### socketserver并发起来， 必须选择使用以下一个多并发的类
```python
class socketserver.ForkingTCPServer
class socketserver.ForkingUDPServer
class socketserver.ThreadingTCPServer
class socketserver.ThreadingUDPServer
```
#### 使上例支持并发,，客户端每连进一个来，服务器端就会分配一个新的线程来处理这个客户端的请求
```python
# 将上例中的 server = socketserver.TCPServer((HOST, PORT), MyTCPHandler) 替换为下面的
server = socketserver.ThreadingTCPServer((HOST, PORT), MyTCPHandler)
```
### class socketserver.BaseServer(server_address, RequestHandlerClass) 主要有以下方法
```python
class socketserver.BaseServer(server_address, RequestHandlerClass)
This is the superclass of all Server objects in the module. It defines the interface, given below, but does not implement most of the methods, which is done in subclasses. The two parameters are stored in the respective server_address and RequestHandlerClass attributes.

fileno()
Return an integer file descriptor for the socket on which the server is listening. This function is most commonly passed to selectors, to allow monitoring multiple servers in the same process.

handle_request()
Process a single request. This function calls the following methods in order: get_request(), verify_request(), and process_request(). If the user-provided handle() method of the handler class raises an exception, the server’s handle_error() method will be called. If no request is received within timeout seconds, handle_timeout() will be called and handle_request() will return.

serve_forever(poll_interval=0.5)
Handle requests until an explicit shutdown() request. Poll for shutdown every poll_interval seconds. Ignores the timeout attribute. It also calls service_actions(), which may be used by a subclass or mixin to provide actions specific to a given service. For example, the ForkingMixIn class uses service_actions() to clean up zombie child processes.

Changed in version 3.3: Added service_actions call to the serve_forever method.

service_actions()
This is called in the serve_forever() loop. This method can be overridden by subclasses or mixin classes to perform actions specific to a given service, such as cleanup actions.

New in version 3.3.

shutdown()
Tell the serve_forever() loop to stop and wait until it does.

server_close()
Clean up the server. May be overridden.

address_family
The family of protocols to which the server’s socket belongs. Common examples are socket.AF_INET and socket.AF_UNIX.

RequestHandlerClass
The user-provided request handler class; an instance of this class is created for each request.

server_address
The address on which the server is listening. The format of addresses varies depending on the protocol family; see the documentation for the socket module for details. For Internet protocols, this is a tuple containing a string giving the address, and an integer port number: ('127.0.0.1', 80), for example.

socket
The socket object on which the server will listen for incoming requests.

The server classes support the following class variables:

allow_reuse_address
Whether the server will allow the reuse of an address. This defaults to False, and can be set in subclasses to change the policy.

request_queue_size
The size of the request queue. If it takes a long time to process a single request, any requests that arrive while the server is busy are placed into a queue, up to request_queue_size requests. Once the queue is full, further requests from clients will get a “Connection denied” error. The default value is usually 5, but this can be overridden by subclasses.

socket_type
The type of socket used by the server; socket.SOCK_STREAM and socket.SOCK_DGRAM are two common values.

timeout
Timeout duration, measured in seconds, or None if no timeout is desired. If handle_request() receives no incoming requests within the timeout period, the handle_timeout() method is called.

There are various server methods that can be overridden by subclasses of base server classes like TCPServer; these methods aren’t useful to external users of the server object.

finish_request()
Actually processes the request by instantiating RequestHandlerClass and calling its handle() method.

get_request()
Must accept a request from the socket, and return a 2-tuple containing the new socket object to be used to communicate with the client, and the client’s address.

handle_error(request, client_address)
This function is called if the handle() method of a RequestHandlerClass instance raises an exception. The default action is to print the traceback to standard output and continue handling further requests.

handle_timeout()
This function is called when the timeout attribute has been set to a value other than None and the timeout period has passed with no requests being received. The default action for forking servers is to collect the status of any child processes that have exited, while in threading servers this method does nothing.

process_request(request, client_address)
Calls finish_request() to create an instance of the RequestHandlerClass. If desired, this function can create a new process or thread to handle the request; the ForkingMixIn and ThreadingMixIn classes do this.

server_activate()
Called by the server’s constructor to activate the server. The default behavior for a TCP server just invokes listen() on the server’s socket. May be overridden.

server_bind()
Called by the server’s constructor to bind the socket to the desired address. May be overridden.

verify_request(request, client_address)
Must return a Boolean value; if the value is True, the request will be processed, and if it’s False, the request will be denied. This function can be overridden to implement access controls for a server. The default implementation always returns True.
```


  
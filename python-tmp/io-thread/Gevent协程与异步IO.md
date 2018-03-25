####  内容目录  
1. 协程
     1. [协程概念](#协程)
     2.  Greenlet
     3. Gevent
2. 事件驱动
3. 异步IO


## 1. 协程
#### 1.1 概念 
协程，又称微线程，纤程。英文名Coroutine。一句话说明什么是线程：**协程是一种用户态的轻量级线程。**

协程拥有自己的寄存器上下文和栈。协程调度切换时，将寄存器上下文和栈保存到其他地方，在切回来的时候，恢复先前保存的寄存器上下文和栈。因此：

协程能保留上一次调用时的状态（即所有局部状态的一个特定组合），每次过程重入时，就相当于进入上一次调用的状态，换种说法：进入上一次离开时所处逻辑流的位置。

协程的好处：
- 无需线程上下文切换的开销。
- 无需原子操作锁定及同步的开销 。
"原子操作(atomic operation)是不需要synchronized"，所谓原子操作是指不会被线程调度机制打断的操作；这种操作一旦开始，就一直运行到结束，中间不会有任何 context switch （切换到另一个线程）。原子操作可以是一个步骤，也可以是多个操作步骤，但是其顺序是不可以被打乱，或者切割掉只执行部分。视作整体是原子性的核心。
- 方便切换控制流，简化编程模型。
- 高并发+高扩展性+低成本：一个CPU支持上万的协程都不是问题。所以很适合用于高并发处理。

缺点：
- 无法利用多核资源：协程的本质是个单线程,它不能同时将 单个CPU 的多个核用上,协程需要和进程配合才能运行在多CPU上.当然我们日常所编写的绝大部分应用都没有这个必要，除非是cpu密集型应用。
- 进行阻塞（Blocking）操作（如IO时）会阻塞掉整个程序。  
使用yield模仿协程操作例子（但并不是协程）：  

```python
import time, queue

def consumer(n):
	print("starting consumer")
	while True:
		item = yield
		print(n, item)
def producer(*args):
	item = 0
	while item < 5:
		item += 1
		for i in args:
			i.send(item)
		print("producer",item)
con1 = consumer("con1")
con1.__next__()
con2 = consumer("con2")
con2.__next__()
producer(con1, con2)
```
**称之为协程的条件**
- 必须在只有一个单线程里实现并发
- 修改共享数据不需加锁
- 用户程序里自己保存多个控制流的上下文栈
- 一个协程遇到IO操作自动切换到其它协程

#### 1.2 Greenlet
greenlet是一个用C实现的协程模块，相比与python自带的yield，它可以使你在任意函数之间随意切换，而不需把这个函数先声明为generator
```python
from greenlet import greenlet
def test1():
    print(12)
    gr2.switch() # 调用switch()函数切换到函数test1
    print(34)
    gr2.switch()
 
def test2():
    print(56)
    gr1.switch() # 调用switch()函数切换到函数test1
    print(78)
  
gr1 = greenlet(test1) # 创建greenlet对象
gr2 = greenlet(test2)
gr1.switch() # 将函数切换到test1
```
注： 虽然比generator要简单了，但是还有遇到IO操作，自动切换的问题。
#### 1.3 Gevent 
Gevent 是一个第三方库，可以轻松通过gevent实现并发同步或异步编程，在gevent中用到的主要模式是Greenlet, 它是以C扩展模块形式接入Python的轻量级协程。 Greenlet全部运行在主程序操作系统进程的内部，但它们被协作式地调度。
```python
import gevent
 
def run1():
 	print("run1 start")
 	gevent.sleep(2) 
 	print("run1 end")

def run2():
	print("run2 start")
	gevent.sleep(1)
	print("run2 end")

# 遇到IO阻塞时会自动切换任务
gevent.joinall([
	gevent.spawn(run1),
	gevent.spawn(run2)
	])
--------输出结果：
run1 start
run2 start
run2 end
run1 end
[Finished in 2.3s]
```
通过gevent实现单线程下的多socket并发,demo:     
[server](https://github.com/lunarku/blog/blob/master/python-tmp/code/Gevent-issue/socket/server.py)  
 [client](https://github.com/lunarku/blog/blob/master/python-tmp/code/Gevent-issue/socket/client.py)

## 2. 事件驱动与异步IO
通常，我们写服务器处理模型的程序时，有以下几种模型：
（1）每收到一个请求，创建一个新的进程，来处理该请求；
（2）每收到一个请求，创建一个新的线程，来处理该请求；
（3）每收到一个请求，放入一个事件列表，让主进程通过非阻塞I/O方式来处理请求
上面的几种方式，各有千秋，
第（1）中方法，由于创建新的进程的开销比较大，所以，会导致服务器性能比较差,但实现比较简单。
第（2）种方式，由于要涉及到线程的同步，有可能会面临死锁等问题。
第（3）种方式，在写应用程序代码时，逻辑比前面两种都复杂。
综合考虑各方面因素，一般普遍认为第（3）种方式是大多数网络服务器采用的方式









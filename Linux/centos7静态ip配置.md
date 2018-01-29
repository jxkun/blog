#### 内容目录
1. net模式连接，配置静态ip
2. 三种虚拟网络连接模式

### 1. net模式下，静态ip配置
>service network restart # 重启网卡服务  
ip addr 查看配置是否成功.  
route 查看默认网关

几个配置好的例子:
```
# ]# cat /etc/sysconfig/network-scripts/ifcfg-eth0  
HWADDR="00:15:5D:07:F1:02"  
TYPE="Ethernet"  
BOOTPROTO="static" #dhcp改为static   
DEFROUTE="yes"  
PEERDNS="yes"  
PEERROUTES="yes"  
IPV4_FAILURE_FATAL="no"  
IPV6INIT="yes"  
IPV6_AUTOCONF="yes"  
IPV6_DEFROUTE="yes"  
IPV6_PEERDNS="yes"  
IPV6_PEERROUTES="yes"  
IPV6_FAILURE_FATAL="no"  
NAME="eth0"  
UUID="bb3a302d-dc46-461a-881e-d46cafd0eb71"  
ONBOOT="yes" #开机启用本配置  
IPADDR=192.168.7.106 # 静态IP  
GATEWAY=192.168.7.1  # 默认网关，网关默认最后一位为1，可以通过虚拟机中编辑选项进入虚拟网络编辑查看
NETMASK=255.255.255.0 # 子网掩码  
DNS1=192.168.7.1 #DNS 配置，不知道具体DNS地址可以直接配置为网关地址，会自动通过网关去找DNS地址  
```

### 2. 三种虚拟网络连接模式
#### 2.1 Net连接模式
VMware中每创建一台虚拟机，都会为创建的虚拟机生成一块虚拟网卡，通过ifconfig命令可以查看虚拟网卡。  

Net模式下，虚拟机的网卡需要经过虚拟网关才能进行通信（网关可以看做为VMware生成的虚拟路由器，一般默认ip为网段地址最后一位为1，网关地址可以通过VMware的编辑选项进入虚拟网络编辑器，选中net模式，下面会有net设置，进入net设置可以看到VMware生成的网关地址）。虚拟网关通过连接VMware在真实计算机上生成的虚拟网卡vmnet8与外部网络进行通信。

因此配置net模式下的静态ip地址需要配置：静态ip(IPADDR), 虚拟网关(GATEWAY),子网掩码(NETMASK)

特点: 虚拟机ip为真实计算机连接的局域网的一个子网网段,虚拟机ip与局域网的其他真实计算机不会产生冲突。

#### 2.2 桥接模式
虚拟机的虚拟网卡直接连接到VMware在真实计算机上生成的网桥vmnet0上，然后在与外部网络通信。虚拟机网关为局域网中的真实路由器，虚拟机的ip由局域网的真实路由器分发。

特地： 虚拟机ip与局域网其他计算机同属于一个网段。


#### 2.3 host only模式
此模式下虚拟机只能与本地计算机通信，不能连接外部网络。虚拟机连接在vmnet1这个虚拟网卡上，由此网卡充当网关。







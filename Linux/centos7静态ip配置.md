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

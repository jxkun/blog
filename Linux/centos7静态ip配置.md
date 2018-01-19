

>vim /etc/sysconfig/network-scripts/ifcfg-eth0 #修改ifcfg文件
```
DEVICE=eth0(默认)
HWADDR=00:0C:29:2E:36:16(默认)
TYPE=Ethernet(默认)
UUID=XXXXXXX(默认)
ONBOOT=yes(默认为no,修改为yes意为每次reboot后 ifup eth0)
MM_CONTROLLED=yes(默认)

#BOOTPROTO=dhcp(dhcp为自动分配ip地址,我们把他注释了，在下面另外加)
BOOTPROTO=static(新添加)
IPV6INIT=no(新添加)
USERCTL=no(新添加)
IPADDR=192.168.164.100(新添加)
NETMASK=255.255.255.0(新添加)
```

>service network restart # 重启网卡服务  
ip addr 查看配置是否成功.

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
IPADDR=192.168.7.106 #静态IP  
GATEWAY=192.168.7.1 #默认网关  
NETMASK=255.255.255.0 #子网掩码  
DNS1=192.168.7.1 #DNS 配置  
```



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

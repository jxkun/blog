package com.lunarku.bigdata.zkdist;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class DistributedServer {

	private static final String CONNECT_STRING=
			"192.168.25.141:2181,192.168.25.142:2181,192.168.25.143:2181";
	private static final int SESSION_TIMEOUT = 2000;
	
	private static final String GROUP_NODE = "/servers";
	public ZooKeeper zk = null; 
	
	/**
	 * 获取Zookeeper连接
	 * @throws Exception
	 */
	public void getConnect() throws Exception {
		zk = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				// 收到事件通知后的回调函数
				System.out.println(event.getType() + "---" + event.getPath());
				try {
					zk.getChildren("/", true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 注册服务
	 * @param hostName
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void registerServer(String hostName) throws KeeperException, InterruptedException {
		Stat exists = zk.exists(GROUP_NODE, false);
		if(exists == null) {
			zk.create(GROUP_NODE, "parent node".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		String create = zk.create(GROUP_NODE + "/" + "server", 
				hostName.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		
		System.out.println(hostName + " is online: " + create);
	}
	
	/**
	 * 模拟业务处理过程
	 * @param hostName
	 * @throws Exception
	 */
	public void handleBussiness(String hostName) throws Exception {
		System.out.println(hostName + " start working... ");
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public static void main(String[] args) throws Exception {
		
		// 获取zk连接
		DistributedServer server = new DistributedServer();
		server.getConnect();
		// 利用zk连接注册服务器信息
		server.registerServer(args[0]);
		// 启动业务功能
		server.handleBussiness(args[0]);
	}
	
	
}

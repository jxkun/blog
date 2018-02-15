package com.lunarku.bigdata.zkdist;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * DistributeClient  DistributeServer 
 * zookeeper 实现系统服务器上下线动态感知
 */
public class DistributeClient {

	private static final String CONNECT_STRING=
			"192.168.25.141:2181,192.168.25.142:2181,192.168.25.143:2181";
	private static final int SESSION_TIMEOUT = 2000;
	
	private static final String GROUP_NODE = "/servers";
	public volatile List<String> serverList; // 加上volatile关键字修饰，保持数据一致性
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
					getServerList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 获取服务列表
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void getServerList() throws KeeperException, InterruptedException {
		List<String> children = zk.getChildren(GROUP_NODE, true);
		List<String> servers = new ArrayList<String>();
		
		for(String child : children) {
			byte[] data = zk.getData(GROUP_NODE + "/" + child,  false, null);
			servers.add(new String(data));
		}
		serverList = servers;
		System.out.println(serverList);
	}
	
	/**
	 * 模拟业务处理过程
	 * @throws Exception
	 */
	public void handleBussiness() throws Exception {
		System.out.println("client start working... ");
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public static void main(String[] args) throws Exception {
		// 获取zookeeper连接
		DistributeClient client = new DistributeClient();
		client.getConnect();
		//获取服务列表
		client.getServerList();
		// 处理业务
		client.handleBussiness();
	}
	
}

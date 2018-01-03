package net.sea.simple.rpc.server;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;

import com.github.zkclient.IZkChildListener;
import com.github.zkclient.IZkDataListener;
import com.github.zkclient.ZkClient;

public class ZKClient {
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		// final ZkClient client = new
		// ZkClient("192.168.118.100:2181,192.168.118.200:2181,192.168.118.201:2181",
		// 50,
		// 10000);
		// client.createPersistent("/test", true);
		// System.out.println("创建完成");
		// new Thread(() -> {
		// client.watchForData("/test");
		// client.subscribeDataChanges("/test", new IZkDataListener() {
		//
		// @Override
		// public void handleDataDeleted(String dataPath) throws Exception {
		// // TODO Auto-generated method stub
		// System.out.println("delete data");
		// }
		//
		// @Override
		// public void handleDataChange(String dataPath, Object data) throws
		// Exception {
		// // TODO Auto-generated method stub
		// System.out.println("change data");
		// }
		// });
		// System.out.println("------------------------");
		// }).start();
		// try {
		// Thread.sleep(1000);
		// System.out.println("sleep 1s");
		// Thread.sleep(1000);
		// System.out.println("sleep 2s");
		// Thread.sleep(100000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// while (true) {
		// client.subscribeDataChanges("/test", new IZkDataListener() {
		//
		// @Override
		// public void handleDataDeleted(String arg0) throws Exception {
		// // TODO Auto-generated method stub
		// System.out.println(arg0);
		// }
		//
		// @Override
		// public void handleDataChange(String arg0, Object arg1) throws
		// Exception {
		// // TODO Auto-generated method stub
		// System.out.println(arg0);
		// System.out.println(arg1);
		// }
		// });
		// }
		// ZooKeeper zk = new
		// ZooKeeper("192.168.118.100:2181,192.168.118.200:2181,192.168.118.201:2181",
		// 50,
		// new Watcher() {
		//
		// @Override
		// public void process(WatchedEvent event) {
		// // TODO Auto-generated method stub
		// System.out.println(event);
		// }
		// });
		// zk.create("/test", "test".getBytes(), Ids.OPEN_ACL_UNSAFE,
		// CreateMode.EPHEMERAL);
		ZkClient client = new ZkClient("192.168.118.100:2181,192.168.118.200:2181,192.168.118.201:2181");
		client.subscribeDataChanges("/test", new IZkDataListener() {

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("delete data");
			}

			@Override
			public void handleDataChange(String dataPath, byte[] data) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("change data");
			}
		});
		client.subscribeChildChanges("/", new IZkChildListener() {

			@Override
			public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
				// TODO Auto-generated method stub
				System.out.println(parentPath + ":" + currentChildren);
			}
		});
		client.createEphemeral("/test", "test".getBytes());
		Thread.sleep(60000);
	}
}

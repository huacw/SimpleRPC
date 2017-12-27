package net.sea.simple.rpc.server;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class ZKClient {
	public static void main(String[] args) {
		ZkClient client = new ZkClient("192.168.118.100:2181,192.168.118.200:2181,192.168.118.201:2181", 10000);
		while (true) {
			client.subscribeDataChanges("/rpc", new IZkDataListener() {

				@Override
				public void handleDataDeleted(String arg0) throws Exception {
					// TODO Auto-generated method stub
					System.out.println(arg0);
				}

				@Override
				public void handleDataChange(String arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					System.out.println(arg0);
					System.out.println(arg1);
				}
			});
		}

	}
}

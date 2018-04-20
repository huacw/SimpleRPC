package net.sea.simple.rpc.register;

import java.util.List;

import net.sea.simple.rpc.server.RegisterCenterConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.github.zkclient.IZkChildListener;
import com.github.zkclient.IZkDataListener;
import com.github.zkclient.IZkStateListener;
import com.github.zkclient.ZkClient;

import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.utils.JsonUtils;
import org.springframework.stereotype.Component;

/**
 * 服务注册器
 * 
 * @author sea
 *
 */
@Component
public class ServiceRegister {
	private static final Register REG_INST = Register.newInstance();
	private static RegisterCenterConfig config;

	public ServiceRegister() {
		System.out.println("config="+config);
	}

	/**
	 * 注册器
	 * 
	 * @author sea
	 *
	 */
	private static class Register {
		private volatile static Register register = null;
		private ZkClient client;
		private static final String ROOT_PATH = "/rpc/services/v_%s/";

		private Register() {
			init();
		}

		/**
		 * 初始化方法
		 */
		private void init() {
			client = new ZkClient(config.getZkServers(), config.getSessionTimeout(), config.getConnetionTimeout());
		}

		/**
		 * 添加服务节点
		 * 
		 * @param service
		 * @return
		 */
		public boolean addNode(ServiceInfo service) {
			String serviceName = service.getServiceName();
			if (StringUtils.isBlank(serviceName)) {
				throw new IllegalArgumentException("无效的服务名");
			}
			String host = service.getHost();
			if (StringUtils.isBlank(host)) {
				throw new IllegalArgumentException("无效的服务主机");
			}
			byte[] datas = JsonUtils.toJson(service).getBytes();
			// 创建以服务命名的服务节点(临时节点)
			String namePath = getServiceNameNode(service);
			if (!client.exists(namePath)) {
				client.createPersistent(namePath, true);
			}
			String serviceNodePath = namePath.concat("/").concat(host);
			client.createEphemeral(serviceNodePath, datas);
			// 添加监听
			addWatcher(serviceNodePath);
			return true;
		}

		/**
		 * 获取服务节点地址
		 * 
		 * @param service
		 * @return
		 */
		private String getServiceNameNode(ServiceInfo service) {
			return String.format(ROOT_PATH.concat(service.getServiceName()), service.getVersion());
		}

		/**
		 * 添加监听
		 * 
		 * @param serviceNodePath
		 */
		private void addWatcher(String serviceNodePath) {
			Watcher watcher = new Watcher();
			client.subscribeChildChanges(serviceNodePath, watcher);
			client.subscribeDataChanges(serviceNodePath, watcher);
			client.subscribeStateChanges(watcher);
		}

		/**
		 * 删除服务节点
		 * 
		 * @param service
		 * @return
		 */
		public boolean removeNode(ServiceInfo service) {
			String serviceName = service.getServiceName();
			if (StringUtils.isBlank(serviceName)) {
				return true;
			}
			String serviceNameNode = getServiceNameNode(service);
			String host = service.getHost();
			if (StringUtils.isBlank(host)) {
				return client.deleteRecursive(serviceNameNode);
			}
			return client.delete(serviceNameNode.concat("/").concat(host));
		}

		/**
		 * 查找服务节点
		 * 
		 * @param service
		 * @return
		 */
		public ServiceInfo findNode(ServiceInfo service) {
			return null;
		}

		/**
		 * 服务节点是否存在
		 * 
		 * @param service
		 * @return
		 */
		public boolean hasNode(ServiceInfo service) {
			String serviceName = service.getServiceName();
			if (StringUtils.isBlank(serviceName)) {
				return false;
			}
			String host = service.getHost();
			if (StringUtils.isBlank(host)) {
				return client.exists(ROOT_PATH.concat(serviceName));
			}
			return client.exists(ROOT_PATH.concat(serviceName).concat("/").concat(host));
		}

		/**
		 * 创建实例
		 * 
		 * @return
		 */
		public static Register newInstance() {
			if (register == null) {
				synchronized (ServiceRegister.class) {
					if (register == null) {
						register = new Register();
					}
				}
			}
			return register;
		}

	}

	/**
	 * zk监听
	 * 
	 * @author sea
	 *
	 */
	private static class Watcher implements IZkChildListener, IZkStateListener, IZkDataListener {

		@Override
		public void handleDataChange(String dataPath, byte[] data) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void handleDataDeleted(String dataPath) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void handleStateChanged(KeeperState state) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void handleNewSession() throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 注册服务
	 * 
	 * @param service
	 * @return
	 */
	public boolean register(ServiceInfo service) {
		return REG_INST.addNode(service);
	}

	/**
	 * 根据服务名称注销服务
	 * 
	 * @param serviceName
	 * @return
	 */
	public boolean unregister(String serviceName) {
		return unregister(new ServiceInfo(serviceName));
	}

	/**
	 * 根据服务名称和地址注销服务
	 * 
	 * @param serviceName
	 * @param host
	 * @return
	 */
	public boolean unregister(String serviceName, String host) {
		return unregister(new ServiceInfo(serviceName, host));
	}

	/**
	 * 注销服务
	 * 
	 * @param service
	 * @return
	 */
	public boolean unregister(ServiceInfo service) {
		return REG_INST.removeNode(service);
	}

	/**
	 * 根据服务名称查询RPC服务
	 * 
	 * @param serviceName
	 * @return
	 */
	public ServiceInfo findService(String serviceName) {
		return findService(new ServiceInfo(serviceName));
	}

	/**
	 * 根据服务名称和地址查询PRC服务
	 * 
	 * @param serviceName
	 * @param host
	 * @return
	 */
	public ServiceInfo findService(String serviceName, String host) {
		return findService(new ServiceInfo(serviceName, host));
	}

	/**
	 * 查找RPC服务
	 * 
	 * @param service
	 * @return
	 */
	public ServiceInfo findService(ServiceInfo service) {
		return REG_INST.findNode(service);
	}

	/**
	 * 根据服务名称查询RPC服务是否存在
	 * 
	 * @param serviceName
	 * @return
	 */
	public boolean hasService(String serviceName) {
		return hasService(new ServiceInfo(serviceName));
	}

	/**
	 * 根据服务名称和地址查询PRC服务是否存在
	 * 
	 * @param serviceName
	 * @param host
	 * @return
	 */
	public boolean hasService(String serviceName, String host) {
		return hasService(new ServiceInfo(serviceName, host));
	}

	/**
	 * 查询RPC服务是否存在
	 * 
	 * @param service
	 * @return
	 */
	public boolean hasService(ServiceInfo service) {
		return REG_INST.hasNode(service);
	}
}

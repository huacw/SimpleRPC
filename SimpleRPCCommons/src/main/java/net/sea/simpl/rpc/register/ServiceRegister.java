package net.sea.simpl.rpc.register;

import org.I0Itec.zkclient.ZkClient;

import net.sea.simpl.rpc.server.RegisterCenterConfig;
import net.sea.simpl.rpc.server.ServiceInfo;
import net.sea.simpl.rpc.utils.JsonUtils;

/**
 * 服务注册器
 * 
 * @author sea
 *
 */
public class ServiceRegister {
	private static RegisterCenterConfig config;

	/**
	 * 注册器
	 * 
	 * @author sea
	 *
	 */
	private static class Register {
		private volatile static Register register = null;
		private ZkClient client;
		private static final String ROOT_PATH = "/rpc";

		private Register() {
			init();
		}

		/**
		 * 初始化方法
		 */
		private void init() {
			client = new ZkClient(config.getZkServers(), config.getTimeout());
		}

		/**
		 * 添加服务节点
		 * 
		 * @param service
		 * @return
		 */
		public boolean addNode(ServiceInfo service) {
			String serviceName = service.getServiceName();
			int port = service.getPort();
			String serviceJson = JsonUtils.toJson(service);
			// 创建以服务命名的服务节点
			String namePath = ROOT_PATH.concat("/services");
			if (!client.exists(namePath)) {
				client.createPersistent(ROOT_PATH, true);
			}
			client.createEphemeral(namePath.concat("/").concat(serviceName), serviceJson);
			// 创建以端口命名的服务节点
			String portPath = ROOT_PATH.concat("/ports");
			if (!client.exists(portPath)) {
				client.createPersistent(ROOT_PATH, true);
			}
			client.createEphemeral(portPath.concat("/").concat(serviceName).concat(":").concat(String.valueOf(port)),
					serviceJson);
			return true;
		}

		public boolean removeNode(ServiceInfo service) {
			return false;
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
	 * 注册服务
	 * 
	 * @param service
	 * @return
	 */
	public boolean register(ServiceInfo service) {
		return Register.newInstance().addNode(service);
	}

	/**
	 * 根据端口号注销服务
	 * 
	 * @param service
	 * @return
	 */
	public boolean unregister(int port) {
		return unregister(new ServiceInfo(port));
	}

	/**
	 * 根据服务名称注销服务
	 * 
	 * @param service
	 * @return
	 */
	public boolean unregister(String serviceName) {
		return unregister(new ServiceInfo(serviceName));
	}

	/**
	 * 根据服务名称注销服务
	 * 
	 * @param service
	 * @return
	 */
	private boolean unregister(ServiceInfo service) {
		return Register.newInstance().removeNode(service);
	}

	/**
	 * 根据端口号查询RPC服务
	 * 
	 * @param port
	 * @return
	 */
	public ServiceInfo findService(int port) {
		return null;
	}

	/**
	 * 根据服务名称查询RPC服务
	 * 
	 * @param serviceName
	 * @return
	 */
	public ServiceInfo findService(String serviceName) {
		return null;
	}
}

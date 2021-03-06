package com.mycompany.cloud.controller.test.zookeeper.leader;

import com.google.common.collect.Lists;
import com.mycompany.cloud.controller.test.zookeeper.utils.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class LeaderLatchExample {
	public static void main(String[] args) {

		List<CuratorFramework> clients = Lists.newArrayList();
		List<LeaderLatchClient> examples = Lists.newArrayList();
		try {
			for (int i = 0; i < 10; i++) {
				CuratorFramework client = ClientFactory.newClient();
				LeaderLatchClient example = new LeaderLatchClient(client, "Client #" + i);
				clients.add(client);
				examples.add(example);

				client.start();
				example.start();
			}

			Thread.sleep(10000);

			System.out.println("----------关闭前5个客户端，再观察选举leader的结果-----------");
			for (int i = 0; i < 8; i++) {
				clients.get(i).close();
			}

			// 让main程序一直监听控制台输入，不退出
			new BufferedReader(new InputStreamReader(System.in)).readLine();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			for (LeaderLatchClient exampleClient : examples) {
				CloseableUtils.closeQuietly(exampleClient);
			}
			for (CuratorFramework client : clients) {
				CloseableUtils.closeQuietly(client);
			}
		}
	}
}

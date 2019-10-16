package com.sysu.provider;

import com.sysu.api.HelloService;
import com.sysu.registry.IRegistry;
import com.sysu.registry.RegistryFactory;
import com.sysu.registry.zookeeper.ZookeeperRegistry;
import com.sysu.rpc.netty.NettyProviderServer;
import org.junit.Test;

import java.io.IOException;

public class ProviderTest {
    @Test
    public void test() throws IOException {
        HelloService helloService = new HelloServiceImpl();
        IRegistry registrar = new ZookeeperRegistry();

        NettyProviderServer server = new NettyProviderServer();
        server.init("127.0.0.1:20880");
        server.bind(HelloService.class.getName(), helloService);
        server.publisher();
    }

    @Test
    public void spiTest() throws IOException {
        IRegistry registrar = RegistryFactory.getRegistry();
        registrar.register("127.0.0.1:62880", HelloService.class.getName());
        System.out.println("end");
    }
}

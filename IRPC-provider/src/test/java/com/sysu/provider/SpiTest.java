package com.sysu.provider;


import com.sysu.api.HelloService;
import com.sysu.cluster.ILoadBalance;
import com.sysu.cluster.LoadBalanceFactory;
import com.sysu.common.loader.ExtensionLoader;
import com.sysu.registry.IRegistry;
import com.sysu.registry.RegistryFactory;
import com.sysu.rpc.netty.NettyConsumerProxy;
import com.sysu.rpc.netty.NettyProviderServer;
import org.junit.Test;

import java.io.IOException;

public class SpiTest {
    @Test
    public void providerTest() throws IOException {
        NettyProviderServer server = new NettyProviderServer();
        server.init("127.0.0.1:20880");
        server.bind(HelloService.class.getName(), new HelloServiceImpl());
        server.publisher();
        System.in.read();
    }

    @Test
    public void consumerTest(){
        IRegistry registrar = RegistryFactory.getRegistry();
        HelloService helloService = NettyConsumerProxy.create(registrar, HelloService.class);
        String result = helloService.sayHello("lxx");
        System.out.println(result);
    }

    @Test
    public void test() {
        ILoadBalance loadbalance = LoadBalanceFactory.getLoadBalance();
        IRegistry registrar = RegistryFactory.getRegistry();
        System.out.println();
    }

    @Test
    public void spiTest(){
        ILoadBalance round = ExtensionLoader.getExtensionLoader(ILoadBalance.class)
                .getExtension("round");
        ILoadBalance random = ExtensionLoader.getExtensionLoader(ILoadBalance.class)
                .getExtension("random");
        System.out.println(round.getClass().getName());
        System.out.println(random.getClass().getName());
    }

}

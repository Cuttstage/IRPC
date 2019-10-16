package com.sysu.rpc.netty;

import com.sysu.registry.IRegistry;
import com.sysu.registry.RegistryFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.HashMap;
import java.util.Map;

public class NettyProviderServer {
    private IRegistry registry;
    private String selfAddress;
    private Map<String, Object> providers = new HashMap<String, Object>();

    public void init(String selfAddress) {
        this.registry = RegistryFactory.getRegistry();
        this.selfAddress = selfAddress;
    }

    public void bind(String interfaceName, Object provider) {
        providers.put(interfaceName, provider);
    }

    public void publisher() {

        // 服务注册，发布
        for (String service : providers.keySet()) {
            registry.register(selfAddress, service);
        }

        // 启动服务
        try {
//            EventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("qos-boss", true));
//            EventLoopGroup workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("qos-worker", true));
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(NettyProviderServer.class.getClassLoader())));
                            pipeline.addLast(new NettyProviderHandler(providers));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
            String[] addrs = selfAddress.split(":");
            String ip = addrs[0];
            Integer port = Integer.parseInt(addrs[1]);
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

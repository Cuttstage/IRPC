package com.sysu.rpc.netty;

import com.sysu.registry.IRegistry;
import com.sysu.rpc.RequestDTO;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class NettyConsumerProxy {

    public static <T> T create(final IRegistry registry, final Class<T> interfaceClass) {
        Object object = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        RequestDTO requestDTO = new RequestDTO();
                        requestDTO.setClassName(method.getDeclaringClass().getName());
                        requestDTO.setMethodName(method.getName());
                        requestDTO.setParams(args);
                        requestDTO.setTypes(method.getParameterTypes());

                        String service = interfaceClass.getName();
                        String serviceAddress = registry.discover(service);
                        String[] addr = serviceAddress.split(":");
                        String host = addr[0];
                        int port = Integer.parseInt(addr[1]);

                        final NettyConsumerHandler consumerHandler = new NettyConsumerHandler();
                        EventLoopGroup group = new NioEventLoopGroup();
                        try {
                            Bootstrap bootstrap = new Bootstrap();
                            bootstrap.group(group)
                                    .channel(NioSocketChannel.class)
                                    .option(ChannelOption.TCP_NODELAY, true)
                                    .handler(new ChannelInitializer<Channel>() {
                                        @Override
                                        protected void initChannel(Channel channel) throws Exception {
                                            ChannelPipeline pipeline = channel.pipeline();
                                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(NettyConsumerProxy.class.getClassLoader())));
                                            pipeline.addLast(new ObjectEncoder());
                                            pipeline.addLast(consumerHandler);
                                        }
                                    });
                            //对应的netty连接进行维护
                            ChannelFuture future = bootstrap.connect(host, port).sync();

                            Channel channel = future.channel();
                            channel.writeAndFlush(requestDTO);
                            channel.closeFuture().sync();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            group.shutdownGracefully();
                        }
                        return consumerHandler.getResponse();
                    }
                });
        return (T) object;
    }


}

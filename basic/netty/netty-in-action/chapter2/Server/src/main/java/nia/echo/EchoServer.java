package nia.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author czhan
 * @version 0.0.1
 * @description TODO
 * @date 2019年06月06日 下午4:33
 * @since 0.0.1
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
//        System.out.println("args.length: "+args.length);
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();

        EventLoopGroup group = new NioEventLoopGroup(); //创建EventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap(); //创建ServerBootstrap
            b.group(group)
                    .channel(NioServerSocketChannel.class) //指定所使用的NIO传输Channel
                    .localAddress(new InetSocketAddress(port)) //使用指定的端口设置套接字地址
                    //添加一个EchoServerHandler到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler); //EchoServerHandler被设置为Shareable，所以我们可以总是使用同样的实例
                        }
                    });
            ChannelFuture f = b.bind().sync(); //异步地绑定服务器；调用sync()方法阻塞等待直到绑定完成
            f.channel().closeFuture().sync(); //获取Channel的CloseFuture,并且阻塞当前线程直到它完成
        } finally {
            group.shutdownGracefully().sync(); //关闭EventLoopGroup，释放所有的资源
        }
    }
}

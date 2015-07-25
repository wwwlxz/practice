package com.lxz.netty.serializable.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

public class SubReqClient {
	public void connect(int port, String host) throws Exception{
		//配置客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>(){
							@Override
							protected void initChannel(SocketChannel ch)
									throws Exception {
								ch.pipeline().addLast(new ObjectDecoder(1024, 
										ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
								ch.pipeline().addLast(new ObjectEncoder());
								ch.pipeline().addLast(new SubReqClientHandler());
							}
						});
			//发起异步连接操作
			ChannelFuture f = b.connect(host, port).sync();
			//等待客户端链路关闭
			f.channel().closeFuture().sync();
		}finally{
			//优雅的退出，释放NIO线程组
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception{
		int port = 1234;
		if(args != null && args.length > 0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(NumberFormatException e){
				
			}
		}
		new SubReqClient().connect(port, "127.0.0.1");
	}
}

class SubReqClientHandler extends ChannelHandlerAdapter{
	private static final Logger logger = Logger.getLogger(SubReqClientHandler.class.getName());
	
	public SubReqClientHandler(){//初始化
	}
	
	//当客户端和服务端TCP链路建立成功之后，Netty的NIO线程会调用这个方法
	public void channelActive(ChannelHandlerContext ctx){
		for(int i = 0; i < 10; i++){
			ctx.write(subReq(i));//发送查询时间的指令给服务端
		}
		ctx.flush();
	}
	
	private SubscribeReq subReq(int i){
		SubscribeReq req = new SubscribeReq();
		req.setAddress("武汉市");
		req.setPhoneNumber("18xxxxxxxxx");
		req.setProductName("Netty book");
		req.setSubReqID(i);
		req.setUserName("Lxz");
		return req;
	}
	
	//当服务端返回应答消息时，会调用这个方法
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException{
		System.out.println("Receive server response : [" + msg + "]");
	}
	
	public void channelReadComplete(ChannelHandlerContext ctx){
		ctx.flush();
	}
	
	//当发生异常时，打印异常日志，释放客户端资源
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		//释放资源
		//logger.warn("Unexcepted exception form downstream : " + cause.getMessage());
		cause.printStackTrace();
		ctx.close();
	}
}

package com.lxz.netty.test01;

import java.io.UnsupportedEncodingException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.apache.log4j.Logger;

public class TimeClient {
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
								ch.pipeline().addLast(new TimeClientHandler());
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
		new TimeClient().connect(port, "127.0.0.1");
	}
}

class TimeClientHandler extends ChannelHandlerAdapter{
	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
	private final ByteBuf firstMessage;
	
	public TimeClientHandler(){//初始化
		byte[] req = "QUERY TIME ORDER".getBytes();
		firstMessage = Unpooled.buffer(req.length);
		firstMessage.writeBytes(req);
	}
	
	//当客户端和服务端TCP链路建立成功之后，Netty的NIO线程会调用这个方法
	public void channelActive(ChannelHandlerContext ctx){
		ctx.writeAndFlush(firstMessage);//发送查询时间的指令给服务端
	}
	
	//当服务端返回应答消息时，会调用这个方法
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException{
		ByteBuf buf = (ByteBuf)msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("Now is: " + body);
	}
	
	//当发生异常时，打印异常日志，释放客户端资源
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		//释放资源
		logger.warn("Unexcepted exception form downstream : " + cause.getMessage());
		ctx.close();
	}
}

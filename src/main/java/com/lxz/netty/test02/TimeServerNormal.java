package com.lxz.netty.test02;

import io.netty.bootstrap.ServerBootstrap;
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
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.io.UnsupportedEncodingException;

public class TimeServerNormal {
	public void bind(int port) throws Exception{
		//配置服务端的NIO线程组，专门用于处理网络事件
		EventLoopGroup bossGroup = new NioEventLoopGroup();//用于服务端接收客户端的连接
		EventLoopGroup workerGroup = new NioEventLoopGroup();//用于进行SocketChannel的网络读写
		try{
			ServerBootstrap b = new ServerBootstrap();//Netty用于启动NIO服务端的辅助启动类，目的是降低服务端开发的复杂度
			b.group(bossGroup, workerGroup)//将两个NIO线程组当做参数传递到ServerBootstrap中
					.channel(NioServerSocketChannel.class)//功能对应于JDK NIO中的ServerSocketChannel，用于监听客户端的连接
					.option(ChannelOption.SO_BACKLOG, 1024)//配置NioServerSocketChannel的TCP参数
					.childHandler(new ChildChannelHandler());
			//绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();//异步操作的通知回调
			//阻塞。等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		}finally{
			//优雅退出，释放线程资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	//主要的处理部分
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
			arg0.pipeline().addLast(new StringDecoder());
			arg0.pipeline().addLast(new TimeServerHandler1());
		}
	}
	
	public static void main(String[] args) throws Exception{
		int port = 1234;
		if(args != null && args.length >0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(NumberFormatException e){
				
			}
		}
		new TimeServerNormal().bind(port);
	}
}	

//用于对网络事件进行读写操作，主要的业务逻辑
class TimeServerHandler1 extends ChannelHandlerAdapter{
	private int counter;
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException{
		String body = (String)msg;
		System.out.println("The time server receive order : " + body + " ; the counter is : " + ++counter);
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? 
				new java.util.Date(System.currentTimeMillis()).toString() : 
					"BAD ORDER";
		currentTime = currentTime + System.getProperty("line.separator");
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(resp);//将消息发送队列的消息写入到SocketChannel中发送给对方
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		ctx.close();//当发生异常时，关闭ChannelHandlerContext，释放ChannelHandlerContext相关的句柄资源
	}
}

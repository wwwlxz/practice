package com.lxz.netty.test03;

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
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {
	public void connect(int port, String host) throws InterruptedException{
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
						ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new EchoClientHandler());
					}
				});
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException{
		int port = 1234;
		if(args != null && args.length >0){
			try{
				port = Integer.valueOf(args[0]);
			}catch(NumberFormatException e){
				
			}
		}
		new EchoClient().connect(port, "127.0.0.1");
	}
}

class EchoClientHandler extends ChannelHandlerAdapter{
	private int counter;
	
	static final String ECHO_REQ = "Hi, Lxz, Welcome to Netty.$_";
	
	public EchoClientHandler(){
		
	}
	
	public void channelActive(ChannelHandlerContext ctx){
		for(int i = 0; i < 10; i++){
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
		}
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		System.out.println("This is " + ++counter + " times receive server : [" + msg + "]");
	}
	
	public void channelReadComplete(ChannelHandlerContext ctx){
		ctx.flush();
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		ctx.close();
	}
}
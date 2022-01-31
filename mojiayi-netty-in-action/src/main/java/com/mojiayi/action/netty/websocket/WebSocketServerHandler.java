package com.mojiayi.action.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        if (!httpRequest.decoderResult().isSuccess() || !"websocket".equals(httpRequest.headers().get("Upgrade"))) {
            sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket", null, false);
        handshaker = wsFactory.newHandshaker(httpRequest);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), httpRequest);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
        if (webSocketFrame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) webSocketFrame.retain());
            return;
        }
        if (webSocketFrame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
            return;
        }
        if (!(webSocketFrame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", webSocketFrame.getClass().getName()));
        }
        String request = ((TextWebSocketFrame) webSocketFrame).text();
        System.out.println(ctx.channel() + " received " + request);
        ctx.channel().write(new TextWebSocketFrame(request + ", welcome to netty websocket,now is " + new Date()));
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest httpRequest, FullHttpResponse httpResponse) {
        if (httpResponse.status().code() != 200) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(httpResponse.status().toString(), StandardCharsets.UTF_8);
            httpResponse.content().writeBytes(byteBuf);
            byteBuf.release();
            setContentLength(httpResponse, httpResponse.content().readableBytes());
        }
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(httpResponse);
        if (!isKeepAlive(httpRequest) || httpResponse.status().code() != 200) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}


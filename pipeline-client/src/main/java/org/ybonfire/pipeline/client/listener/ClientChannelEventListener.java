package org.ybonfire.pipeline.client.listener;

import org.ybonfire.pipeline.client.manager.NettyChannelManager;
import org.ybonfire.pipeline.common.listener.INettyChannelEventListener;

import io.netty.channel.Channel;

/**
 * Netty客户端连接相关事件监听器
 *
 * @author Bo.Yuan5
 * @date 2022-05-24 17:30
 */
public class ClientChannelEventListener implements INettyChannelEventListener {
    private final NettyChannelManager nettyChannelManager;

    public ClientChannelEventListener(final NettyChannelManager nettyChannelManager) {
        this.nettyChannelManager = nettyChannelManager;
    }

    /**
     * @description: 连接开启
     * @param:
     * @return:
     * @date: 2022/05/24 17:37:53
     */
    @Override
    public void onOpen(final String address, final Channel channel) {
        nettyChannelManager.putChannel(address, channel);
    }

    /**
     * @description: 连接关闭
     * @param:
     * @return:
     * @date: 2022/05/24 17:37:58
     */
    @Override
    public void onClose(final String address, final Channel channel) {
        nettyChannelManager.closeChannel(address);
    }
}

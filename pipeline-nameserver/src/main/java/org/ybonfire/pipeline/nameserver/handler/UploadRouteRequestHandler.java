package org.ybonfire.pipeline.nameserver.handler;

import org.ybonfire.pipeline.common.command.RemotingCommand;
import org.ybonfire.pipeline.common.constant.ResponseEnum;
import org.ybonfire.pipeline.common.protocol.request.RouteUploadRequest;
import org.ybonfire.pipeline.common.protocol.response.DefaultResponse;
import org.ybonfire.pipeline.nameserver.route.RouteManageService;
import org.ybonfire.pipeline.server.handler.AbstractNettyRemotingRequestHandler;

import io.netty.channel.ChannelHandlerContext;

/**
 * UploadRoute请求处理器
 *
 * @author Bo.Yuan5
 * @date 2022-07-01 17:35
 */
public final class UploadRouteRequestHandler extends AbstractNettyRemotingRequestHandler {
    private final RouteManageService routeManageService;

    public UploadRouteRequestHandler(final RouteManageService routeManageService) {
        this.routeManageService = routeManageService;
    }

    /**
     * @description: 参数校验
     * @param:
     * @return:
     * @date: 2022/07/09 15:10:48
     */
    @Override
    protected void check(RemotingCommand request, ChannelHandlerContext context) {

    }

    /**
     * @description: 业务处理
     * @param:
     * @return:
     * @date: 2022/07/01 18:22:39
     */
    @Override
    protected RemotingCommand fire(final RemotingCommand request, final ChannelHandlerContext context) {
        final RouteUploadRequest data = (RouteUploadRequest)request.getBody();
        routeManageService.uploadByBroker(data);
        return RemotingCommand.createResponseCommand(request.getCode(), request.getCommandId(),
            DefaultResponse.create("success"));
    }

    /**
     * @description: 异常处理
     * @param:
     * @return:
     * @date: 2022/07/01 18:22:39
     */
    @Override
    protected RemotingCommand onException(final RemotingCommand request, final ChannelHandlerContext context,
        Exception ex) {
        return RemotingCommand.createResponseCommand(ResponseEnum.INTERNAL_SYSTEM_ERROR.getCode(),
            request.getCommandId(), DefaultResponse.create("failed"));
    }

    /**
     * @description: 处理结束流程
     * @param:
     * @return:
     * @date: 2022/07/09 15:20:21
     */
    @Override
    protected void onComplete(RemotingCommand request, ChannelHandlerContext context) {

    }
}

package org.ybonfire.pipeline.client.model;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ybonfire.pipeline.common.callback.IRequestCallback;
import org.ybonfire.pipeline.common.command.RemotingCommand;
import org.ybonfire.pipeline.common.exception.ExceptionTypeEnum;
import org.ybonfire.pipeline.common.util.ExceptionUtil;

import io.netty.channel.Channel;

/**
 * 远程调用数据
 *
 * @author Bo.Yuan5
 * @date 2022-05-19 12:13
 */
public class RemoteRequestFuture {
    private final String address;
    private final Channel channel;
    private final long startTimestamp = System.currentTimeMillis();
    private final RemotingCommand request;
    private final IRequestCallback callback;
    private final long timeoutMillis;
    private final CompletableFuture<RemotingCommand> responseFuture = new CompletableFuture<>();
    private volatile boolean isRequestSuccess = false;
    private volatile boolean isCompleted = false;
    private volatile Throwable cause;

    public RemoteRequestFuture(final String address, final Channel channel, final RemotingCommand request,
        IRequestCallback callback, final long timeoutMillis) {
        this.address = address;
        this.channel = channel;
        this.request = request;
        this.callback = callback;
        this.timeoutMillis = timeoutMillis;
    }

    /**
     * @description: 等待远程调用请求执行结果
     * @param:
     * @return:
     * @date: 2022/06/02 09:56:41
     */
    public RemotingCommand get(final long timeoutMillis) throws InterruptedException {
        try {
            return this.responseFuture.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            throw ExceptionUtil.exception(ExceptionTypeEnum.UNKNOWN, e);
        } catch (TimeoutException e) {
            throw ExceptionUtil.exception(ExceptionTypeEnum.REQUEST_TIMEOUT, e);
        }
    }

    /**
     * @description: 完成并唤醒远程调用请求等待
     * @param:
     * @return:
     * @date: 2022/06/02 09:56:54
     */
    public void complete(final RemotingCommand response) {
        this.responseFuture.complete(response);
        this.isCompleted = true;
    }

    public String getAddress() {
        return address;
    }

    public Channel getChannel() {
        return channel;
    }

    public RemotingCommand getRequest() {
        return request;
    }

    public CompletableFuture<RemotingCommand> getResponseFuture() {
        return responseFuture;
    }

    public boolean isRequestSuccess() {
        return isRequestSuccess;
    }

    public void setRequestSuccess(boolean requestSuccess) {
        isRequestSuccess = requestSuccess;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(final Throwable cause) {
        this.cause = cause;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public IRequestCallback getCallback() {
        return callback;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > startTimestamp + timeoutMillis;
    }
}

package org.ybonfire.pipeline.nameserver.thread;

import org.ybonfire.pipeline.common.protocol.IRemotingRequest;
import org.ybonfire.pipeline.common.protocol.request.nameserver.BrokerHeartbeatRequest;
import org.ybonfire.pipeline.common.thread.task.AbstractThreadTask;
import org.ybonfire.pipeline.nameserver.client.INameServerClient;
import org.ybonfire.pipeline.nameserver.model.PeerNode;

import java.util.concurrent.CountDownLatch;

/**
 * RouteUploadRequestPublish异步任务
 *
 * @author Bo.Yuan5
 * @date 2022-08-12 22:24
 */
public final class RouteUploadRequestPublishThreadTask extends AbstractThreadTask {
    private final IRemotingRequest<BrokerHeartbeatRequest> request;
    private final PeerNode peer;
    private final CountDownLatch latch;
    private final INameServerClient client;

    RouteUploadRequestPublishThreadTask(final IRemotingRequest<BrokerHeartbeatRequest> request, final PeerNode peer,
        final CountDownLatch latch, final INameServerClient client) {
        this.request = request;
        this.peer = peer;
        this.latch = latch;
        this.client = client;
    }

    /**
     * @description: 执行异步任务
     * @param:
     * @return:
     * @date: 2022/08/12 22:25:57
     */
    @Override
    protected void execute() {
        try {
            doPublish();
        } finally {
            latch.countDown();
        }
    }

    /**
     * @description: 广播UploadRouteRequest
     * @param:
     * @return:
     * @date: 2022/08/12 22:34:17
     */
    private void doPublish() {
        client.publish(request, peer.getAddress());
    }
}

package org.ybonfire.pipeline.broker.server;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.ybonfire.pipeline.broker.config.BrokerConfig;
import org.ybonfire.pipeline.broker.processor.ProduceMessageRequestProcessor;
import org.ybonfire.pipeline.broker.model.RoleEnum;
import org.ybonfire.pipeline.broker.register.IBrokerRegisterService;
import org.ybonfire.pipeline.broker.register.impl.BrokerRegisterServiceImpl;
import org.ybonfire.pipeline.broker.role.RoleManager;
import org.ybonfire.pipeline.broker.util.ThreadPoolUtil;
import org.ybonfire.pipeline.common.constant.RequestEnum;
import org.ybonfire.pipeline.server.NettyRemotingServer;

/**
 * Broker服务
 *
 * @author Bo.Yuan5
 * @date 2022-08-24 21:36
 */
public final class Broker extends NettyRemotingServer {
    private final AtomicBoolean started = new AtomicBoolean();
    private final IBrokerRegisterService brokerRegisterService = new BrokerRegisterServiceImpl();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final List<String> nameServerAddressList;

    public Broker(final BrokerConfig config, final List<String> nameServerAddressList) {
        super(config);
        RoleManager.getInstance().set(RoleEnum.of(config.getRole()));
        this.nameServerAddressList = nameServerAddressList;
    }

    @Override
    public void start() {
        if (started.compareAndSet(false, true)) {
            super.start();
            brokerRegisterService.start();

            // 注册定时任务,定时向NameServer上报信息
            if (RoleManager.getInstance().get() == RoleEnum.LEADER) {
                scheduledExecutorService.scheduleAtFixedRate(this::registerToNameServer, 5 * 1000L, 10 * 1000L,
                    TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public void shutdown() {
        if (started.compareAndSet(true, false)) {
            super.shutdown();
            brokerRegisterService.shutdown();

            // 注册定时任务,定时向NameServer上报信息
            if (RoleManager.getInstance().get() == RoleEnum.LEADER) {
                scheduledExecutorService.shutdown();
            }
        }
    }

    @Override
    protected void registerRequestProcessor() {
        // ProduceMessageRequestProcessor
        registerProduceMessageRequestProcessor();
        // ConsumeMessageRequestProcessor
        registerConsumeMessageRequestProcessor();
    }

    /**
     * 注册Broker至NameServer
     */
    private void registerToNameServer() {
        brokerRegisterService.registerToNameServer(this.nameServerAddressList);
    }

    /**
     * 注册ProduceMessage请求处理器
     */
    private void registerProduceMessageRequestProcessor() {
        final ExecutorService produceMessageRequestProcessorExecutor =
            ThreadPoolUtil.getProduceMessageProcessorExecutorService();
        registerRequestProcessor(RequestEnum.PRODUCE_MESSAGE.getCode(), ProduceMessageRequestProcessor.getInstance(),
            produceMessageRequestProcessorExecutor);
    }

    /**
     * 注册ConsumeMessage请求处理器
     */
    private void registerConsumeMessageRequestProcessor() {
        final ExecutorService consumeMessageRequestProcessorExecutor =
            ThreadPoolUtil.getConsumeMessageProcessorExecutorService();
    }
}

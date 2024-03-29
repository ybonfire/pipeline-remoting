package org.ybonfire.pipeline.common.protocol;

/**
 * 远程调用请求接口
 *
 * @author Bo.Yuan5
 * @date 2022-07-19 16:36
 */
public interface IRemotingRequest<T extends IRemotingRequestBody> extends IRemotingRequestResponse {

    /**
     * @description: 获取远程调用Id
     * @param:
     * @return:
     * @date: 2022/07/19 16:37:50
     */
    String getId();

    /**
     * @description: 获取远程调用类型码
     * @param:
     * @return:
     * @date: 2022/07/19 16:37:53
     */
    Integer getCode();

    /**
     * @description: 获取远程调用体
     * @param:
     * @return:
     * @date: 2022/07/19 19:19:12
     */
    T getBody();

    /**
     * @description: 获取请求挂起时间
     * @param:
     * @return:
     * @date: 2022/08/19 15:25:51
     */
    long getSuspendMillis();
}

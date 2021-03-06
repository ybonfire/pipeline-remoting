package org.ybonfire.pipeline.common.exception;

/**
 * 异常码常量类
 *
 * @author Bo.Yuan5
 * @date 2022-05-19 10:19
 */
public enum ExceptionTypeEnum {
    /**
     * 连接超时
     */
    CONNECT_TIMEOUT(1, "连接超时"),
    /**
     * 连接失败
     */
    CONNECT_FAILED(2, "连接失败"),
    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(3, "请求超时"),
    /**
     * 序列化失败
     */
    SERIALIZE_FAILED(4, "序列化失败"),
    /**
     * 非法参数
     */
    ILLEGAL_ARGUMENT(5, "非法参数"),
    /**
     * 未知路由
     */
    UNKNOWN_ROUTE(6, "未知路由"),
    /**
     * 未知PartitionLeader
     */
    UNKNOWN_PARTITION_LEADER(7, "未知PartitionLeader"),
    /**
     * 远程调用异常
     */
    REMOTING_INVOKE_FAILED(8, "远程调用异常"),
    /**
     * 超时异常
     */
    TIMEOUT_EXCEPTION(9, "超时异常"),
    /**
     * 未知异常
     */
    UNKNOWN(-1, "未知异常");

    private int code;
    private String description;

    ExceptionTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

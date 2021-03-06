package org.ybonfire.pipeline.producer.model;

/**
 * 这里添加类的注释【强制】
 *
 * @author Bo.Yuan5
 * @date 2022-07-15 10:13
 */
public enum ProduceTypeEnum {
    /**
     * 同步
     */
    SYNC(2, "同步投递"),
    /**
     * 异步
     */
    ASYNC(1, "异步投递"),
    /**
     * 单向
     */
    ONEWAY(0, "单向投递");

    ProduceTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    private int code;
    private String description;
}

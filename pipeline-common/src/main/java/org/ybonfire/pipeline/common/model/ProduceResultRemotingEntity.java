package org.ybonfire.pipeline.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息生产结果响应
 *
 * @author Bo.Yuan5
 * @date 2022-06-30 16:30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProduceResultRemotingEntity {
    private Message message;
    private String topic;
    private Integer partitionId;
    private Long offset;
    private Boolean isSuccess;
}

package org.ybonfire.pipeline.producer.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.ybonfire.pipeline.common.converter.IConverter;
import org.ybonfire.pipeline.common.model.Node;
import org.ybonfire.pipeline.common.model.PartitionInfo;
import org.ybonfire.pipeline.common.model.PartitionInfoRemotingEntity;

/**
 * PartitionInfo参数转换器
 *
 * @author Bo.Yuan5
 * @date 2022-06-30 09:44
 */
public final class PartitionConverter implements IConverter<PartitionInfoRemotingEntity, PartitionInfo> {
    private final NodeConverter nodeConverter;

    public PartitionConverter(final NodeConverter nodeConverter) {
        this.nodeConverter = nodeConverter;
    }

    /**
     * @description: 参数转换
     * @param:
     * @return:
     * @date: 2022/07/28 20:31:40
     */
    @Override
    public PartitionInfo convert(final PartitionInfoRemotingEntity src) {
        if (src == null) {
            return null;
        }

        final int partitionId = src.getPartitionId() == null ? -1 : src.getPartitionId();
        final List<Node> nodes = src.getNodes() == null ? Collections.emptyList()
            : src.getNodes().stream().map(nodeConverter::convert).collect(Collectors.toList());

        return PartitionInfo.builder().partitionId(partitionId).nodes(nodes).build();
    }
}
package org.ybonfire.pipeline.broker.store.index;

import org.ybonfire.pipeline.broker.constant.BrokerConstant;
import org.ybonfire.pipeline.broker.exception.FileLoadException;
import org.ybonfire.pipeline.broker.model.Index;
import org.ybonfire.pipeline.broker.model.SelectMappedFileDataResult;
import org.ybonfire.pipeline.broker.store.file.MappedFile;
import org.ybonfire.pipeline.broker.store.message.MessageLog;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 索引文件
 *
 * @author yuanbo
 * @date 2022-10-08 17:46
 */
public final class IndexLog {
    private static final String INDEX_STORE_BASE_PATH = BrokerConstant.BROKER_STORE_BASE_PATH + "index";
    private static final int INDEX_LOG_SIZE = 4 * 1024 * 1024; // 4MB
    /**
     * 4 bytes startOffset + 4 bytes size + 8 bytes timestamp
     */
    private static final int INDEX_UNIT_BYTE_LENGTH = 16;
    private final Lock lock = new ReentrantLock();
    private final String topic;
    private final int partitionId;
    private final MappedFile file;

    private IndexLog(final String topic, final int partitionId) throws IOException {
        this.topic = topic;
        this.partitionId = partitionId;
        this.file = MappedFile.create(buildIndexLogFilename(this.topic, this.partitionId), INDEX_LOG_SIZE);
    }

    private IndexLog(final File file) throws IOException {
        final String[] parseResult = parseTopicPartitionByFilename(file.getName());
        final String topic = parseResult[0];
        final int partitionId = Integer.parseInt(parseResult[1]);

        final MappedFile mappedFile = MappedFile.from(file);

        this.topic = topic;
        this.partitionId = partitionId;
        this.file = mappedFile;
    }

    /**
     * 写入索引
     *
     * @param index 索引
     */
    public void put(final Index index) {
        if (index == null) {
            return;
        }

        lock.lock();
        try {
            final byte[] data = convert(index);
            file.put(data);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 索引刷盘
     *
     * @return boolean
     */
    public boolean flush() {
        lock.lock();
        try {
            return file.flush();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取索引文件从position位置开始的全部数据
     *
     * @param position 位置
     * @return {@link Optional}<{@link SelectMappedFileDataResult}>
     */
    public Optional<SelectMappedFileDataResult> get(final int position) {
        lock.lock();
        try {
            return file.get(position);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取索引文件从position位置开始的size大小的数据
     *
     * @param position 位置
     * @param size 大小
     * @return {@link Optional}<{@link SelectMappedFileDataResult}>
     */
    public Optional<SelectMappedFileDataResult> get(final int position, final int size) {
        lock.lock();
        try {
            return file.get(position, size);
        } finally {
            lock.unlock();
        }
    }

    public String getTopic() {
        return topic;
    }

    public int getPartitionId() {
        return partitionId;
    }

    /**
     * 将索引数据二进制化
     *
     * @param index 索引数据
     * @return {@link byte[]}
     */
    private byte[] convert(final Index index) {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(INDEX_UNIT_BYTE_LENGTH);
        // startOffset
        byteBuffer.putInt(index.getStartOffset());
        // size
        byteBuffer.putInt(index.getSize());
        // timestamp
        byteBuffer.putLong(index.getTimestamp());

        return byteBuffer.array();
    }

    /**
     * 构建索引文件路径
     *
     * @param topic 主题
     * @param partitionId 分区id
     * @return {@link String}
     */
    private String buildIndexLogFilename(final String topic, final int partitionId) {
        return INDEX_STORE_BASE_PATH + File.separator + topic + File.separator + partitionId;
    }

    /**
     * 根据消息文件路径解析Topic、Partition
     *
     * @param filename 文件名
     * @return {@link String}
     */
    private static String[] parseTopicPartitionByFilename(final String filename) {
        String[] result;
        if (filename.startsWith(INDEX_STORE_BASE_PATH)) {
            result = filename.substring((INDEX_STORE_BASE_PATH + File.separator).length()).split(File.separator);
        } else {
            result = filename.split(File.separator);
        }

        if (result.length == 2) {
            return result;
        }

        throw new FileLoadException();
    }

    /**
     * 创建IndexLog
     *
     * @param topic 主题
     * @param partitionId 分区id
     * @return {@link MessageLog}
     * @throws IOException ioexception
     */
    public static IndexLog create(final String topic, final int partitionId) throws IOException {
        return new IndexLog(topic, partitionId);
    }

    /**
     * 加载所有索引文件
     *
     * @return {@link List}<{@link IndexLog}>
     * @throws IOException ioexception
     */
    public static List<IndexLog> reloadAll() throws IOException {
        final File indexLogDir = new File(INDEX_STORE_BASE_PATH);
        if (indexLogDir.isDirectory()) {
            final File[] indexLogFiles = indexLogDir.listFiles();
            if (indexLogFiles != null) {
                final List<IndexLog> indices = new ArrayList<>(indexLogFiles.length);
                for (final File indexLogFile : indexLogFiles) {
                    final IndexLog index = new IndexLog(indexLogFile);
                    // TODO set Positions
                    indices.add(index);
                }

                return indices;
            }
        }

        return Collections.emptyList();
    }
}
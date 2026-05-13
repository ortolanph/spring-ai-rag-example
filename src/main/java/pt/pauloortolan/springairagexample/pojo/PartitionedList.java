package pt.pauloortolan.springairagexample.pojo;

import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
public class PartitionedList<E> {

    private final int batchSize;
    private final List<E> data;

    public PartitionedList(int batchSize, List<E> data) {
        if (batchSize <= 0) throw new IllegalArgumentException("Batch size must be positive and greater than zero");
        this.batchSize = batchSize;
        this.data = data;
    }

    public List<E> head() {
        return data.subList(0, Math.min(data.size(), batchSize));
    }

    public PartitionedList<E> tail() {
        return (batchSize > data.size()) ?
                new PartitionedList<>(batchSize, List.of()) :
                new PartitionedList<>(batchSize, data.subList(batchSize, data.size()));
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public String toString() {
        return "PartitionedList{" +
                "batchSize=" + batchSize +
                ", data=" + data +
                '}';
    }

}
package org.example;

import java.util.Objects;


/**
 * 开放寻址法实现的 Map（非线程安全）
 *
 * @param <K>
 * @param <V>
 */
public class ToyMap<K, V> {

    /**
     * 使用数组保存，数组的下标即为 key 的 hash 值
     */
    private Node<K, V>[] nodes;

    /**
     * 默认初始化容量
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * 最大容量
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 装载因子，达到阈值后触发扩容，避免由于填充元素过多、哈希冲突频繁
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 当前元素个数
     */
    private int size = 0;

    public ToyMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ToyMap(int capacity) {
        if (capacity <= 0) {
            capacity = DEFAULT_INITIAL_CAPACITY;
        }
        nodes = new Node[capacity];
    }

    public void put(K key, V value) {
        if (size >= DEFAULT_LOAD_FACTOR * nodes.length) {
            resize();
        }
        int index = getIndex(key, nodes);
        if (nodes[index] == null) {
            size++;
        }
        nodes[index] = new Node<>(key, value);
    }

    public V get(K key) {
        int idx = getIndex(key, nodes);
        Node<K, V> node = nodes[idx];
        if (node == null) {
            return null;
        }
        return node.value;
    }

    private int getIndex(K key, Node<K, V>[] nodes) {
        // hashCode() 可能返回负数，不能直接取模作为下标，需要先取 abs。
        int idx = key == null? 0: Math.abs(key.hashCode()) % nodes.length;
        while (nodes[idx] != null && (!Objects.equals(key, nodes[idx].key))) {
            // 开放寻址法：此处使用简单的线性探测，即如果当前位置已经被占据，且该位置的 key 与目标的 key 不同，则取下一个位置（越界则从头开始）。
            // 由于 nodes 满则会触发扩容，必然会找到一个位置能放入当前的元素。
            idx = (idx + 1) % nodes.length;
        }
        return idx;
    }

    /**
     *
     */
    private void resize() {

        // 此处简化实现，默认扩容为原容量的两倍（需要避免溢出）。
        int oldCapacity = nodes.length;
        int newCapacity = oldCapacity >= MAXIMUM_CAPACITY / 2? MAXIMUM_CAPACITY: oldCapacity * 2;

        // 非空的元素迁移到新的数组。
        Node<K, V>[] newNodes = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = nodes[i];
            if (node != null) {
                // 置空旧数组，并在新数组中寻找下标、放入元素。
                nodes[i] = null;
                int newIndex = getIndex(node.key, newNodes);
                newNodes[newIndex] = node;
            }
        }

        // 修改 nodes 的指向，完成扩容。
        nodes = newNodes;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return nodes.length;
    }

    private static class Node<K, V> {
        private final K key;
        private final V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}

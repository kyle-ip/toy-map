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
     * 使用数组保存，数组的下标即为 key 的 hash 值（mod）
     */
    private Node<K, V>[] nodes;

    /**
     * 默认初始化容量
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * 最大容量
     */
    private static final int MAX_CAPACITY = 1 << 30;

    /**
     * 装载因子，达到阈值后触发扩容，避免由于填充元素过多、哈希冲突频繁（待测试）
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

    /**
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        // 插入前判断，如果已经达到阈值则触发扩容。
        if (size >= DEFAULT_LOAD_FACTOR * nodes.length) {
            resize();
        }

        // 定位，如果找到一个空的位置则表示插入（长度 +1），否则表示更新。
        int idx = getIndex(key, nodes);
        if (nodes[idx] == null) {
            size++;
        }
        nodes[idx] = new Node<>(key, value);
    }

    /**
     *
     * @param key
     * @return
     */
    public V get(K key) {
        int idx = getIndex(key, nodes);
        Node<K, V> node = nodes[idx];
        if (node == null) {
            return null;
        }
        return node.value;
    }

    /**
     *
     * @param key
     * @param nodes
     * @return
     */
    private int getIndex(K key, Node<K, V>[] nodes) {
        int idx = 0;
        if (key != null) {
            // hashCode() 可能返回负数，不能直接取模作为下标，需要先取 abs。
            idx = Math.abs(key.hashCode()) % nodes.length;
        }
        while (nodes[idx] != null && (!Objects.equals(key, nodes[idx].key))) {
            // 开放寻址法：此处使用简单的线性探测，即如果当前位置已经被占据，且该位置的 key 与目标的 key 不同，则取下一个位置（越界则从头开始）。
            // 由于 nodes 满则会触发扩容，必然会找到一个位置能放入当前的元素，不会死循环。
            idx = (idx + 1) % nodes.length;
        }
        return idx;
    }

    /**
     *
     */
    private void resize() {

        // 此处简化实现，默认扩容为原容量的两倍（需要避免溢出）。
        int oldCapacity = nodes.length, newCapacity;
        if (oldCapacity >= MAX_CAPACITY / 2) {
            newCapacity = MAX_CAPACITY;
        } else {
            newCapacity = oldCapacity * 2;
        }

        // 非空的元素迁移到新的数组。
        Node<K, V>[] newNodes = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = nodes[i];
            if (node != null) {
                // 置空旧数组中的该项，并在新数组中寻找下标、放入元素。
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

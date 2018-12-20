package com.mc.manager.tool.info;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * value为set的map
 * todo:高并发下不会有线程问题吗？
 *
 * @author Liu Chunfu
 * @date 2018-08-28 下午5:00
 **/
public class MultiSetValueMap<K, V> {

    private Map<K, Set<V>> innerMap;

    /**
     * 容量的Size
     */
    private int containerSize;

    /**
     * value的size
     */
    private int valueSize;


    public MultiSetValueMap() {
        this.containerSize = 10;
        this.valueSize = 20;
        innerMap = new ConcurrentHashMap(containerSize);
    }

    public MultiSetValueMap(int containerSize, int valueSize) {
        this.containerSize = containerSize;
        this.valueSize = valueSize;
        innerMap = new ConcurrentHashMap<>(containerSize);
    }

    /**
     * 比较内容是否相等
     *
     * @param key
     * @param values
     * @return
     */
    public boolean equals(K key, Set<V> values) {
        Set<V> innerSet = innerMap.get(key);
        return values.equals(innerSet);
    }

    /**
     * 添加1个
     *
     * @param k
     * @param v
     * @return
     */
    public boolean put(K k, V v) {
        Set<V> set = innerMap.get(k);
        if (set == null) {
            set = new ConcurrentHashSet<>(valueSize);
            innerMap.put(k, set);
        }
        return set.add(v);
    }

    /**
     * 添加多个
     *
     * @param key
     * @param set
     * @return
     */
    public boolean putAll(K key, Set<V> set) {
        Set<V> oriSet = innerMap.get(key);
        if (oriSet == null) {
            oriSet = new ConcurrentHashSet<>(valueSize);
        }
        return oriSet.addAll(set);
    }

    /**
     * 通过主键进行移除
     *
     * @param key
     */
    public void remove(K key) {
        innerMap.remove(key);
    }

    /**
     * 删除一个
     *
     * @param key
     * @param value
     */
    public void remove(K key, V value) {
        Set<V> set = innerMap.get(key);
        if (CollectionUtil.isEmpty(set)) {
            return;
        }
        set.remove(value);
    }

    /**
     * 删除多个
     *
     * @param key
     * @param value
     */
    public void remove(K key, Set<V> value) {
        Set<V> set = innerMap.get(key);
        if (CollectionUtil.isEmpty(set)) {
            return;
        }
        set.removeAll(value);
    }

    /**
     * 通过value进行删除
     *
     * @param value
     */
    public void removeValue(V value) {
        if (value == null) {
            return;
        }
        Map<K, Set<V>> innerMap = this.innerMap;
        if (innerMap == null) {
            return;
        }
        Set<Map.Entry<K, Set<V>>> entries = innerMap.entrySet();
        Iterator<Map.Entry<K, Set<V>>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, Set<V>> next = iterator.next();
            Set<V> curSet = next.getValue();
            curSet.remove(value);
            if (!curSet.isEmpty()) {
                continue;
            }
            iterator.remove();
        }
    }

    /**
     * 获取value的set集合
     *
     * @param k
     * @return
     */
    public Set<V> valueSet(K k) {
        return innerMap.get(k);
    }

    /**
     * 请勿通过此接口进行内部值得更改
     *
     * @return
     */
    public Map<K, Set<V>> coreMap() {
        return innerMap;
    }


    @Override
    public String toString() {
        return innerMap.toString();
    }
}

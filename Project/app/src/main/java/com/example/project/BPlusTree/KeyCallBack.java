package com.example.project.BPlusTree;
/**
 * @author u7619947 Xinlong Wu
 */
public interface KeyCallBack<T, V extends Comparable<V>> {
    public V getKey(T val);
}

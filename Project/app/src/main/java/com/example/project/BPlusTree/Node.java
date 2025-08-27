package com.example.project.BPlusTree;

import java.util.ArrayList;
/**
 * @author u7619947 Xinlong Wu
 */
public abstract class Node<T,V extends Comparable<V>> {
    protected int bTreeOrder;
    protected Node<T,V> parent;
    protected Node<T,V>[] children;
    protected int childrenCount;
    protected Object[] keys;

    public Node(int order){
        bTreeOrder = order;
        keys = new Object[order+1];
        children = new Node[order+1];
        childrenCount = 0;
        parent = null;
    }

    abstract T find(V key);
    abstract ArrayList<T> findAll(V key);
    abstract Node<T,V> insert(V key, T value);
    abstract LeafNode<T,V> refreshLeft();
}

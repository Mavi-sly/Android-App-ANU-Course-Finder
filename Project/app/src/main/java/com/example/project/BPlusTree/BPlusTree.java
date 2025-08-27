package com.example.project.BPlusTree;

import java.util.ArrayList;
/**
 * @author u7619947 Xinlong Wu
 */
public class BPlusTree<T, V extends Comparable<V>> {

    private int bTreeOrder;
    private int maxNum;
    private Node<T,V> root;
    private LeafNode<T,V> left;

    public BPlusTree(){
        this(3);
    }

    public BPlusTree(int bTreeOrder){
        this.bTreeOrder = bTreeOrder;
        this.maxNum = bTreeOrder + 1;
        this.root = new LeafNode<T,V>(bTreeOrder);
        this.left = null;
    }

    public T find(V key){
        T res = this.root.find(key);
        return res;
    }

    public ArrayList<T> findAll(V key){
        ArrayList<T> res = this.root.findAll(key);
        return res;
    }

    public void insert(V key, T value){
        if (key == null)
            return;
        Node<T,V> node = this.root.insert(key, value);
        if (node != null)
            root = node;
        this.left = (LeafNode<T, V>) this.root.refreshLeft();
    }

}

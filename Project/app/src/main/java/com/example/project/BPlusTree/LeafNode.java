package com.example.project.BPlusTree;

import java.util.ArrayList;
/**
 * @author u7619947 Xinlong Wu
 */
public class LeafNode <T,V extends Comparable<V>> extends Node<T,V>{

    protected Object values[];
    protected LeafNode left;
    protected LeafNode right;

    public LeafNode(int order) {
        super(order);
        values = new Object[order+1];
        left = null;
        right = null;
    }

    @Override
    T find(V key) {
        if (childrenCount < 0)
            return null;

        int left = 0;
        int right = childrenCount;

        int middle = right / 2;

        while (left <= right){
            V middleKey = (V) keys[middle];
            int res = key.compareTo(middleKey);
            if (res == 0)
                return (T) values[middle];
            else if (res < 0)
                right = middle - 1;
            else
                left = middle + 1;
            middle = (left + right) / 2;
        }
        return null;
    }

    @Override
    ArrayList<T> findAll(V key) {
        int i = 0;
        for (;i < childrenCount;i++)
            if (key.compareTo((V) keys[i]) <= 0)
                break;

        ArrayList<T> res = new ArrayList<>();
        int j;
        for (j = i;j < childrenCount && key.compareTo((V) keys[j]) == 0; j++)
            res.add((T) values[j]);

        if (j >= childrenCount){
            LeafNode<T,V> node = right;
            while (node != null){
                for(j = 0; j < node.childrenCount && key.compareTo((V)node.keys[j]) == 0; j++)
                    res.add((T) node.values[j]);

                if (j < node.childrenCount)
                    break;
                else
                    node = node.right;
            }
        }

        return res;
    }

    @Override
    Node<T, V> insert(V key, T value) {
        V oldKey = null;
        if (childrenCount > 0)
            oldKey = (V) keys[childrenCount-1];
        
        int i;
        for (i = 0; i < childrenCount; i++)
            if (key.compareTo((V)keys[i])<0)
                break;

        Object tempKeys[] = new Object[keys.length];
        Object tempValues[] = new Object[values.length];

        System.arraycopy(keys, 0, tempKeys, 0, i);
        System.arraycopy(values, 0, tempValues, 0, i);
        System.arraycopy(keys, i, tempKeys, i+1, childrenCount-i);
        System.arraycopy(values, i, tempValues, i+1, childrenCount-i);
        tempKeys[i] = key;
        tempValues[i] = value;

        childrenCount++;


        // check if it need split
        if(childrenCount <= bTreeOrder){
            System.arraycopy(tempKeys, 0, keys, 0, childrenCount);
            System.arraycopy(tempValues, 0, values, 0, childrenCount);

            Node node = this;
            while (node.parent != null){
                V tempKey = (V) node.keys[node.childrenCount-1];
                if(tempKey.compareTo((V)node.parent.keys[node.parent.childrenCount-1])>0){
                    node.parent.keys[node.parent.childrenCount-1] = tempKey;
                    node = node.parent;
                }
                else
                    break;
            }
            return null;
        }

        // need to split, split from middle
        int middle = childrenCount / 2;

        // new leaf node, as right part
        LeafNode<T,V> tempNode = new LeafNode<>(bTreeOrder);
        tempNode.childrenCount = childrenCount - middle;

        // if parent is null, create a new parent(internal node), make tow node point to.
        if (parent == null){
            parent = new InternalNode<>(bTreeOrder);
            oldKey = null;
        }
        tempNode.parent = parent;

        System.arraycopy(tempKeys, middle, tempNode.keys, 0, tempNode.childrenCount);
        System.arraycopy(tempValues, middle, tempNode.values, 0, tempNode.childrenCount);

        // original node is left part
        childrenCount = middle;
        keys = new Object[bTreeOrder+1];
        values = new Object[bTreeOrder+1];
        System.arraycopy(tempKeys, 0, keys, 0, middle);
        System.arraycopy(tempValues, 0, values, 0, middle);

        LeafNode tempRight = right;
        right = tempNode;
        tempNode.left = this;
        tempNode.right = tempRight;

        return ((InternalNode<T,V>)parent).insertNode(this,tempNode,oldKey);
    }

    @Override
    LeafNode<T, V> refreshLeft() {
        if (childrenCount  <= 0)
            return null;
        return this;
    }
}

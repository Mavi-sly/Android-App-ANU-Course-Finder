package com.example.project.BPlusTree;

import java.util.ArrayList;
/**
 * @author u7619947 Xinlong Wu
 */
public class InternalNode<T,V extends Comparable<V>> extends Node<T,V> {

    public InternalNode(int order){
        super(order);
    }

    @Override
    T find(V key) {
        int i = 0;
        for (;i < childrenCount;i++){
            if (key.compareTo((V) keys[i]) <= 0)
                break;
        }
        if (childrenCount == i)
            return null;
        return children[i].find(key);
    }

    @Override
    ArrayList<T> findAll(V key) {
        int i = 0;
        for (;i < childrenCount;i++){
            if (key.compareTo((V) keys[i]) <= 0)
                break;
        }
        if (childrenCount == i)
            return new ArrayList<>();
        return children[i].findAll(key);
    }

    @Override
    Node<T, V> insert(V key, T value) {
        int i = 0;
        for (;i < childrenCount;i++){
            if (key.compareTo((V) keys[i]) < 0)
                break;
        }
        if (key.compareTo((V) keys[childrenCount-1]) >= 0)
            i--;
        return children[i].insert(key, value);
    }

    @Override
    LeafNode<T, V> refreshLeft() {
        return children[0].refreshLeft();
    }

    Node<T,V> insertNode(Node<T,V> node1, Node<T,V> node2, V key){
        V oldKey = null;
        if (childrenCount > 0)
            oldKey = (V) keys[childrenCount - 1];
        // key == null, means this node is empty, insert these two node
        if(key == null || childrenCount <= 0){
            keys[0] = node1.keys[node1.childrenCount - 1];
            keys[1] = node2.keys[node2.childrenCount - 1];

            children[0] = node1;
            children[1] = node2;

            childrenCount += 2;

            return this;
        }

        // key != null, find original node first
        int i;
        for (i = 0; i < childrenCount && children[i] != node1; ++i);

        // insert node1
        keys[i] = node1.keys[node1.childrenCount - 1];
        children[i] = node1;

        // insert node2
        Object tempKeys[] = new Object[keys.length];
        Node<T,V> tempChildern[] = new Node[children.length];

        System.arraycopy(keys, 0, tempKeys, 0, i+1);
        System.arraycopy(children, 0, tempChildern, 0, i+1);
        System.arraycopy(keys, i+1, tempKeys, i+2, childrenCount-i-1);
        System.arraycopy(children, i+1, tempChildern, i+2, childrenCount-i-1);
        tempKeys[i+1] = node2.keys[node2.childrenCount-1];
        tempChildern[i+1] = node2;

        this.childrenCount++;

        // check if it need to be split
        if (childrenCount <= bTreeOrder){
            System.arraycopy(tempKeys, 0, keys, 0, childrenCount);
            System.arraycopy(tempChildern, 0, children, 0, childrenCount);

            if (!oldKey.equals(keys[childrenCount-1])){
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
            }
            return null;
        }

        // need to be split
        int middle = childrenCount / 2;

        InternalNode<T,V> tempNode = new InternalNode<>(bTreeOrder);
        tempNode.childrenCount = childrenCount - middle;

        // if parent is null, crate a internal node as parent
        if (parent == null){
            parent = new InternalNode<>(bTreeOrder);
            oldKey = null;
        }
        tempNode.parent = parent;

        System.arraycopy(tempKeys, middle, tempNode.keys, 0, tempNode.childrenCount);
        System.arraycopy(tempChildern, middle, tempNode.children, 0, tempNode.childrenCount);
        for (int j = 0; j < tempNode.childrenCount; j++)
            tempNode.children[j].parent = tempNode;

        // let original internal node be left node
        childrenCount = middle;
        keys = new Object[bTreeOrder+1];
        children = new Node[bTreeOrder+1];
        System.arraycopy(tempKeys, 0, keys, 0, middle);
        System.arraycopy(tempChildern, 0, children, 0, middle);

        // insert new internal node to parent
        return ((InternalNode<T, V>)parent).insertNode(this,tempNode,oldKey);
    }
}

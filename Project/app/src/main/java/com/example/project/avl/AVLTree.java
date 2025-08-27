package com.example.project.avl;

import com.example.project.model.Course;
/**
 * @author u7726399 Meitong Liu
 */
public class AVLTree<T extends Comparable<T>> extends Tree<T> {

    public AVLTree(T value) {
        super(value);
        // Set left and right children to be of EmptyAVL as opposed to EmptyBST.
        this.leftNode = new EmptyAVL<>();
        this.rightNode = new EmptyAVL<>();
    }

    public AVLTree(T value, Tree<T> leftNode, Tree<T> rightNode) {
        super(value, leftNode, rightNode);
    }

    @Override
    public T min() {
        return (leftNode instanceof EmptyTree) ? value : leftNode.min();
    }

    @Override
    public T max() {
        return (rightNode instanceof EmptyTree) ? value : rightNode.max();
    }

    @Override
    public Tree<T> find(T element) {
        /*
            Left is less, right is greater in this implementation.
            compareTo returns 0 if both elements are equal.
            compareTo returns < 0 if the element is less than the node.
            compareTo returns > 0 if the element is greater than the node.
         */

        // Ensure input is not null.
        if (element == null)
            throw new IllegalArgumentException("Input cannot be null");

        if (element.compareTo(value) == 0) {
            return this;
        } else if (element.compareTo(value) < 0) {
            return leftNode.find(element);
        } else {
            return rightNode.find(element);
        }
    }

    /**
     * @return balance factor of the current node.
     */
    public int getBalanceFactor() {
        /*
             Note:
             Calculating the balance factor and height each time they are needed is less efficient than
             simply storing the height and balance factor as fields within each tree node (as some
             implementations of the AVLTree do). However, although it is inefficient, it is easier to implement.
         */
        return leftNode.getHeight() - rightNode.getHeight();
    }

    @Override
    public AVLTree<T> insert(T element) {

        AVLTree<T> node;

        // Ensure input is not null.
        if (element == null)
            throw new IllegalArgumentException("Input cannot be null");

        if (element.compareTo(value) > 0) {         // bigger than current node
            node= new AVLTree<>(value,leftNode,rightNode.insert(element));
        } else if (element.compareTo(value) < 0) {  // less than current node
            node= new AVLTree<>(value,leftNode.insert(element),rightNode);
        } else {        //equal to current node, return this
            return this;    //element already exist, no need to change anything
        }

        //balance for AVL Tree
        if(node.getBalanceFactor()<=1&&node.getBalanceFactor()>=-1){
            return node;        // already balanced.

        }else if(node.getBalanceFactor()<-1){
            if(node.rightNode.rightNode.getHeight()>=node.rightNode.leftNode.getHeight()){
                //Single Right Rotation
                node=node.leftRotate();
                return node;
            }else{
                //RL rotation
                AVLTree<T> subR = (AVLTree<T>)node.rightNode;
                subR=subR.rightRotate();
                AVLTree<T> new_final= new AVLTree<>(value,leftNode,subR);
                new_final=new_final.leftRotate();
                return new_final;
            }
        }else if(node.getBalanceFactor()>1){
            //need right rotate
            if(node.leftNode.leftNode.getHeight()>=node.leftNode.rightNode.getHeight()){
                //Single Right Rotation
                node=node.rightRotate();
                return node;
            }
            else{
                //LR rotation
                AVLTree<T> subL = (AVLTree<T>)node.leftNode;
                subL=subL.leftRotate();
                AVLTree<T> new_final= new AVLTree<>(value,subL,rightNode);
                new_final=new_final.rightRotate();
                return new_final;

            }


        }
        else return node;
    }

    /**
     * Conducts a left rotation on the current node.
     *
     * @return the new 'current' or 'top' node after rotation.
     */
    public AVLTree<T> leftRotate() {


        Tree<T> newParent = this.rightNode;
        Tree<T> newRightOfCurrent = newParent.leftNode;
        // COMPLETE
        newParent.leftNode=new AVLTree<>(value,leftNode,newRightOfCurrent);
        return (AVLTree<T>) newParent; // Change to return something different
    }

    /**
     * Conducts a right rotation on the current node.
     *
     * @return the new 'current' or 'top' node after rotation.
     */
    public AVLTree<T> rightRotate() {

        Tree<T> newParent = this.leftNode;
        Tree<T> newLeftOfCurrent = newParent.rightNode;
        newParent.rightNode=new AVLTree<>(value,newLeftOfCurrent,rightNode);
        return (AVLTree<T>) newParent;
    }

    public static class EmptyAVL<T extends Comparable<T>> extends EmptyTree<T> {
        @Override
        public Tree<T> insert(T element) {
            // The creation of a new Tree, hence, return tree.
            return new AVLTree<T>(element);
        }
    }
}

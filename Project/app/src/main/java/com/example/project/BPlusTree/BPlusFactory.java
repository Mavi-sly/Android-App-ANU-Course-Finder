package com.example.project.BPlusTree;

import com.example.project.avl.AVLTree;
import com.example.project.model.Course;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
/**
 * @author u7619947 Xinlong Wu
 */
public class BPlusFactory {

    public enum KeyType{
        Code,CodeSub,CodeNum,Subject,Name
    }
    private static HashMap<KeyType,BPlusTree<Course, Integer>> courseTree = new HashMap<>();
    private static List<Course> data = null;
    public static void setData(List<Course> data) {
        BPlusFactory.data = data;
    }

    private static BPlusTree<Course, Integer> generateBPlus(KeyType name, KeyCallBack<Course, Integer> key){
        // loop to get all resourses downloaded in the AVL tree
        BPlusTree<Course, Integer> tree = new BPlusTree<>(10);
        for (Course course: data)
            tree.insert(key.getKey(course), course);
        courseTree.put(name, tree);
        return courseTree.get(name);
    }

    public static BPlusTree<Course, Integer> getBPlusTree(KeyType name, KeyCallBack<Course, Integer> key){
        if (courseTree.containsKey(name))
            return courseTree.get(name);
        return generateBPlus(name, key);
    }

    public static BPlusTree<Course, Integer> getBPlusTree(KeyType name) {
        if (!courseTree.containsKey(name))
            return null;
        return courseTree.get(name);
    }
}

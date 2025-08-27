package com.example.project.avl;
import com.example.project.model.Course;
import com.example.project.utils.DataLoader;

import java.util.ArrayList;
import java.util.List;
/**
 * @author u7619947 Xinlong Wu u7726399 Meitong Liu
 */
public class AVLFactory {
    private static AVLTree<Course> courseTree = null;

    public static void setData(List<Course> data) {
        AVLFactory.data = data;
    }

    private static List<Course> data = null;
    private static AVLTree<Course> generateAVLFromList(){
        // loop to get all resourses downloaded in the AVL tree
        AVLTree<Course> avl = new AVLTree<>(data.get(0));
        for(int i=1;i<data.size();i++){
            avl = avl.insert(data.get(i));
        }
        return avl;
    }

    public static AVLTree<Course> getCourseAVLTree() {
        if (courseTree != null)
            return courseTree;

        courseTree = generateAVLFromList();
        return courseTree;
    }
}

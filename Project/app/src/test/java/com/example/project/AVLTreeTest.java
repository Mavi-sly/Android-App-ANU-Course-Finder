package com.example.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.project.avl.AVLFactory;
import com.example.project.avl.AVLTree;
import com.example.project.avl.Tree;
import com.example.project.model.Course;
import com.example.project.parser.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
/**
 * @author u7619947 Xinlong Wu u7726399 Meitong Liu
 */
public class AVLTreeTest {

    private ArrayList<Course> data;

    @Before
    public void init(){
        data = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 20; i++){
            data.add(new Course("TEST00"+String.valueOf(r.nextInt())+String.valueOf(r.nextInt())));
        }
        AVLFactory.setData(data);
    }

    @Test
    public void testCreate(){
        AVLTree<Course> tree = AVLFactory.getCourseAVLTree();
        assertNotNull(tree);
    }

    @Test
    public void testInsert(){
        AVLTree<Course> tree = AVLFactory.getCourseAVLTree();
        Random r = new Random();
        for(int i = 0;i<5;i++)
            tree.insert(new Course("TEST0"+String.valueOf(r.nextInt())+String.valueOf(r.nextInt())+String.valueOf(r.nextInt())));
    }

    @Test
    public void testFind(){
        AVLTree<Course> tree = AVLFactory.getCourseAVLTree();
        for (Course course :
                data) {
            Tree<Course> res = tree.find(course);
            assertEquals(course,res.getValue());
        }
    }
}

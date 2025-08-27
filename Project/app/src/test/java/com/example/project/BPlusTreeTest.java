package com.example.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.example.project.BPlusTree.BPlusFactory;
import com.example.project.BPlusTree.BPlusTree;
import com.example.project.BPlusTree.KeyCallBack;
import com.example.project.avl.AVLFactory;
import com.example.project.avl.AVLTree;
import com.example.project.avl.Tree;
import com.example.project.model.Course;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
/**
 * @author u7619947 Xinlong Wu u7726399 Meitong Liu
 */
public class BPlusTreeTest {

    private ArrayList<Course> data;

    @Before
    public void init(){
        data = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 20; i++){
            data.add(new Course("TEST00"+String.valueOf(r.nextInt())+String.valueOf(r.nextInt())));
        }
        BPlusFactory.setData(data);
    }

    @Test
    public void testCreate(){
        BPlusTree<Course, Integer> tree = BPlusFactory.getBPlusTree(BPlusFactory.KeyType.Code, new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                return val.getCode().substring(0,4).toLowerCase().hashCode();
            }
        });
        assertNotNull(tree);
    }

    @Test
    public void testDoubleCreate(){
        BPlusTree<Course, Integer> tree = BPlusFactory.getBPlusTree(BPlusFactory.KeyType.Code, new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                return val.getCode().substring(0,4).toLowerCase().hashCode();
            }
        });
        assertNotNull(tree);
        BPlusTree<Course, Integer> again = BPlusFactory.getBPlusTree(BPlusFactory.KeyType.Code, new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                assert(false);
                return 0;
            }
        });
        assertEquals(tree, again);
    }

    @Test
    public void testGetBeforeCreate(){
        BPlusTree<Course, Integer> tree = BPlusFactory.getBPlusTree(BPlusFactory.KeyType.CodeSub);
        assertNull(tree);
    }

    @Test
    public void testInsert(){
        KeyCallBack<Course, Integer> call = new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                return val.getCode().substring(0,4).toLowerCase().hashCode();
            }
        };
        BPlusTree<Course, Integer> tree = BPlusFactory.getBPlusTree(BPlusFactory.KeyType.Code, call);
        assertNotNull(tree);

        Random r = new Random();
        for(int i = 0;i<5;i++){
            Course course = new Course("TEST0"+String.valueOf(r.nextInt())+String.valueOf(r.nextInt())+String.valueOf(r.nextInt()));
            tree.insert(call.getKey(course), course);
        }
    }

    @Test
    public void testFind(){
        KeyCallBack<Course, Integer> call = new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                return val.getCode().toLowerCase().hashCode();
            }
        };
        BPlusTree<Course, Integer> tree = BPlusFactory.getBPlusTree(BPlusFactory.KeyType.Name, call);
        assertNotNull(tree);

        for (Course course :
                data) {
            Course res = tree.find(call.getKey(course));
            assertEquals(course,res);
        }
    }

    @Test
    public void testFindALL(){
        KeyCallBack<Course, Integer> call = new KeyCallBack<Course, Integer>() {
            @Override
            public Integer getKey(Course val) {
                return val.getCode().substring(0,4).toLowerCase().hashCode();
            }
        };
        BPlusTree<Course, Integer> tree = BPlusFactory.getBPlusTree(BPlusFactory.KeyType.Code, call);
        assertNotNull(tree);

        ArrayList<Course> res = tree.findAll(call.getKey(data.get(0)));
        assertEquals(data.size(),res.size());
    }
}

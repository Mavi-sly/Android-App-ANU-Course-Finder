package com.example.project;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.project.BPlusTree.BPlusTree;
import com.example.project.parser.CodeExp;
import com.example.project.parser.Exp;
import com.example.project.parser.NameExp;
import com.example.project.parser.Parser;
import com.example.project.parser.SubExp;
import com.example.project.parser.Tokenizer;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void bPlusTree(){
        BPlusTree<Integer,Integer> tree = new BPlusTree<>(3);
        for (int i = 0; i < 30; i++) {
            tree.insert(i,i);
        }
//        tree.insert(-1,-1);
    }





}
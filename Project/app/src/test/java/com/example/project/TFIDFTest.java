package com.example.project;

import static org.junit.Assert.assertEquals;

import com.example.project.TFIDF.Document;
import com.example.project.TFIDF.TFIDFFactory;
import com.example.project.model.Course;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TFIDFTest {
    public static ArrayList<Document> documents = new ArrayList<>();
    @BeforeClass
    public static void InitDocs(){
        Course course1 = new Course();
        course1.setName("Lorem ipsum dolor ipsum sit ipsum");
        TFIDFFactory.addDocument(course1);
        Course course2 = new Course();
        course2.setName("Vituperata incorrupte at ipsum pro quo");
        TFIDFFactory.addDocument(course2);
        Course course3 = new Course();
        course3.setName("Has persius disputationi id simul");
        TFIDFFactory.addDocument(course3);
        documents.add(course1);
        documents.add(course2);
        documents.add(course3);
    }

    @Test
    public void testTdIDF(){
        double res = TFIDFFactory.getTFIDF(documents.get(0), "ipsum");
        assertEquals(0.25541281188299536, res, 0.005);
    }
}

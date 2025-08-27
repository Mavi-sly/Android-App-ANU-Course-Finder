package com.example.project.utils;

import android.content.res.XmlResourceParser;

import com.example.project.model.Course;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
/**
 * @author u7619947 Xinlong Wu
 */
public class DataLoader {
    private static ArrayList<Course> Data = null;
    public static ArrayList<Course> getCourseList(XmlResourceParser xrp){
        // Singleton Pattern
        if (Data != null)
            return Data;
        ArrayList<Course> courses = new ArrayList<>();
        try {
            while (xrp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlPullParser.START_TAG && "course".equals(xrp.getName())) {
                    Course course = CourseFactory.createCourseFromXml(xrp);
                    if (course != null) {
                        courses.add(course);
                    }
                } else {
                    xrp.next();
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        Data = courses;
        return courses;
    }

    public static ArrayList<Course> getCourseList(){
        assert (Data != null);
        return Data;
    }
}

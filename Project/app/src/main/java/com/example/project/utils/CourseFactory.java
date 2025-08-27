package com.example.project.utils;

import com.example.project.model.Course;
import com.example.project.model.CourseSession;
import com.example.project.model.DeliverMode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * @author u7619947 Xinlong Wu
 */
public class CourseFactory {
    public static Course createCourseFromXml(XmlPullParser xrp) throws XmlPullParserException, IOException {
        Course course = null;
        String currentTag = null;

        while (xrp.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (xrp.getEventType()) {
                case XmlPullParser.START_TAG:
                    currentTag = xrp.getName();
                    if ("course".equals(currentTag)) {
                        course = new Course();
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("course".equals(xrp.getName())) {
                        return course;
                    }
                    currentTag = null;
                    break;

                case XmlPullParser.TEXT:
                    if (course != null && currentTag != null) {
                        switch (currentTag) {
                            case "code":
                                course.setCode(xrp.getText());
                                break;
                            case "name":
                                course.setName(xrp.getText());
                                break;
                            case "subject":
                                course.setSubject(xrp.getText());
                                break;
                            case "deliverMode":
                                course.setDeliverMode(DeliverMode.getDeliverMode(xrp.getText()));
                                break;
                            case "session":
                                course.setSession(CourseSession.getCourseSession(xrp.getText()));
                                break;
                            case "college":
                                course.setCollege(xrp.getText());
                                break;
                            case "offeredBy":
                                course.setOfferedBy(xrp.getText());
                                break;
                            case "unit":
                                course.setUnit(Float.parseFloat(xrp.getText()));
                                break;
                            case "career":
                                course.setCareer(xrp.getText());
                                break;
                            case "convener":
                                course.addConvener(xrp.getText());
                                break;

                        }
                    }
                    break;
            }
            xrp.next();
        }
        return course;
    }

//    public static String getCon
}

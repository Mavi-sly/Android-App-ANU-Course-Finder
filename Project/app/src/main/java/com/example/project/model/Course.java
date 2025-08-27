package com.example.project.model;

import com.example.project.TFIDF.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
/**
 * @author u7619947 Xinlong Wu
 */
public class Course extends Document
        implements Comparable<Course>, Serializable {
    private String code;
    private String name;
    private DeliverMode deliverMode;
    private String college;
    private String offeredBy;
    private float unit;
    private CourseSession session;
    private String subject;
    private Career career;
    private final ArrayList<String> conveners;

    public Course() {
        super("");
        conveners = new ArrayList<>();
        session = new CourseSession();
        career = Career.NONE;
    }

    public Course(String code) {
        this();
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        setTerms(name);
        this.name = name;
    }

    public DeliverMode getDeliverMode() {
        return deliverMode;
    }

    public void setDeliverMode(DeliverMode deliverMode) {
        this.deliverMode = deliverMode;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getOfferedBy() {
        return offeredBy;
    }

    public void setOfferedBy(String offeredBy) {
        this.offeredBy = offeredBy;
    }

    public float getUnit() {
        return unit;
    }

    public void setUnit(float unit) {
        this.unit = unit;
    }

    public CourseSession getSession() {
        return session;
    }

    public void setSession(CourseSession session) {
        this.session = session;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = Career.valueOf(career);
    }

    public ArrayList<String> getConveners() {
        return conveners;
    }

    public void addConvener(String convener) {
        this.conveners.add(convener);
    }

    /**
     * @author u6393399 Xuan Liu
     * @return a string representation of all conveners.
     */
    public String getAllConvenersAsString() {
        return String.join(", ", conveners);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Float.compare(course.unit, unit) == 0 && code.equals(course.code) && name.equals(course.name) && deliverMode == course.deliverMode && college.equals(course.college) && offeredBy.equals(course.offeredBy) && session == course.session && subject.equals(course.subject) && career.equals(course.career) && conveners.equals(course.conveners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, deliverMode, college, offeredBy, unit, session, subject, career, conveners);
    }

    public int compareTo(Course Course) {
        String code= this.getCode();
        return code.compareTo(Course.getCode());
    }
}
